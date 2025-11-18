package com.example.aps.Model.GTFS


// Objeto para simular dados GTFS Estático pré-processados
object LineData {
    // ⭐️ NOVO HEADWAY (Intervalo) em minutos, extraído da análise do frequencies.txt (420 segundos).
    val HEADWAY: Int = 7

    // Estações de destino simuladas para a linha
    val DESTINATIONS = listOf("Osasco", "Grajaú", "Jurubatuba", "Santo Amaro", "Luz")
}