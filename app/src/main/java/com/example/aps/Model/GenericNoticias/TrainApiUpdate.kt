package com.example.aps.Model.GenericNoticias

import retrofit2.http.GET
import retrofit2.http.Path

data class TrainApiUpdate(
    val lineId: String,
    val timestamp: Long,
    val status: String,
    val message: String?
)





interface TrainApi {

    // A rota mais provável para obter a lista de eventos:
    @GET("status/linha/{lineId}/events") // Chute um endpoint que traga a lista de eventos
    suspend fun getLineEvents(@Path("lineId") lineId: Int): List<StatusDetail>


    @GET("status/codigo/{linha}")
    suspend fun getRecentStatusIds(@Path("linha") lineId: Int): List<StatusID>

    @GET("status/id/{id}")
    suspend fun getStatusDetail(@Path("id") id: Long): StatusDetail

}