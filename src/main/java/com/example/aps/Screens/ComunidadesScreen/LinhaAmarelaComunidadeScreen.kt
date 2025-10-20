package com.example.aps.Screens.Comunidades

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aps.R
import coil.compose.AsyncImage // Importação do Coil para carregar a URL


// Modelo de dados (Mock)
data class Post(
    val id: Int,
    val user: String,
    val time: String,
    val content: String,
    val userImage: Int
)



// Cor principal da Linha Amarela
val LineYellow = Color(0xFFEBC100)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinhaAmarelaComunidadeScreen(
    navController: NavController,
    textSizeFactor: Float,
    onTextSizeChange: (Float) -> Unit,
    userPhotoUrl: String? = null
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Comunidade - Linha Amarela",
                        color = Color.Black,
                        fontSize = 18.sp * textSizeFactor,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {

                    Text(
                        text = "4",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black,
                        modifier = Modifier.padding(start = 16.dp, end = 4.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LineYellow,
                    titleContentColor = Color.Black
                )
            )
        },
        containerColor = Color(0xFFF0F2F5)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            PostInputField(
                textSizeFactor = textSizeFactor,
                userPhotoUrl = userPhotoUrl
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
            }
        }
    }
}

@Composable
fun PostInputField(textSizeFactor: Float, userPhotoUrl: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LineYellow)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- MUDANÇA: Usando AsyncImage (Coil) para a foto do Google ---
        AsyncImage(
            model = userPhotoUrl, // URL da foto do Google (vindo do UserProfile)
            contentDescription = "Foto de Perfil do Google",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.White)
        )
        // -----------------------------------------------------------------

        Spacer(modifier = Modifier.width(12.dp))

        // Campo de Texto Simulado
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .height(40.dp)
                .clickable { /* Abre tela de postagem */ },
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Compartilhe Sua experiência...",
                color = Color.Gray,
                fontSize = 14.sp * textSizeFactor,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))

        // Botão Postar
        Button(
            onClick = { },
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
            modifier = Modifier.height(40.dp)
        ) {
            Text(
                "Postar",
                fontSize = 14.sp * textSizeFactor
            )
        }
    }
}