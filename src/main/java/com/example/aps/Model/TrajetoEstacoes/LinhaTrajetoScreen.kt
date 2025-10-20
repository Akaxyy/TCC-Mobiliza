package com.example.aps.Model.TrajetoEstacoes // Make sure this is the correct package for your file

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessible
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.unit.offset // Import for Modifier.offset with Dp




@Composable
fun LinhaTrajetoScreen(
    navController: NavHostController,
    // NOVO: Adiciona os parâmetros de acessibilidade
    textSizeFactor: Float,
    onTextSizeChange: (Float) -> Unit, // Mantenha este parâmetro se precisar de um Switch local
    nomeDaLinha: String?
) {
    val linha = todasAsLinhasDeTransporte.find { it.nome == nomeDaLinha }

    if (linha == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Linha '$nomeDaLinha' não encontrada.",
                fontSize = 20.sp * textSizeFactor, // NOVO: Aplica o fator de escala
                fontWeight = FontWeight.Bold,
                color = Color.Red,
                textAlign = TextAlign.Center
            )
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // Header of the Line
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(linha.cor)
                .padding(vertical = 24.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // ... (Icons e Spacers)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = linha.nome,
                fontSize = 28.sp * textSizeFactor, // NOVO: Aplica o fator de escala
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Linha ${linha.numeroLinha}",
                fontSize = 18.sp * textSizeFactor, // NOVO: Aplica o fator de escala
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }

        // --- Station Journey ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            linha.estacoes.forEachIndexed { index, estacao ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 60.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    // Station Marker and Line Segment (MANTIDO)
                    // ...

                    // Station Information (Text)
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .offset(y = 9.dp)
                    ) {
                        Text(
                            text = estacao.nome,
                            fontSize = 16.sp * textSizeFactor, // NOVO: Aplica o fator de escala
                            fontWeight = FontWeight.SemiBold,
                            color = Color.DarkGray
                        )
                        if (estacao.temAcessibilidade) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // ... (Icon)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Acessível",
                                    fontSize = 12.sp * textSizeFactor, // NOVO: Aplica o fator de escala
                                    color = Color.Gray
                                )
                            }
                        }
                        estacao.tempoEstimadoProximaEstacao?.let {
                            Text(
                                text = "Prox. Estação: $it",
                                fontSize = 12.sp * textSizeFactor, // NOVO: Aplica o fator de escala
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}