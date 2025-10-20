// src/main/java/com/example/aps/ResultColorsTrajeto/LineColors.kt
package com.example.aps.ResultColorsTrajeto

import androidx.compose.ui.graphics.Color

fun getLineColor(lineName: String): Color {
    val normalizedLineName = lineName.trim().lowercase()

    return when (normalizedLineName) {


        "linha 1 - azul" -> Color(0xFF005AA6) // Azul
        "linha 2 - verde" -> Color(0xFF00804D) // Verde
        "linha 3 - vermelha" -> Color(0xFFEE3737) // Vermelha
        "linha 4 - amarela" -> Color(0xFFFFCC00) // Amarela
        "linha 5 - lilás" -> Color(0xFF9146A3) // Lilás
        "linha 15 - prata" -> Color(0xFFC0C0C0) // Prata

        // CPTM
        "linha 7 - rubi" -> Color(0xFFA61E47) // Rubi
        "linha 8 - diamante" -> Color(0xFF908D8C) // Diamante
        "linha 9 - esmeralda" -> Color(0xFF00B09A) // Esmeralda
        "linha 10 - turquesa" -> Color(0xFF00A2C9) // Turquesa
        "linha 11 - coral" -> Color(0xFFF16522) // Coral
        "linha 12 - safira" -> Color(0xFF0079A3) // Safira

        "anda um pouco" -> Color(0xFF908D8C)
        "transferencia" -> Color(0xFF908D8C)

        else -> {
            println("ALERTA: Linha não mapeada: $lineName")
            Color.Blue
        }
    }
}