package com.example.aps.Model

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aps.R // Certifique-se de que este é o seu pacote R real

// --- Simplified Data Class for Metro Line ---
data class MetroLine(
    val numberIconResId: Int,
    val name: String,
    val onClickAction: () -> Unit
)

@Composable
fun MetroLineItem(line: MetroLine, textSizeFactor: Float) {
    Row(
        // Design aprimorado para a área de clique
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = line.onClickAction)
            .padding(vertical = 12.dp, horizontal = 16.dp), // Aumentado para 12.dp
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = painterResource(id = line.numberIconResId),
            contentDescription = "Linha ${line.name.split(" ")[0]}",
            modifier = Modifier.size(32.dp), // Aumentado o ícone para 32.dp
            colorFilter = null,
        )
        Spacer(modifier = Modifier.width(16.dp)) // Aumentado o espaçamento
        Text(
            text = line.name,
            fontSize = 17.sp * textSizeFactor, // Aumentado a fonte para 17.sp
            color = MaterialTheme.colorScheme.onSurface // Usando a cor do tema
        )
    }
}
// --- Main Composable for the Metro Interface (USADO NA TELA COMUNIDADE) ---
@Composable
fun MetroLinesInterface(
    onLineClick: (String) -> Unit,
    textSizeFactor: Float,
    routePrefix: String // <<< IMPORTANTE: Este prefixo deve ser "comunidade"
) {

    // --- Dados das Linhas de Trem (CPTM) ---
    val cptmLines = listOf(
        // Gerando o nome da linha + o prefixo (ex: "Linha Rubi comunidade")
        MetroLine(R.drawable.linha_rubi_metro, "Linha 7 - Rubi (Luz - Jundiaí)") { onLineClick("Linha Rubi $routePrefix") },
        MetroLine(R.drawable.linha_diamante_metro, "Linha 8 - Diamante (Júlio Prestes - Amador Bueno)") { onLineClick("Linha Diamante $routePrefix") },
        MetroLine(R.drawable.linha_esmeralda_metro, "Linha 9 - Esmeralda (Osasco - Grajaú)") { onLineClick("Linha Esmeralda $routePrefix") },
        MetroLine(R.drawable.linha_turquesa_trem, "Linha 10 - Turquesa (Brás - Rio Grande da Serra)") { onLineClick("Linha Turquesa $routePrefix") },
        MetroLine(R.drawable.linha_coral_metro, "Linha 11 - Coral (Luz - Estudantes)") { onLineClick("Linha Coral $routePrefix") },
        MetroLine(R.drawable.linha_safira_metro, "Linha 12 - Safira (Brás - Calmon Viana)") { onLineClick("Linha Safira $routePrefix") }
    )

    // --- Dados das Linhas de Metrô (Operado pelo Metrô de São Paulo) ---
    val metroLines = listOf(
        MetroLine(R.drawable.linha_azul_metro, "Linha 1 - Azul (Tucuruvi - Jabaquara)") { onLineClick("Linha Azul $routePrefix") },
        MetroLine(R.drawable.linha_verde_metro, "Linha 2 - Verde (Vila Prudente - Vila Madalena)") { onLineClick("Linha Verde $routePrefix") },
        MetroLine(R.drawable.linha_vermelha_metro, "Linha 3 - Vermelha (Corinthians-Itaquera - Palmeiras-Barra Funda)") { onLineClick("Linha Vermelha $routePrefix") },
        MetroLine(R.drawable.linha_amarela_icon, "Linha 4 - Amarela (São Paulo-Morumbi - Luz)") { onLineClick("Linha Amarela $routePrefix") },
        MetroLine(R.drawable.linha_lilas_metro, "Linha 5 - Lilás (Capão Redondo - Chácara Klabin)") { onLineClick("Linha Lilás $routePrefix") },
        MetroLine(R.drawable.linha_prata_metro, "Linha 15 - Prata (Vila Prudente - Jardim Colonial)") { onLineClick("Linha Prata $routePrefix") }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // --- Seção de Trem (CPTM) ---
        Text(
            text = "Trem (CPTM)",
            fontSize = 22.sp * textSizeFactor,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                cptmLines.forEachIndexed { index, line ->
                    MetroLineItem(line = line, textSizeFactor = textSizeFactor)
                    if (index < cptmLines.lastIndex) {
                        Divider(
                            color = MaterialTheme.colorScheme.outlineVariant,
                            thickness = 0.8.dp,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Seção de Metrô (Operado pelo Metrô de São Paulo) ---
        Text(
            text = "Metrô",
            fontSize = 22.sp * textSizeFactor,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                metroLines.forEachIndexed { index, line ->
                    MetroLineItem(line = line, textSizeFactor = textSizeFactor)
                    if (index < metroLines.lastIndex) {
                        Divider(
                            color = MaterialTheme.colorScheme.outlineVariant,
                            thickness = 0.8.dp,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }
}
