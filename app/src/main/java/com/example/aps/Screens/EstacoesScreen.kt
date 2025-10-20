package com.example.aps.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.aps.Model.MetroLinesInterface // Assume-se que esta interface contém a lista de linhas

@Composable
fun EstacoesScreen(
    navController: NavHostController,
    textSizeFactor: Float,
    onTextSizeChange: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MetroLinesInterface(
            onLineClick = { lineName ->

                val nomeParaRota = when (lineName) {
                    "Linha Rubi Estacoes" -> "Linha Rubi"
                    "Linha Diamante Estacoes" -> "Linha Diamante"
                    "Linha Esmeralda Estacoes" -> "Linha Esmeralda"
                    "Linha Turquesa Estacoes" -> "Linha Turquesa"
                    "Linha Coral Estacoes" -> "Linha Coral"
                    "Linha Safira Estacoes" -> "Linha Safira"
                    "Linha Azul Estacoes" -> "Linha Azul"
                    "Linha Verde Estacoes" -> "Linha Verde"
                    "Linha Vermelha Estacoes" -> "Linha Vermelha"
                    "Linha Amarela Estacoes" -> "Linha Amarela"
                    "Linha Lilás Estacoes" -> "Linha Lilás"
                    "Linha Prata Estacoes" -> "Linha Prata"
                    else -> lineName
                }

                navController.navigate("estacoes_detail/${nomeParaRota}")

                println("Navegando para as estações da linha: $nomeParaRota")
            },
            textSizeFactor = textSizeFactor,
            routePrefix = "Estacoes"

        )
    }
}