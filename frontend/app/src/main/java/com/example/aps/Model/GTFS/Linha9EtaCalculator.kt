package com.example.aps.Model.GTFS

object Linha9EtaCalculator {

    // Velocidade média da Linha 9 (em km/min)
    private const val VELOCIDADE_MEDIA = 0.58 // ≈ 35 km/h

    // Distâncias aproximadas entre principais estações
    private val distanciasKm = mapOf(
        "Grajaú" to 0.0,
        "Primavera-Interlagos" to 2.3,
        "Jurubatuba" to 4.7,
        "Autódromo" to 7.1,
        "Santo Amaro" to 8.9,
        "Socorro" to 10.9,
        "Pinheiros" to 17.3,
        "Ceasa" to 19.1,
        "Villa-Lobos Jaguaré" to 20.8,
        "Cidade Universitária" to 22.0,
        "Presidente Altino" to 24.3,
        "Osasco" to 25.0
    )

    fun calcularEta(estacao: String, intervalo: Int?): Int? {
        val dist = distanciasKm[estacao] ?: return null
        if (intervalo == null) return null

        val tempoDeslocamento = dist / VELOCIDADE_MEDIA
        val etaEstimado = tempoDeslocamento + (intervalo / 2)

        return etaEstimado.toInt()
    }
}
