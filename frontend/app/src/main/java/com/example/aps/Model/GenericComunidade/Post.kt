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
    val timestamp: String?, // Opcional (String?) por segurança

    @SerializedName("line_id")
    val lineId: String,

    @SerializedName("user_name")
    val author: String?,

    @SerializedName("icon_url")
    val iconUrl: String? = null,

    @SerializedName("content")
    val content: String,
) {
    // ⭐️ CORREÇÃO APLICADA: Uso de 'get()' explícito para garantir que a propriedade seja calculada
    // Em Post.kt (dentro da propriedade timeAgo)

// ...

    // ⭐️ CÓDIGO CORRIGIDO
    val timeAgo: String
        get() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                if (timestamp.isNullOrBlank()) {
                    return "hoje"
                }

                return try {
                    // ⭐️ MUDANÇA AQUI: Usa um parser específico para o formato ISO com Offset (fuso horário)
                    val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
                    val zonedDateTime = ZonedDateTime.parse(timestamp, formatter)
                    val postTime = zonedDateTime.toInstant() // Converte para Instant para o cálculo ChronoUnit

                    val now = Instant.now()
                    val minutes = ChronoUnit.MINUTES.between(postTime, now)

                    when {
                        minutes < 1 -> "agora"
                        minutes < 60 -> "$minutes min atrás"
                        minutes < 60 * 24 -> "${minutes / 60}h atrás"
                        minutes < 60 * 24 * 7 -> "${ChronoUnit.DAYS.between(postTime, now)} dias atrás"
                        else -> {
                            val localZonedDateTime = ZonedDateTime.ofInstant(postTime, ZoneId.systemDefault())
                            localZonedDateTime.format(DateTimeFormatter.ofPattern("dd MMM"))
                        }
                    }
                } catch (e: Exception) {
                    // Seu log para debug
                    Log.e("PostModel", "Erro ao formatar timestamp: $timestamp. Erro: ${e.message}")
                    return "Data inválida" // Fallback seguro
                }
            } else {
                return "Data indisponível"
            }
        }
        }

