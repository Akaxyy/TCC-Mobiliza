package com.example.aps.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class UserRepository {
    private val client = OkHttpClient()
    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()
    private val SYNC_URL = "http://srv1070702.hstgr.cloud:5000/api/user"


    suspend fun syncUserWithSupabase(user: FirebaseUser): Result<Boolean> = withContext(Dispatchers.IO) {
        val userId = user.uid
        val email = user.email ?: ""
        val name = user.displayName ?: ""
        val icon = user.photoUrl?.toString()?: ""

        val jsonBody = """
            {
                "user_id": "$userId",
                "email": "$email",
                "name": "$name",
                "icon_url": "$icon"
            }
        """.trimIndent()

        val body = jsonBody.toRequestBody(jsonMediaType)

        val request = Request.Builder()
            .url(SYNC_URL)
            .post(body)
            .build()


        try {
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    Log.d("UserRepository", "API SUCESSO: Usuário $userId sincronizado com sucesso!")
                    Result.success(true) // <-- Retorna sucesso com true
                } else {
                    val errorBody = response.body?.string()
                    Log.e("UserRepository", "API ERRO: Falha ao sincronizar usuário $userId. Código: ${response.code}, Erro: $errorBody")
                    Result.failure(IOException("Falha na sincronização de usuário: ${response.code}. Erro: $errorBody")) // <-- Retorna falha
                }
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "API ERRO: Exceção de rede ao sincronizar usuário $userId: ${e.message}", e)
            Result.failure(e) // <-- Retorna falha
        }
    }
}