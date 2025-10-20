package com.example.aps.Model.GenericScreenEstacoes

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ============================================================
// Componente principal: Exibição de estações de uma linha
// ============================================================

@Composable
fun LineEstacoesScreen(
    data: EstacaoData,            // Dados completos da linha (número, nome, cor e lista de estações)
    textSizeFactor: Float         // Fator de acessibilidade para redimensionamento de texto
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0)) // Fundo neutro para destacar os elementos principais
    ) {
        // Cabeçalho da tela com identificação da linha
        EstacoesHeader(
            numeroLinha = data.numeroLinha,
            nomeLinha = data.nomeLinha,
            corPrincipal = data.corPrincipal,
            textSizeFactor = textSizeFactor
        )

        // Lista de estações exibida de forma rolável
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            itemsIndexed(data.estacoes) { index, estacao ->
                val isFirst = index == 0
                val isLast = index == data.estacoes.lastIndex

                EstacaoItem(
                    estacao = estacao,
                    corLinha = data.corPrincipal,
                    isFirst = isFirst,
                    isLast = isLast,
                    textSizeFactor = textSizeFactor
                )
            }
        }
    }
}

// ============================================================
// Cabeçalho de seção: exibe o número e o nome da linha
// ============================================================

@Composable
fun EstacoesHeader(
    numeroLinha: String,
    nomeLinha: String,
    corPrincipal: Color,
    textSizeFactor: Float
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(corPrincipal)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ícone circular com o número da linha
        Box(
            modifier = Modifier
                .size(30.dp)
                .background(Color.White, shape = MaterialTheme.shapes.extraSmall),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = numeroLinha,
                fontSize = 16.sp * textSizeFactor,
                fontWeight = FontWeight.Bold,
                color = corPrincipal
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Nome completo da linha
        Text(
            text = "Estações - $nomeLinha",
            fontSize = 20.sp * textSizeFactor,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

// ============================================================
// Item individual de estação com suporte a baldeações
// ============================================================

@Composable
fun EstacaoItem(
    estacao: Estacao,
    corLinha: Color,
    isFirst: Boolean,
    isLast: Boolean,
    textSizeFactor: Float
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Coluna lateral com a linha vertical e o marcador da estação
        Column(
            modifier = Modifier
                .width(30.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Linha superior
            if (!isFirst) {
                LinhaVertical(cor = corLinha, isPontilhada = false, modifier = Modifier.weight(1f))
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            // Marcador da estação (círculo)
            val circleSize =
                if (isFirst || isLast || estacao.linhasBaldeacao.isNotEmpty()) 12.dp else 8.dp
            Canvas(modifier = Modifier.size(circleSize)) {
                drawCircle(color = corLinha)
            }

            // Linha inferior
            if (!isLast) {
                LinhaVertical(cor = corLinha, isPontilhada = false, modifier = Modifier.weight(1f))
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Conteúdo textual e indicadores de baldeação
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Nome da estação
            Text(
                text = estacao.nome,
                fontSize = 18.sp * textSizeFactor,
                fontWeight = if (isFirst || isLast) FontWeight.ExtraBold else FontWeight.Medium,
                color = if (isFirst || isLast) Color.Black else Color.DarkGray,
                modifier = Modifier.weight(1f, fill = false)
            )

            // Indicadores visuais de baldeação (linhas conectadas)
            if (estacao.linhasBaldeacao.isNotEmpty()) {
                Spacer(Modifier.width(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.weight(1f)
                ) {
                    estacao.linhasBaldeacao.forEach { linha ->
                        Box(
                            modifier = Modifier
                                .size(20.dp * textSizeFactor)
                                .background(linha.cor, shape = MaterialTheme.shapes.extraSmall),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = linha.numeroLinha,
                                fontSize = 11.sp * textSizeFactor,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                        Spacer(Modifier.width(4.dp))
                    }
                }
            }
        }
    }
}

// ============================================================
// Desenho da linha vertical que conecta as estações
// ============================================================

@Composable
fun LinhaVertical(cor: Color, isPontilhada: Boolean, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxWidth()) {
        val x = size.width / 2f
        val startY = 0f
        val endY = size.height

        if (isPontilhada) {
            drawLine(
                color = cor,
                start = Offset(x, startY),
                end = Offset(x, endY),
                strokeWidth = 2.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            )
        } else {
            drawLine(
                color = cor,
                start = Offset(x, startY),
                end = Offset(x, endY),
                strokeWidth = 2.dp.toPx()
            )
        }
    }
}
