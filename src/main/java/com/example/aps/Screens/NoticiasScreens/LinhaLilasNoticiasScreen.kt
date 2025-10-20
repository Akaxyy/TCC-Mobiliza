package com.example.aps.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.aps.Model.DailyStatus
import com.example.aps.Model.StatusColor
import com.example.aps.Model.StatusEntry
import com.example.aps.R

@Composable
fun LinhaLilasNoticiasScreen(
    navController: NavHostController,
    // NOVO: Adiciona os parâmetros de acessibilidade
    textSizeFactor: Float,
    onTextSizeChange: (Float) -> Unit
) {
    // Dados de exemplo, substituídos pela API no futuro
    val statusData = listOf(
        DailyStatus(
            day = "Hoje",
            entries = listOf(
                StatusEntry(time = "05:34", description = "Operação Normal", statusColor = StatusColor.GREEN),
                StatusEntry(time = "09:41", description = "Operação Normal", statusColor = StatusColor.GREEN),
                StatusEntry(time = "14:56", description = "Atividade Programada", statusColor = StatusColor.YELLOW),
                StatusEntry(time = "16:34", description = "Velocidade Reduzida", statusColor = StatusColor.YELLOW),
                StatusEntry(time = "00:02", description = "Operação Encerrada", statusColor = StatusColor.RED)
            )
        ),
        DailyStatus(
            day = "Ontem",
            entries = listOf(
                StatusEntry(time = "07:34", description = "Operação Normal", statusColor = StatusColor.GREEN),
                StatusEntry(time = "08:41", description = "Velocidade Reduzida", statusColor = StatusColor.YELLOW),
                StatusEntry(time = "09:33", description = "Operação Normal", statusColor = StatusColor.GREEN),
                StatusEntry(time = "14:55", description = "Operação Normal", statusColor = StatusColor.GREEN),
                StatusEntry(time = "00:04", description = "Operação Encerrada", statusColor = StatusColor.RED)
            )
        )
        // Você pode adicionar mais dias aqui
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF6A0DAD)) // Cor de fundo da Linha Lilás
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "5 Atualizações - Linha Lilás",
            fontSize = 24.sp * textSizeFactor, // NOVO: Aplica o fator de escala
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(statusData) { dailyStatus ->
                StatusCard(dailyStatus, textSizeFactor) // NOVO: Passa o fator de escala
            }
        }
    }
}


@Composable
fun StatusCard(dailyStatus: DailyStatus, textSizeFactor: Float) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = dailyStatus.day,
                fontSize = 16.sp * textSizeFactor, // NOVO: Aplica o fator de escala
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 2.dp)
            )

            // ✅ Legenda mais compacta
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatusLegendItem(color = Color.Green, text = "Normal", textSizeFactor = textSizeFactor)
                StatusLegendItem(color = Color.Yellow, text = "Ajustes", textSizeFactor = textSizeFactor)
                StatusLegendItem(color = Color.Red, text = "Suspenso", textSizeFactor = textSizeFactor)
            }

            Spacer(modifier = Modifier.height(4.dp))

            dailyStatus.entries.forEach { entry ->
                StatusEntryRow(entry, textSizeFactor) // NOVO: Passa o fator de escala
            }
        }
    }
}

@Composable
fun StatusLegendItem(color: Color, text: String, textSizeFactor: Float) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color = color, shape = RoundedCornerShape(50))
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text, fontSize = 10.sp * textSizeFactor, color = Color.Gray)
    }
}


@Composable
fun StatusEntryRow(entry: StatusEntry, textSizeFactor: Float) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val color = when (entry.statusColor) {
            StatusColor.GREEN -> Color.Green
            StatusColor.YELLOW -> Color.Yellow
            StatusColor.RED -> Color.Red
        }
        Text(
            text = entry.time,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp * textSizeFactor, // NOVO: Aplica o fator de escala
            modifier = Modifier.width(60.dp)
        )
        Text(
            text = entry.description,
            fontSize = 14.sp * textSizeFactor, // NOVO: Aplica o fator de escala
            modifier = Modifier.weight(1f)
        )
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color = color, shape = RoundedCornerShape(50))
        )
    }
}