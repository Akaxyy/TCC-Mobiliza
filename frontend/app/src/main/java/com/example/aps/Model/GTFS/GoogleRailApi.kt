package com.example.aps.Model.GTFS

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object GoogleRailApi {

    suspend fun getRealtimeEta(lat: Double, lon: Double): Int? {
        return withContext(Dispatchers.IO) {
            try {
                val url =
                    "https://m.gm.gdn/transit/rail/eta?lat=$lat&lng=$lon&radius=1200"

                val connection = URL(url).openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                // Os headers são ESSENCIAIS
                connection.setRequestProperty(
                    "User-Agent",
                    "com.google.android.apps.maps/10.60.0 (Linux; Android 11)"
                )
                connection.setRequestProperty("X-App-Version", "10.60.0")
                connection.setRequestProperty("Accept", "application/json")
                connection.setRequestProperty("X-Android-Package", "com.google.android.apps.maps")
                connection.setRequestProperty("X-Android-Cert", "F0FD...") // fingerprint dummy
                connection.connectTimeout = 6000
                connection.readTimeout = 6000

                val response = connection.inputStream.bufferedReader().readText()
                Log.d("GoogleRailApi", "Google ETA RAW: $response")

                val json = JSONObject(response)

                if (!json.has("etas")) return@withContext null

                val etas = json.getJSONArray("etas")
                if (etas.length() == 0) return@withContext null

                val firstTrain = etas.getJSONObject(0)
                val minutes = firstTrain.optInt("minutes", -1)

                return@withContext if (minutes > 0) minutes else null

            } catch (e: Exception) {
                Log.e("GoogleRailApi", "Erro Google ETA: ${e.message}")
                null
            }
        }
    }
}
