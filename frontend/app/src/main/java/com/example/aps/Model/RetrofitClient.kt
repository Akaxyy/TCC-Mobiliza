package com.example.aps.API

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://srv1070702.hstgr.cloud:5000/api/" // A constante que você mudou

    // Cria o interceptor para log de requisições
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Define o nível de log para mostrar o corpo
    }

    // Cria o cliente OkHttp com o interceptor
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val apiService: ApiServiceTrajeto by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient) // Usa o cliente com o interceptor
            .build()
            .create(ApiServiceTrajeto::class.java)
    }
}