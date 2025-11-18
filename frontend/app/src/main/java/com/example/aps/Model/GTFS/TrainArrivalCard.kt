package com.example.aps.Model.GTFS

// Em CommunityDetailScreen.kt (ou outro arquivo de UI)

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TrainArrivalCard(
    arrival: TrainArrival?,
    corLinha: Color,
    textSizeFactor: Float = 1.0f
) {
    if (arrival == null) return

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Título Principal
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Bloco de Cor da Linha
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(corLinha, shape = RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = arrival.lineId,
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Próxima Chegada",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp * textSizeFactor
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Previsão e Status
            if (arrival.arrivalTimeInMinutes != null) {
                // Previsão em minutos
                Text(
                    text = "Chega em: ${arrival.arrivalTimeInMinutes} min",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp * textSizeFactor,
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                // Status quando a previsão em tempo real não está disponível (manutenção, por exemplo)
                Text(
                    text = "Previsão Indisponível",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp * textSizeFactor,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Status/Mensagem
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (arrival.isDelayed) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Alerta",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Text(
                    text = arrival.status,
                    fontSize = 14.sp * textSizeFactor,
                    color = if (arrival.isDelayed) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}