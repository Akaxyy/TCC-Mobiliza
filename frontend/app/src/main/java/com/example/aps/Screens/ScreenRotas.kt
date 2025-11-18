package com.example.aps.Screens

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun ScreenRotas(x0: NavController) {
    RouteScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteScreen() {
    var origin by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }
    val backgroundColor = Color(0xFFE2EFFF)
    val routeColor = Color(0xFF005BA2)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()  // Ocupa a tela toda
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Campo de Orig
            Text(
                text = "Origem",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )

            OutlinedTextField(
                value = origin,
                onValueChange = { origin = it },
                placeholder = { Text("Digite o nome da estação origem") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(8.dp)
            )

            // Campo de Dest
            Text(
                text = "Destino",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )

            OutlinedTextField(
                value = destination,
                onValueChange = { destination = it },
                placeholder = { Text("Digite o nome da estação destino") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(8.dp)
            )

            // Linha div
            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            // Representação do trajeto
            if (origin.isNotEmpty() && destination.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Estação de Origem
                    StationPoint(
                        name = origin,
                        color = routeColor,
                        isFirst = true
                    )

                    // Linha do trajeto
                    VerticalLine(color = routeColor)

                    // Estação de Destino
                    StationPoint(
                        name = destination,
                        color = routeColor,
                        isFirst = false
                    )
                }
            } else {
                Text(
                    text = "Informe origem e destino para ver o trajeto",
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun StationPoint(name: String, color: Color, isFirst: Boolean) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth(0.8f) // ocupar 80% da largura
            .border(
                width = 2.dp,
                color = color,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = name,
            color = Color.Black,
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun VerticalLine(color: Color) {
    Box(
        modifier = Modifier
            .height(40.dp)
            .width(4.dp)
            .background(color)
    )
}