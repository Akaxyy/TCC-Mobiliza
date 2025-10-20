package com.example.aps.Model.GenericScreenEstacoes

import androidx.compose.ui.graphics.Color

data class LinhaBaldeacao(
    val numeroLinha: String,
    val cor: Color
)

data class Estacao(
    val nome: String,
    val éTransferencia: Boolean = false,
    val linhasBaldeacao: List<LinhaBaldeacao> = emptyList()
)

data class EstacaoData(
    val numeroLinha: String,
    val nomeLinha: String,
    val corPrincipal: Color,
    val estacoes: List<Estacao>
)