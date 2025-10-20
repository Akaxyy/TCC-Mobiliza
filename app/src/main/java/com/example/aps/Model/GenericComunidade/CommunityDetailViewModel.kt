package com.example.aps.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aps.data.repository.CommentRepository
import com.example.aps.Model.GenericComunidade.Post
import com.example.aps.services.CurrentUserService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.temporal.ChronoUnit

class CommunityDetailViewModel(
    private val commentRepository: CommentRepository = CommentRepository(),
    val currentUserService: CurrentUserService = CurrentUserService()
) : ViewModel() {

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    private val _commentContent = MutableStateFlow("")
    val commentContent: StateFlow<String> = _commentContent.asStateFlow()

    private val _isSendingComment = MutableStateFlow(false)
    val isSendingComment: StateFlow<Boolean> = _isSendingComment.asStateFlow()

    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    private val _postToEdit = MutableStateFlow<Post?>(null)
    val postToEdit: StateFlow<Post?> = _postToEdit.asStateFlow()

    private var currentLineId: String = ""


    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchPosts(lineId: String) {
        currentLineId = lineId
        viewModelScope.launch {
            val result = commentRepository.fetchCommentsByLineId(lineId)

            result.onSuccess { postsList: List<Post> ->
                _posts.value = (postsList ?: emptyList()).sortedByDescending {
                    try { Instant.parse(it.timestamp) } catch (e: Exception) { Instant.now().minusSeconds(1) }
                }
            }.onFailure { exception ->
                _userMessage.value = "Erro ao carregar posts: ${exception.message ?: "Erro desconhecido"}"
                _posts.value = emptyList()
            }
        }
    }



    fun onCommentContentChange(newContent: String) {
        _commentContent.value = newContent
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun handlePostAction() {
        if (_commentContent.value.isBlank()) {
            _userMessage.value = "O comentário não pode ser vazio."
            return
        }

        if (_postToEdit.value != null) {
            updateComment()
        } else {
            sendComment(currentLineId)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendComment(lineId: String) {
        val userId = currentUserService.userId

        if (userId.isEmpty()) {
            _userMessage.value = "Você precisa estar logado para comentar."
            return
        }

        _isSendingComment.value = true
        viewModelScope.launch {

            val result = commentRepository.createComment(
                userId = userId,
                lineId = lineId,
                content = _commentContent.value
            )

            _isSendingComment.value = false

            result.onSuccess {
                _commentContent.value = ""
                _userMessage.value = "Comentário enviado com sucesso!"
                fetchPosts(lineId)
            }.onFailure { exception ->
                _userMessage.value = "Erro ao enviar comentário: ${exception.message ?: "Erro desconhecido"}"
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateComment() {
        val post = _postToEdit.value ?: return

        _isSendingComment.value = true
        viewModelScope.launch {
            val result = commentRepository.updateComment(
                commentId = post.id,
                newContent = _commentContent.value,
                userId = post.userId
            )

            _isSendingComment.value = false

            result.onSuccess {
                _postToEdit.value = null
                _commentContent.value = ""
                _userMessage.value = "Comentário atualizado com sucesso!"
                fetchPosts(currentLineId)
            }.onFailure { exception ->
                _userMessage.value = "Erro ao atualizar comentário: ${exception.message ?: "Erro desconhecido"}"
            }
        }
    }


    fun startEditing(post: Post) {
        _postToEdit.value = post
        _commentContent.value = post.content
    }

    fun cancelEditing() {
        _postToEdit.value = null
        _commentContent.value = ""
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun canDeletePost(post: Post): Boolean {
        val currentUserId = currentUserService.userId
        if (post.userId != currentUserId || post.id == 0) {
            return false
        }
        return try {
            val postTime = Instant.parse(post.timestamp)
            val currentTime = Instant.now()
            val minutesDifference = ChronoUnit.MINUTES.between(postTime, currentTime)
            minutesDifference <= 30
        } catch (e: Exception) {
            Log.e("ViewModel", "Erro ao parsear timestamp para post ID ${post.id}: ${e.message}")
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteComment(postId: Int, userId: String) {
        viewModelScope.launch {
            _userMessage.value = null

            val result = commentRepository.deleteComment(postId, userId)

            result.onSuccess {
                _userMessage.value = "Comentário excluído com sucesso!"
                fetchPosts(currentLineId)
            }.onFailure { exception ->
                _userMessage.value = "Falha ao excluir o comentário: ${exception.message ?: "Erro desconhecido"}"
            }
        }
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }
}