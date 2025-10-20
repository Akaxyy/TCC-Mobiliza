package com.example.aps.Screens.NoticiasScreens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun LinhaEsmeraldaComunidadeScreen(
    navController: NavHostController,
    textSizeFactor: Float,
    onTextSizeChange: (Float) -> Unit
) {
    Text(
        text = "Notícias da Linha Diamante",
        fontSize = 16.sp * textSizeFactor
    )
}