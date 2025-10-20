package com.example.aps.Model.GenericComunidade

import androidx.compose.ui.graphics.Color

data class CommunityData(
    val numeroLinha: String,
    val nomeLinha: String,
    val corPrincipal: Color,
    val posts: List<Post>
)