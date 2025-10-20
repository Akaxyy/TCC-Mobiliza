package com.example.aps.data.repository

import android.util.Log
import com.example.aps.LineRanking.LineRankingItem
import com.example.aps.LineRanking.LineRankingResponse

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class LineRankingRepository {

    private val client = OkHttpClient()
    private val gson = Gson()

    private val RANKING_API_URL = "https://348106ec772d.ngrok-free.app/api/line-ranking"

    suspend fun fetchLineRanking(): Result<List<LineRankingItem>> = withContext(Dispatchers.IO) {
        try {
            Log.d("REPO_RANKING", "Tentando buscar ranking na URL: $RANKING_API_URL")

            val request = Request.Builder()
                .url(RANKING_API_URL)
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                val responseBodyString = response.body?.string()

                if (response.isSuccessful) {
                    val json = responseBodyString ?: "{\"dados\":[]}"

                    try {
                        val responseType = object : TypeToken<LineRankingResponse>() {}.type
                        val rankingResponse: LineRankingResponse = gson.fromJson(json, responseType)
                        return@withContext Result.success(rankingResponse.data)
                    } catch (e: Exception) {
                        Log.e("REPO_RANKING", "Erro de formato JSON: ${e.message}")
                        Result.failure(IOException("Erro de formato de dados da API: ${e.message}"))
                    }
                } else {
                    Result.failure(IOException("Falha ao buscar ranking: ${response.code}. Erro: $responseBodyString"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}