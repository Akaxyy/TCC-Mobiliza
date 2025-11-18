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
// ⚠️ IMPORTAÇÕES DE CÁLCULO DE DATA REMOVIDAS:
// import java.time.ZonedDateTime
// import java.time.ZoneOffset
// import java.time.format.DateTimeFormatter
import java.time.Instant // Mantido para ordenação e regra de exclusão (30 min)
import java.time.temporal.ChronoUnit // Mantido para regra de exclusão (30 min)

import com.example.aps.Model.GTFS.LineData
import com.example.aps.Model.GTFS.TrainArrival
import com.example.aps.Screens.FiltroComunidade // Importação do Enum com 'limit'
import kotlinx.coroutines.delay
import kotlin.random.Random

class CommunityDetailViewModel(
    private val commentRepository: CommentRepository = CommentRepository(),
    val currentUserService: CurrentUserService = CurrentUserService()
) : ViewModel() {

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    // ⭐️ Configurado para usar o DEFAULT (100) do Enum
    private val _selectedFilter = MutableStateFlow(FiltroComunidade.DEFAULT)
    val selectedFilter: StateFlow<FiltroComunidade> = _selectedFilter.asStateFlow()

    private val _commentContent = MutableStateFlow("")
    val commentContent: StateFlow<String> = _commentContent.asStateFlow()

    private val _isSendingComment = MutableStateFlow(false)
    val isSendingComment: StateFlow<Boolean> = _isSendingComment.asStateFlow()

    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    private val _postToEdit = MutableStateFlow<Post?>(null)
    val postToEdit: StateFlow<Post?> = _postToEdit.asStateFlow()

    private val _trainArrival = MutableStateFlow<TrainArrival?>(null)
    val trainArrival: StateFlow<TrainArrival?> = _trainArrival.asStateFlow()

    private var currentLineId: String = ""

    // ⭐️ FUNÇÃO: Atualiza o filtro e recarrega os posts
    @RequiresApi(Build.VERSION_CODES.O)
    fun setFilter(filter: FiltroComunidade, lineId: String) {
        if (_selectedFilter.value != filter) {
            _selectedFilter.value = filter
            // Chama a busca, que usará o novo limite
            fetchPosts(lineId)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchPosts(lineId: String) {
        currentLineId = lineId
        viewModelScope.launch {

            val currentFilter = _selectedFilter.value

            // 1. Obtém o limite (20, 30, 50 ou 100) do Enum
            val limit = currentFilter.limit

            // 2. startDate não é mais usado
            // Removido da chamada: startDate = getStartDateForFilter(currentFilter) ⚠️

            val result = commentRepository.fetchCommentsByLineId(
                lineId = lineId,
                limit = limit,      // Passa o limite
                // startDate não é passado, pois foi removido do Repository
            )

            result.onSuccess { postsList: List<Post> ->
                // Ordenação mantida usando o timestamp
                _posts.value = (postsList ?: emptyList()).sortedByDescending {
                    try { Instant.parse(it.timestamp) } catch (e: Exception) { Instant.now().minusSeconds(1) }
                }
                clearUserMessage()
            }.onFailure { exception ->
                _userMessage.value = "Erro ao carregar posts: ${exception.message ?: "Erro desconhecido"}"
                _posts.value = emptyList()
            }
        }
    }

    // ⚠️ FUNÇÃO getStartDateForFilter FOI REMOVIDA ⚠️


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

    // Regra de exclusão em 30 minutos mantida, utilizando Instant/ChronoUnit
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

    // ⭐️ FUNÇÃO DE PREVISÃO DE CHEGADA (SIMULAÇÃO GTFS) ⭐️
    fun fetchTrainArrival(numeroLinha: String) {
        viewModelScope.launch {
            delay(1000)

            val programmedHeadway = LineData.HEADWAY
            val randomDeviation = Random.nextInt(-2, 4)
            val simulatedArrival = (programmedHeadway + randomDeviation).coerceAtLeast(1)
            val randomDestination = LineData.DESTINATIONS.random()

            val statusMessage = when {
                randomDeviation > 1 -> "Atraso: ${randomDeviation} min acima do programado."
                randomDeviation < -1 -> "Adiantado: Chegada mais rápida que o normal."
                else -> "Tudo normal. No horário programado."
            }

            _trainArrival.value = TrainArrival(
                lineId = numeroLinha,
                stationName = randomDestination,
                arrivalTimeInMinutes = simulatedArrival,
                status = statusMessage,
                isDelayed = randomDeviation > 1
            )
        }
    }
    // ----------------------------------------------------


    fun clearUserMessage() {
        _userMessage.value = null
    }
}