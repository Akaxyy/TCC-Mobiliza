package com.example.aps.Screens.NoticiasScreens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun LinhaRubiNoticiasScreen(
    navController: NavHostController,
    // NOVO: Adiciona os parâmetros de acessibilidade
    textSizeFactor: Float,
    onTextSizeChange: (Float) -> Unit
) {
    Text(
        text = "OI DEU CERTO",
        fontSize = 16.sp * textSizeFactor // Aplica o fator de escala ao texto
    )
}

