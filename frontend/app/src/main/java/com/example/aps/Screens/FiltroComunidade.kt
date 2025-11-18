package com.example.aps.Screens

enum class FiltroComunidade(val display: String, val limit: Int) {
    ULTIMOS_20("Últimos 20 Posts", 20),
    ULTIMOS_30("Últimos 30 Posts", 30),
    ULTIMOS_50("Últimos 50 Posts", 50),
    ULTIMOS_100("Últimos 100 Posts", 100);

    // Define o valor padrão para o filtro inicial
    companion object {
        val DEFAULT = ULTIMOS_100
    }
}