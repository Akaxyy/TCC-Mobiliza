// src/main/java/com/example/aps/API/ApiServiceTrajeto.kt
package com.example.aps.API

import com.example.aps.Model.TrajetoResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiServiceTrajeto {
    @GET("rota")
    suspend fun getTrajeto(
        @Query("partida") origem: String,
        @Query("chegada") destino: String
    ): TrajetoResponse

    // Nova função para testar com a URL completa
    @GET
    suspend fun getTrajetoComUrlCompleta(@Url url: String): TrajetoResponse
}