package com.example.aps.Model.GenericNoticias

import StatusType
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.*
import java.time.format.DateTimeFormatter


class LineUpdateRepository(private val api: TrainApi) {

    @RequiresApi(Build.VERSION_CODES.O)
    private val SAO_PAULO_ZONE = ZoneId.of("America/Sao_Paulo")

    private fun mapStatus(situacao: String): StatusType {
        return when (situacao.uppercase()) {
            "OPERAÇÃO NORMAL" -> StatusType.NORMAL
            "VELOCIDADE REDUZIDA", "EM AJUSTE" -> StatusType.REDUCED_SPEED
            "PARALISADA", "SERVIÇO SUSPENSO" -> StatusType.SUSPENDED
            "ATIVIDADE PROGRAMADA" -> StatusType.PROGRAMMED_ACTIVITY
            "OPERAÇÃO ENCERRADA" -> StatusType.SERVICE_ENDED
            else -> StatusType.REDUCED_SPEED
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getUpdatesForLine(lineId: Int): List<StatusUpdate> {

        val statusIds = api.getRecentStatusIds(lineId)

        val detailedUpdates = statusIds.mapNotNull { statusID ->
            try {
                api.getStatusDetail(statusID.id)
            } catch (e: Exception) {
                null
            }
        }.sortedByDescending {
            OffsetDateTime.parse(it.criado).toInstant().toEpochMilli()
        }

        val mappedUpdates = detailedUpdates.map { detail ->
            val statusType = mapStatus(detail.situacao)

            val offsetDateTime = OffsetDateTime.parse(detail.criado)

            val saoPauloTime = offsetDateTime.atZoneSameInstant(SAO_PAULO_ZONE).toLocalTime()

            val formattedTime = saoPauloTime.format(DateTimeFormatter.ofPattern("HH:mm"))

            StatusUpdate(
                timestamp = detail.criado,
                formattedTime = formattedTime,
                description = detail.descricao?.ifBlank { detail.situacao } ?: detail.situacao,
                status = statusType
            )
        }

        return mappedUpdates
    }
}