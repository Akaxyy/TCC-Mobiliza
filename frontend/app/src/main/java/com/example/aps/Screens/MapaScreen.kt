package com.example.aps.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.aps.R // Certifique-se de ter o ID da sua imagem no R.drawable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapaScreen(
    navController: NavHostController,
    textSizeFactor: Float,
    onTextSizeChange: (Float) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Mapa da Rede",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp * textSizeFactor,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface) // Fundo claro para o mapa
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // ⭐️ Componente de Imagem com Zoom, Pan e agora Rotação
            ZoomableMapImage(
                imageResId = R.drawable.mapa_rede_metro, // ⚠️ Substitua pelo ID real da sua imagem
                contentDescription = "Mapa da Rede Metroferroviária de São Paulo"
            )
        }
    }
}

// ----------------------------------------------------------------------
// ⭐️ COMPONENTE: ZoomableMapImage (Implementação do Zoom, Pan e Rotação)
// ----------------------------------------------------------------------

@Composable
fun ZoomableMapImage(imageResId: Int, contentDescription: String) {
    // 1. Estados para gerenciar o zoom, o pan (offset)
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    // ⭐️ Novo estado para rotação (0f = retrato, 90f = paisagem)
    var rotation by remember { mutableStateOf(0f) }

    // 2. Estado de transformação que escuta gestos de toque
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        // Aplica o zoom, limitando a escala entre 1x e 3x
        scale = (scale * zoomChange).coerceIn(1f, 3f)

        // Aplica o pan (arrasto)
        offset += offsetChange
    }

    // 3. Imagem
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray) // Cor de fundo do Box
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = contentDescription,
            modifier = Modifier
                .fillMaxSize()
                // ⭐️ Aplica os gestos de zoom/pan
                .transformable(state = state)
                // ⭐️ Aplica as transformações visuais (escala, offset E ROTAÇÃO)
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y,
                    rotationZ = rotation // ⭐️ Aplicando a rotação aqui
                )
        )

        // ⭐️ Botão para alternar a visão (0° / 90°)
        FloatingActionButton(
            onClick = {
                // Alterna entre 0 graus (Vertical/Retrato) e 90 graus (Horizontal/Paisagem)
                rotation = if (rotation == 0f) 90f else 0f
                // Resetar o zoom e o pan ao rotacionar para uma visualização limpa
                scale = 1f
                offset = Offset.Zero
            },
            modifier = Modifier
                .align(Alignment.TopEnd) // Posiciona no canto superior direito
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ) {
            Icon(Icons.Default.RotateRight, contentDescription = "Alternar Orientação do Mapa")
        }

        // Nota informativa para o usuário
        if (scale > 1f) {
            Text(
                "Arraste para mover o mapa.",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 12.sp,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
            )
        }
    }
}