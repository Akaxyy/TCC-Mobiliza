package com.example.aps.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.aps.Model.GenericNoticias.getLineData
import com.example.aps.R
import com.example.aps.Routes.Routes


@Composable
fun NoticiasScreen(
    navController: NavHostController,
    textSizeFactor: Float,
    onTextSizeChange: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = R.drawable.noticias_icon,
            contentDescription = "Ícone de Notícias",
            modifier = Modifier.size(150.dp),
            contentScale = ContentScale.Fit,
            colorFilter = null
        )

        Text(
            text = "Acompanhe as atualizações e status das\n linhas e estações em tempo real.",
            textAlign = TextAlign.Center,
            fontSize = 16.sp * textSizeFactor,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )

        // --- CHAMADA DO METROLINESINTERFACE ATUALIZADA ---
        MetroLinesInterface(
            onLineClick = { lineKey ->
                val lineInfo = getLineData(lineKey)

                if (lineInfo != null) {

                    navController.navigate(
                        Routes.LINE_UPDATES_SCREEN
                            .replace("{lineId}", lineInfo.lineId.toString())
                            .replace("{lineName}", lineInfo.lineName)
                            .replace("{corPrincipalHex}", lineInfo.corPrincipalHex)
                            .replace("{textSizeFactor}", textSizeFactor.toString())
                    )
                } else {
                    println("Aviso: Dados da linha não encontrados para: $lineKey")
                }
            },
            textSizeFactor = textSizeFactor,
            routePrefix = "Noticias"
        )
    }
}