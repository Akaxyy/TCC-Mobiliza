package com.example.aps.Model.GenericNoticias


import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

data class LineInfo(
    val lineId: Int,
    val lineName: String,
    val corPrincipalHex: String
)

fun getLineData(nomeDaLinha: String): LineInfo? {
    fun Color.toHexString(): String = String.format("#%06X", (0xFFFFFF and this.toArgb()))

    val data = when (nomeDaLinha) {
        // METRÔ
        "Linha Azul Noticias" -> LineInfo(1, "Linha Azul", Color(0xFF0056A4).toHexString())
        "Linha Verde Noticias" -> LineInfo(2, "Linha Verde", Color(0xFF007A5F).toHexString())
        "Linha Vermelha Noticias" -> LineInfo(3, "Linha Vermelha", Color(0xFFC40233).toHexString())
        "Linha Amarela Noticias" -> LineInfo(4, "Linha Amarela", Color(0xFFFFCC00).toHexString())
        "Linha Lilás Noticias" -> LineInfo(5, "Linha Lilás", Color(0xFF8A008A).toHexString())

        // CPTM
        "Linha Rubi Noticias" -> LineInfo(7, "Linha Rubi", Color(0xFF9146A3).toHexString())
        "Linha Diamante Noticias" -> LineInfo(8, "Linha Diamante", Color(0xFF8B8C8A).toHexString())
        "Linha Esmeralda Noticias" -> LineInfo(9, "Linha Esmeralda", Color(0xFF00A38D).toHexString())
        "Linha Turquesa Noticias" -> LineInfo(10, "Linha Turquesa", Color(0xFF007C78).toHexString())
        "Linha Coral Noticias" -> LineInfo(11, "Linha Coral", Color(0xFFF7941D).toHexString())
        "Linha Safira Noticias" -> LineInfo(12, "Linha Safira", Color(0xFF00A0E3).toHexString())

        // MONOTRILHO
        "Linha Prata Noticias" -> LineInfo(15, "Linha Prata", Color(0xFFB5B5B5).toHexString())

        else -> null
    }
    return data
}