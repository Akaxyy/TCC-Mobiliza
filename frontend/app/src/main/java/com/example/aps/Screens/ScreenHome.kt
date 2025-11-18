package com.example.aps.Screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aps.R
import coil.compose.AsyncImage

// ----------------------------
// Tela principal (Home)
// ----------------------------

@Composable
fun HomeScreen(
    navController: NavController,
    textSizeFactor: Float,
    onTextSizeChange: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            // Define a cor de fundo conforme o tema atual (claro/escuro)
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 30.dp, vertical = 20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Título principal da tela
        Text(
            text = "Planeje sua viagem",
            fontSize = 20.sp * textSizeFactor,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            textAlign = TextAlign.Center
        )

        // Barra de pesquisa (campo interativo para navegar até a tela de Viagens)
        SearchBar(navController, textSizeFactor)

        // ----------------------------
        // Card: Acesso ao mapa completo
        // ----------------------------
        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clickable { navController.navigate("mapa_screen") },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(12.dp)
            ) {
                AsyncImage(
                    model = R.drawable.mapasp,
                    contentDescription = "Mapa",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(12.dp))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Explore o Mapa Completo",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp * textSizeFactor,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 8.dp, end = 6.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Clique aqui para visualizar todas as linhas de trem e metrô da região.",
                        fontSize = 14.sp * textSizeFactor,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(end = 5.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // ----------------------------
        // Card: Visualização das estações
        // ----------------------------
        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clickable { navController.navigate("estacoes_screen") },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(12.dp)
            ) {
                AsyncImage(
                    model = R.drawable.imagescreenhome2,
                    contentDescription = "Estações",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(12.dp))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Estações da Linha",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp * textSizeFactor,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 8.dp, end = 6.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Estações do início ao fim da sua linha favorita.",
                        fontSize = 14.sp * textSizeFactor,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(end = 5.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // ----------------------------
        // Card: Dicas e suporte técnico
        // ----------------------------
        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clickable { navController.navigate("suporte_screen") },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(12.dp)
            ) {
                AsyncImage(
                    model = R.drawable.image,
                    contentDescription = "Suporte",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(12.dp))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Dicas e Suporte",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp * textSizeFactor,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 8.dp, end = 6.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Dúvidas, orientações e contatos importantes para sua viagem.",
                        fontSize = 14.sp * textSizeFactor,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(end = 5.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// ----------------------------
// Composable auxiliar: SearchBar
// ----------------------------
@Composable
fun SearchBar(navController: NavController, textSizeFactor: Float) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clickable {
                Toast.makeText(context, "Navegando para Viagens...", Toast.LENGTH_SHORT).show()
                navController.navigate("viagens_screen")
            }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = R.drawable.loupe,
                contentDescription = "Pesquisar",
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Para onde vamos?",
                fontSize = 16.sp * textSizeFactor,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
