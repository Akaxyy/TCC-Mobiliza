package com.example.aps.Screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Star // ⭐️ NOVO ÍCONE: Para o ranking
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aps.ResultColorsTrajeto.getLineColor
import com.example.aps.Model.TrajetoResponse
import com.example.aps.Model.Etapa


@Composable
fun TrajetoResult(trajetoResponse: TrajetoResponse?, textSizeFactor: Float) {
    // É crucial que o modelo TrajetoResponse no seu arquivo Model tenha sido atualizado
    // para incluir o campo 'lineScore' no 'estatisticas'.

    if (trajetoResponse != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Detalhes do Trajeto:",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp * textSizeFactor,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // ✅ Exibição das estatísticas do trajeto
            EstatisticasCard(trajetoResponse = trajetoResponse, textSizeFactor = textSizeFactor)
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(trajetoResponse.caminho) { index, etapa ->

                    val nextLineColor = getNextLineColor(
                        caminho = trajetoResponse.caminho,
                        currentIndex = index
                    )

                    TrajetoStep(
                        etapa = etapa,
                        isLast = index == trajetoResponse.caminho.lastIndex,
                        isFirst = index == 0,
                        nextEtapaLineColor = nextLineColor,
                        textSizeFactor = textSizeFactor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    } else {
        Text(
            text = "Ocorreu um erro ao carregar o trajeto.",
            fontSize = 16.sp * textSizeFactor,
            modifier = Modifier.padding(16.dp)
        )
    }
}

// -----------------------------------------------------
// --- COMPONENTES DE ESTATÍSTICAS ---
// -----------------------------------------------------

@Composable
fun EstatisticasCard(trajetoResponse: TrajetoResponse, textSizeFactor: Float) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Tempo Estimado
            EstatisticaRow(
                icon = Icons.Default.Schedule,
                label = "Tempo Estimado:",
                value = trajetoResponse.estatisticas.tempoEstimadoFormatado,
                textSizeFactor = textSizeFactor
            )
            Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color.LightGray)

            // Número de Estações
            EstatisticaRow(
                icon = Icons.Default.Timeline,
                label = "Estações:",
                value = trajetoResponse.estatisticas.numeroEstacoes.toString(),
                textSizeFactor = textSizeFactor
            )
            Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color.LightGray)

            // Número de Baldeações
            EstatisticaRow(
                icon = Icons.Default.Schedule,
                label = "Baldeações:",
                value = trajetoResponse.estatisticas.numeroBaldeacoes.toString(),
                textSizeFactor = textSizeFactor
            )

            // ⭐️ NOVO: Exibição do Score de Ranking da Linha
            trajetoResponse.estatisticas.lineScore?.let { score ->
                Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color.LightGray)
                EstatisticaRow(
                    icon = Icons.Default.Star, // Usando o ícone de estrela para ranking
                    label = "Score da Linha:",
                    // Formata o score para 2 casas decimais, se o valor não for nulo
                    value = String.format("%.2f / 5.0", score),
                    textSizeFactor = textSizeFactor
                )
            }
        }
    }
}

@Composable
private fun EstatisticaRow(
// ... (O restante da função EstatisticaRow não mudou) ...
    icon: ImageVector,
    label: String,
    value: String,
    textSizeFactor: Float
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFF6200EE),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            fontSize = 15.sp * textSizeFactor,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.width(150.dp)
        )
        Text(
            text = value,
            fontSize = 15.sp * textSizeFactor,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}


// -----------------------------------------------------
// --- FUNÇÕES AUXILIARES (O restante do arquivo não mudou) ---
// -----------------------------------------------------

private fun getNextLineColor(caminho: List<Etapa>, currentIndex: Int): Color {
    return if (currentIndex < caminho.lastIndex) {
        getLineColor(caminho[currentIndex + 1].linha)
    } else {
        getLineColor(caminho[currentIndex].linha)
    }
}

@Composable
private fun TrajetoStep(
    etapa: Etapa,
    isFirst: Boolean,
    isLast: Boolean,
    nextEtapaLineColor: Color,
    textSizeFactor: Float
) {
    val MARKER_SIZE = 12.dp
    val LINE_HEIGHT = 20.dp
    val STROKE_WIDTH = 3.dp

    val dashPathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val currentLineColor = getLineColor(etapa.linha)

            if (!isFirst) {
                Canvas(
                    modifier = Modifier
                        .width(MARKER_SIZE)
                        .height(LINE_HEIGHT)
                ) {
                    drawLine(
                        color = currentLineColor,
                        start = Offset(x = size.width / 2, y = 0f),
                        end = Offset(x = size.width / 2, y = size.height),
                        strokeWidth = STROKE_WIDTH.toPx(),
                        pathEffect = dashPathEffect
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(LINE_HEIGHT))
            }

            Box(
                modifier = Modifier.size(MARKER_SIZE),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = currentLineColor,
                        radius = size.minDimension / 2
                    )
                }
            }

            if (!isLast) {
                Canvas(
                    modifier = Modifier
                        .width(MARKER_SIZE)
                        .height(LINE_HEIGHT)
                ) {
                    drawLine(
                        color = nextEtapaLineColor,
                        start = Offset(x = size.width / 2, y = 0f),
                        end = Offset(x = size.width / 2, y = size.height),
                        strokeWidth = STROKE_WIDTH.toPx(),
                        pathEffect = dashPathEffect
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = etapa.estacao,
            fontSize = 16.sp * textSizeFactor,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(top = 18.dp)
        )
    }
}