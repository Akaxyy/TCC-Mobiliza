package com.example.aps.Screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*

@Composable
fun EtaHelpDialog(textSizeFactor: Float) {
    // Estado para controlar a visibilidade da janela de ajuda
    var showDialog by remember { mutableStateOf(false) }

    // 1. O Ícone de Ajuda (que você colocará ao lado do ETA)
    IconButton(onClick = { showDialog = true }) {
        Icon(
            imageVector = Icons.Default.HelpOutline,
            contentDescription = "Ajuda sobre Previsão de Chegada (ETA)",
            modifier = Modifier.size(20.dp)
        )
    }

    // 2. A Janela de Ajuda (Dialog)
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    text = "Como Calculamos o Tempo de Chegada (ETA) ⏳",
                    fontSize = 18.sp * textSizeFactor,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                EtaExplanationContent(textSizeFactor)
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Entendi", fontWeight = FontWeight.SemiBold)
                }
            }
        )
    }
}

// ----------------------------------------------------


@Composable
fun EtaExplanationContent(textSizeFactor: Float) {
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Nossa Previsão de Chegada (ETA) é uma estimativa inteligente que te ajuda a planejar melhor, usando uma abordagem híbrida de cálculo:",
            fontSize = 15.sp * textSizeFactor
        )

        // --- 1. A BASE DO CÁLCULO (O QUE É FIRME) ---
        Text(
            text = "1. Base no Intervalo Programado (Headway)",
            fontSize = 16.sp * textSizeFactor,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Utilizamos o tempo médio de espera **oficialmente programado** para o próximo trem naquela linha. É o cálculo ideal, assumindo que tudo está funcionando perfeitamente.",
            fontSize = 14.sp * textSizeFactor
        )

        // --- 2. O AJUSTE DE REALIDADE (O QUE É DINÂMICO) ---
        Text(
            text = "2. Ajuste de Realidade e Fator Dinâmico",
            fontSize = 16.sp * textSizeFactor,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Para que a estimativa fique mais próxima do seu dia a dia, aplicamos um **ajuste matemático** que simula as variações comuns da operação (pequenos atrasos, velocidade reduzida por lotação, etc.).",
            fontSize = 14.sp * textSizeFactor
        )

        Divider(modifier = Modifier.padding(vertical = 6.dp))

        // --- 3. TRANSPARÊNCIA SOBRE OS LIMITES ---
        Text(
            text = "⚠️ Transparência Total: O Que Você Precisa Saber",
            fontSize = 16.sp * textSizeFactor,
            fontWeight = FontWeight.Bold
        )

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text("• ", fontWeight = FontWeight.ExtraBold)
                Text(
                    text = "**Não é Tempo Real (GPS):** Não recebemos a localização exata (GPS) dos trens. Nossa previsão é uma **simulação avançada** de alta fidelidade, mas não uma informação exata em tempo real.",
                    fontSize = 14.sp * textSizeFactor
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Text("• ", fontWeight = FontWeight.ExtraBold)
                Text(
                    text = "**Use com Prudência:** Para grandes imprevistos (interrupção total, falha grave), confie sempre no **alerta social** da comunidade (comentários) e nos avisos da estação.",
                    fontSize = 14.sp * textSizeFactor
                )
            }
        }
    }
}