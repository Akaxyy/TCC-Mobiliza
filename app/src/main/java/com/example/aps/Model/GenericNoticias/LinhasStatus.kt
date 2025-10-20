package com.example.aps.Model.GenericNoticias

import StatusType
import androidx.compose.ui.graphics.Color




data class UpdatesSection(
    val day: String, // Ex: "Hoje", "Ontem"
    val updates: List<StatusUpdate>
)

data class LineUpdatesState(
    val updatesByDay: List<UpdateSection> = emptyList(),
    val legend: List<StatusType> = StatusType.entries.toList(),
    val isLoading: Boolean = false,
    val error: String? = null
)


enum class StatusColor(val color: Color, val description: String) {
    GREEN(Color(0xFF00C853), "Tudo funcionando sem problemas"), // Verde
    YELLOW(Color(0xFFFFD600), "Operando com ajustes ou velocidade menor"), // Amarelo
    RED(Color(0xFFD50000), "Serviço suspenso ou com falhas") // Vermelho
}
