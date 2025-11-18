package com.example.aps.Model.GTFS

data class TrainArrival(
    val lineId: String,
    val stationName: String,
    val arrivalTimeInMinutes: Int?,
    val status: String,
    val isDelayed: Boolean = false
)