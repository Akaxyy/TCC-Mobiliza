package com.example.aps.Model


data class TrajetoResponse(
    val caminho: List<Etapa>,
    val baldeacoes: List<Baldeacao>?,

    val estatisticas: Estatisticas
)

data class Etapa(
    val estacao: String,
    val linha: String // Este campo será usado para buscar o Score
)

data class Baldeacao(
    val estacao: String,
    val sairDa: String,
    val entrarNa: String
)

data class Estatisticas(
    val numeroEstacoes: Int,
    val numeroBaldeacoes: Int,
    val tempoEstimadoMinutos: Int,
    val tempoEstimadoFormatado: String,

    val lineScore: Double? = null
)