package com.example.aps.Model.GenericComunidade

import android.os.Build
import android.util.Log
import com.google.gson.annotations.SerializedName
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.time.format.DateTimeFormatter
import java.time.ZoneId
import java.time.ZonedDateTime

data class Post(
    @SerializedName("comment_id")
    val id: Int = 0,

    @SerializedName("user_id")
    val userId: String,

    @SerializedName("created_at")
    val timestamp: String,

    @SerializedName("line_id")
    val lineId: String,

    @SerializedName("user_name")
    val author: String,

    @SerializedName("icon_url")
    val iconUrl: String? = null,

    @SerializedName("content")
    val content: String,

    val timeAgo: String = run {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (timestamp.isNullOrBlank()) {
                return@run "Sem data"
            }
            return@run try {
                val postTime = Instant.parse(timestamp)
                val now = Instant.now()
                val minutes = ChronoUnit.MINUTES.between(postTime, now)

                when {
                    minutes < 1 -> "agora"
                    minutes < 60 -> "$minutes min atrás"
                    minutes < 60 * 24 -> "${minutes / 60}h atrás"
                    minutes < 60 * 24 * 7 -> "${ChronoUnit.DAYS.between(postTime, now)} dias atrás"
                    else -> {
                        val zonedDateTime = ZonedDateTime.ofInstant(postTime, ZoneId.systemDefault())
                        zonedDateTime.format(DateTimeFormatter.ofPattern("dd MMM"))
                    }
                }
            } catch (e: Exception) {
                Log.e("PostModel", "Erro ao formatar timestamp: $timestamp. Erro: ${e.message}")
                "Data inválida"
            }
        } else {
            "Data indisponível"
        }
    }
)