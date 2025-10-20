package com.example.aps.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.aps.R

@Composable
fun ComunidadeScreen(
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
        Image(
            painter = painterResource(R.drawable.comunidade_icon),
            contentDescription = "Ícone da Comunidade",
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Fique por dentro das conversas e\n novidades das estações pelas pessoas.\nParticipe da comunidade!",
            textAlign = TextAlign.Center,
            fontSize = 16.sp * textSizeFactor,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        MetroLinesInterface(
            onLineClick = { fullLineName ->

                val lineNameOnly = fullLineName.replace(" comunidade", "").trim()

                navController.navigate("community_detail/$lineNameOnly")

            },
            textSizeFactor = textSizeFactor,
            routePrefix = "comunidade"
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}