package com.example.aps.data.repository

import android.util.Log
import com.example.aps.Model.GenericComunidade.Post
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.lang.reflect.Type

data class DeleteCommentPayload(
    @SerializedName("user_id") val userId: String
)

data class PostsWrapper(
    @SerializedName("data") val posts: List<Post>
)

data class CreateCommentPayload(
    @SerializedName("user_id") val userId: String,
    @SerializedName("line_id") val lineId: String,
    val content: String
)

data class UpdateCommentPayload(
    val content: String,
    @SerializedName("user_id") val userId: String
)


class CommentRepository {

    private val client = OkHttpClient()
    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()
    private val gson = Gson()

    private val COMMENT_API_URL = "https://348106ec772d.ngrok-free.app/api/comments"

    suspend fun fetchCommentsByLineId(lineId: String): Result<List<Post>> = withContext(Dispatchers.IO) {
        try {
            val fetchUrl = "$COMMENT_API_URL?line_id=$lineId"
            Log.d("REPO_DEBUG", "Tentando buscar posts para Linha ID: $lineId na URL: $fetchUrl")

            val request = Request.Builder()
                .url(fetchUrl)
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                val responseBodyString = response.body?.string()

                if (response.isSuccessful) {
                    val json = responseBodyString ?: "[]"

                    try {
                        val listType: Type = object : TypeToken<List<Post>>() {}.type
                        val posts: List<Post> = gson.fromJson(json, listType)
                        return@withContext Result.success(posts)
                    } catch (e: JsonSyntaxException) {
                        try {
                            val wrapperType: Type = object : TypeToken<PostsWrapper>() {}.type
                            val wrapper: PostsWrapper = gson.fromJson(json, wrapperType)
                            Result.success(wrapper.posts)
                        } catch (e2: Exception) {
                            Result.failure(IOException("Erro de formato de dados da API: ${e2.message}"))
                        }
                    }
                } else {
                    Result.failure(IOException("Falha ao buscar posts: ${response.code}. Erro: $responseBodyString"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun createComment(userId: String, lineId: String, content: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val payload = CreateCommentPayload(userId, lineId, content)
            val jsonBody = gson.toJson(payload)

            val body = jsonBody.toRequestBody(jsonMediaType)
            val request = Request.Builder().url(COMMENT_API_URL).post(body).build()

            Log.d("REPO_DEBUG", "POST Payload (Final): $jsonBody")

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    val errorBody = response.body?.string()
                    Log.e("CommentRepository", "Falha ao criar comentário: ${response.code}. Erro: $errorBody")
                    Result.failure(IOException("Falha ao criar comentário: ${response.code}. Erro: $errorBody"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteComment(commentId: Int, userId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val deleteUrl = "$COMMENT_API_URL/$commentId"

            val payload = DeleteCommentPayload(userId)
            val jsonBody = gson.toJson(payload)
            val body = jsonBody.toRequestBody(jsonMediaType)

            Log.d("REPO_DELETE", "Tentando deletar ID: $commentId com User ID: $userId na URL: $deleteUrl. Body: $jsonBody")

            val request = Request.Builder()
                .url(deleteUrl)
                .delete(body) // <-- Passa o corpo para o método DELETE
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    val errorBody = response.body?.string()
                    Result.failure(IOException("Falha ao deletar comentário ID $commentId. Código: ${response.code}. Erro: $errorBody"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



    suspend fun updateComment(commentId: Int, newContent: String, userId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val payload = UpdateCommentPayload(newContent, userId)
            val jsonBody = gson.toJson(payload)

            val body = jsonBody.toRequestBody(jsonMediaType)
            val updateUrl = "$COMMENT_API_URL/$commentId"

            val request = Request.Builder()
                .url(updateUrl)
                .put(body)
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    val errorBody = response.body?.string()
                    Result.failure(IOException("Falha ao atualizar comentário ID $commentId. Código: ${response.code}. Erro: $errorBody"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}