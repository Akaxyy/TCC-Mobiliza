package com.example.aps.Model

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class StatusLinhas(
    val rubi: String,
    val diamante: String,
    val esmeralda: String,
    val turquesa: String,
    val coral: String,
    val safira: String
)