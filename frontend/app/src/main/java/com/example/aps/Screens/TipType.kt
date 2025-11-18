package com.example.aps.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

// Cor de destaque usada em ícones e elementos visuais
private val HighlightColor = Color(0xFF0057B8)

// ----------------------------
// Estruturas de dados
// ----------------------------

// Representa uma dica individual
data class TipItem(
    val point: String
)

// Representa uma categoria de dicas com título, ícone e lista de dicas
data class TipCategory(
    val title: String,
    val icon: ImageVector? = null,
    val tips: List<TipItem>
)

// ----------------------------
// Dados de exemplo para exibição
// ----------------------------
val tipCategories = listOf(
    TipCategory(
        title = "Como usar melhor as linhas e estações",
        icon = Icons.Default.Lightbulb,
        tips = listOf(
            TipItem("Evite horários de pico (das 6h às 9h e das 17h às 20h), caso possível."),
            TipItem("Em estações grandes (como Luz ou Brás), chegue com antecedência para localizar a plataforma correta."),
            TipItem("Use as placas e mapas de linha nas estações para se orientar melhor.")
        )
    ),
    TipCategory(
        title = "Acessibilidade nas estações",
        icon = Icons.Default.Info,
        tips = listOf(
            TipItem("Estações adaptadas possuem elevadores, piso tátil e avisos sonoros para pessoas com deficiência."),
            TipItem("Peça ajuda aos funcionários nas bilheterias ou guichês, se necessário."),
            TipItem("Cães-guia são permitidos em todas as estações.")
        )
    ),
    TipCategory(
        title = "Planejamento de rota",
        icon = Icons.Default.Lightbulb,
        tips = listOf(
            TipItem("Consulte o mapa da rede e verifique possíveis transferências entre linhas."),
            TipItem("Confira no site ou aplicativo oficial se há obras programadas na linha.")
        )
    ),
    TipCategory(
        title = "Segurança e conduta",
        icon = Icons.Default.Info,
        tips = listOf(
            TipItem("Mantenha seus pertences à frente do corpo em locais movimentados."),
            TipItem("Não ultrapasse a faixa amarela nas plataformas."),
            TipItem("Em caso de emergência, use os intercomunicadores ou acione a equipe de segurança."),
            TipItem("Funcionários oficiais usam uniforme e crachá de identificação.")
        )
    ),
    TipCategory(
        title = "Objetos perdidos",
        icon = Icons.Default.Lightbulb,
        tips = listOf(
            TipItem("Entre em contato com a Central de Achados e Perdidos da linha utilizada."),
            TipItem("Anote linha, horário e estação para facilitar a busca do item.")
        )
    )
)

// ----------------------------
// Composable: Item individual de dica
// ----------------------------
@Composable
fun TipItemRow(tip: TipItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Ícone de marcador para representar a dica
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = HighlightColor,
            modifier = Modifier
                .size(16.dp)
                .padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))

        // Texto da dica
        Text(
            text = tip.point,
            fontSize = 15.sp,
            color = Color.DarkGray,
            lineHeight = 22.sp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// ----------------------------
// Composable: Tela principal de Dicas e Suporte
// ----------------------------
@Composable
fun DicasSuporteTecnicoScreen(navController: NavHostController) {
    val screenBackgroundColor = Color(0xFFF5F5F5)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(screenBackgroundColor)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Cabeçalho da tela
        Text(
            text = "Dicas de Uso e Suporte",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 32.dp, bottom = 24.dp)
        )

        // Lista de categorias de dicas
        tipCategories.forEach { category ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 20.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // Cabeçalho do card (ícone + título)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        // Ícone principal da categoria
                        category.icon?.let {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(
                                        HighlightColor.copy(alpha = 0.1f),
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = it,
                                    contentDescription = null,
                                    tint = HighlightColor,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                        }

                        // Título da categoria
                        Text(
                            text = category.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black
                        )
                    }

                    // Linha divisória entre título e conteúdo
                    Divider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Lista de dicas da categoria
                    Column(modifier = Modifier.padding(horizontal = 4.dp)) {
                        category.tips.forEach { tip ->
                            TipItemRow(tip = tip)
                        }
                    }
                }
            }
        }

        // Espaçamento final para melhor estética ao rolar
        Spacer(modifier = Modifier.height(32.dp))
    }
}

// ----------------------------
// Preview da tela de dicas
// ----------------------------
@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5)
@Composable
fun DicasSuporteTecnicoScreenPreview() {
    val navController = rememberNavController()
    DicasSuporteTecnicoScreen(navController = navController)
}
