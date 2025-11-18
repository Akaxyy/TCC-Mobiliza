// src/main/java/com/example/aps/Model/LineStatus.kt
package com.example.aps.Model

// Representa uma entrada de status de linha em um horário específico
data class StatusEntry(
    val time: String,
    val description: String,
    val statusColor: StatusColor // Usa um enum para a cor do status
)

// Enum para representar o status de forma mais segura
enum class StatusColor {
    GREEN, YELLOW, RED
}

// Representa o histórico de status de um dia
data class DailyStatus(
    val day: String,
    val entries: List<StatusEntry>
)