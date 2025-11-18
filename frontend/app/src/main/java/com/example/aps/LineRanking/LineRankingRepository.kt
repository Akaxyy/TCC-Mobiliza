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

    private val RANKING_API_URL = "http://srv1070702.hstgr.cloud:5000/api/line-ranking"

    /**
     * Função que realiza a chamada HTTP e retorna o resultado.
     * Mantenho o nome fetchLineRanking para clareza, mas criaremos um wrapper loadRankings.
     */
    private suspend fun fetchLineRanking(): Result<List<LineRankingItem>> = withContext(Dispatchers.IO) {
        try {
            Log.d("REPO_RANKING", "Tentando buscar ranking na URL: $RANKING_API_URL")

            val request = Request.Builder()
                .url(RANKING_API_URL)
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                val responseBodyString = response.body?.string()

                if (response.isSuccessful) {
                    val json = responseBodyString ?: "{\"data\":[]}" // Ajustei para 'data' conforme a LineRankingResponse

                    try {
                        val responseType = object : TypeToken<LineRankingResponse>() {}.type
                        val rankingResponse: LineRankingResponse = gson.fromJson(json, responseType)
                        return@withContext Result.success(rankingResponse.data)
                    } catch (e: Exception) {
                        Log.e("REPO_RANKING", "Erro de formato JSON: ${e.message}")
                        Result.failure(IOException("Erro de formato de dados da API: ${e.message}"))
                    }
                } else {
                    Log.e("REPO_RANKING", "Falha HTTP: ${response.code}. Erro: $responseBodyString")
                    Result.failure(IOException("Falha ao buscar ranking: ${response.code}. Erro: $responseBodyString"))
                }
            }
        } catch (e: Exception) {
            Log.e("REPO_RANKING", "Erro de rede/IO: ${e.message}")
            Result.failure(e)
        }
    }

    // ⭐️ NOVO: Função pública chamada pelo ViewModel que trata o Result
    suspend fun loadRankings(): List<LineRankingItem> {
        val result = fetchLineRanking()

        return result.getOrElse {
            Log.e("LineRankingRepository", "Falha crítica ao carregar rankings, retornando lista vazia. Erro: ${it.message}")
            // Retorna uma lista vazia em caso de falha, garantindo que o ViewModel não quebre.
            emptyList()
        }
    }
}