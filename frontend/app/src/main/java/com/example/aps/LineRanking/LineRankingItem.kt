package com.example.aps.LineRanking

import com.google.gson.annotations.SerializedName

data class LineRankingItem(
    @SerializedName("line_id")
    val lineId: Int,
    val score: Double,
    @SerializedName("created_at")
    val createdAt: String
)

data class LineRankingResponse(
    @SerializedName("sucesso")
    val success: Boolean,
    @SerializedName("data_atual")
    val currentDate: String,
    @SerializedName("total_registros")
    val totalRecords: Int,
    @SerializedName("dados")
    val data: List<LineRankingItem>
)



