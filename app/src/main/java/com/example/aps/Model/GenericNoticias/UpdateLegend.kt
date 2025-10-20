package com.example.aps.Model.GenericNoticias

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 🚨 Nota: StatusUpdate e UpdateSection devem estar definidos e importados ou no mesmo pacote.

// --- COMPONENTE: Legenda ---
@Composable
fun UpdateLegend(updates: List<StatusUpdate>, textSizeFactor: Float) {
    // Extrai as cores únicas para a legenda
    val uniqueStatuses = updates.map { it.status }.distinct()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            uniqueStatuses.forEach { status ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Círculo de cor
                    Box(
                        modifier = Modifier
                            .size(size = 10.dp)
                            .background(status.color, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(width = 8.dp))
                    Text(
                        text = status.description,
                        fontSize = 14.sp * textSizeFactor,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}


// --- COMPONENTE: Cartão de Atualizações por Seção (Hoje/Ontem) ---
@Composable
fun UpdatesCard(section: UpdateSection, textSizeFactor: Float) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Título da Seção (Hoje/Ontem)
            Text(
                text = section.title,
                fontSize = 20.sp * textSizeFactor,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Lista de Atualizações
            section.updates.forEach { update ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Hora e Descrição
                    Text(
                        // 🚨 CORREÇÃO APLICADA: Usando 'formattedTime' (HH:mm) em vez de 'timestamp' (ISO 8601)
                        text = "${update.formattedTime} - ${update.description}",
                        fontSize = 16.sp * textSizeFactor,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.weight(1f)) // Empurra o círculo para o final

                    // Círculo de status
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(update.status.color, CircleShape)
                    )
                }
            }
        }
    }
}