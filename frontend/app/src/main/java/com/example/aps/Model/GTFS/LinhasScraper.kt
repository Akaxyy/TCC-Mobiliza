package com.example.aps.Model.GTFS

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

object LinhasScraper {

    // URLs oficiais (ViaMobilidade e Metrô)
    private val urlsPorLinha = mapOf(
        "L09" to "https://www.viamobilidade.com.br/linha-9-esmeralda",
        "L08" to "https://www.viamobilidade.com.br/linha-8-diamante",
        "L05" to "https://www.viamobilidade.com.br/linha-5-lilas",
        "L17" to "https://www.viamobilidade.com.br/linha-17-ouro",
        "L04" to "https://www.viamobilidade.com.br/linha-4-amarela",
        // Metrô SP
        "L01" to "https://www.metro.sp.gov.br/sua-viagem/linhas/linha-1-azul/index.aspx",
        "L02" to "https://www.metro.sp.gov.br/sua-viagem/linhas/linha-2-verde/index.aspx",
        "L03" to "https://www.metro.sp.gov.br/sua-viagem/linhas/linha-3-vermelha/index.aspx",
        "L15" to "https://www.metro.sp.gov.br/sua-viagem/linhas/monotrilho-linha-15-prata/index.aspx"
    )

    /**
     * Scraping genérico para QUALQUER linha
     */
    suspend fun getIntervalo(linhaId: String): Int? = withContext(Dispatchers.IO) {

        val url = urlsPorLinha[linhaId]

        if (url == null) {
            Log.e("LinhasScraper", "URL da linha $linhaId não encontrada")
            return@withContext null
        }

        return@withContext try {
            val doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0")
                .timeout(7000)
                .get()

            val texto = doc.body().text()
            Log.d("LinhasScraper", "Página carregada para linha $linhaId")

            // 🔍 Regex genérico: pega "Intervalo: 6 min" ou "Intervalo médio: 4 min"
            val regex = Regex("(Intervalo médio|Intervalo)[:\\s]+(\\d+)\\s*min")
            val match = regex.find(texto)

            val intervalo = match?.groupValues?.get(2)?.toInt()

            Log.d("LinhasScraper", "Linha $linhaId → intervalo extraído: $intervalo min")
            intervalo

        } catch (e: Exception) {
            Log.e("LinhasScraper", "Erro scraping linha $linhaId: ${e.message}")
            null
        }
    }

    /**
     * API dedicada para Linha 9 (atalho)
     */
    suspend fun getIntervaloLinha9(): Int? {
        return getIntervalo("L09")
    }
}
