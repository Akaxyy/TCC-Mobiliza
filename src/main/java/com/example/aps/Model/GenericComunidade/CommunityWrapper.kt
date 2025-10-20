package com.example.aps.Model.GenericComunidade

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController

import com.example.aps.Screens.GenericComunidade.CommunityDetailScreen

import com.example.aps.Model.GenericComunidade.CommunityData
import com.example.aps.Model.GenericComunidade.Post
import java.time.Instant
import java.time.temporal.ChronoUnit



@RequiresApi(Build.VERSION_CODES.O)
val samplePosts = listOf(
    Post(
        id = 1,
        userId = "luiz_macedo_id",
        timestamp = Instant.now().minus(40, ChronoUnit.MINUTES).toString(),
        lineId = "4",
        author = "Luiz Macedo",
        content = "Lorem ipsum is simply dummy text of the printing and typesetting industry."
    ),
    Post(
        id = 2,
        userId = "nance_id",
        timestamp = Instant.now().minus(15, ChronoUnit.MINUTES).toString(),
        lineId = "4", // Exemplo para Linha Amarela
        author = "Nance",
        content = "O trem parou na estação por 15 minutos sem explicação. Alguma notícia?"
    ),
    Post(
        id = 3,
        userId = "paulo_id",
        timestamp = Instant.now().minus(5, ChronoUnit.MINUTES).toString(),
        lineId = "3", // Exemplo para Linha Vermelha
        author = "Paulo",
        content = "Vi o maquinista sorrindo hoje! Que bom!"
    )
)
// ------------------------------------------------------------------


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CommunityWrapper(
    navController: NavHostController,
    nomeDaLinha: String,
    textSizeFactor: Float
) {

    val dadosDaLinha = when (nomeDaLinha) {
        "Linha Rubi" -> CommunityData(numeroLinha = "7", nomeLinha = "Linha Rubi", corPrincipal = Color(0xFF9146A3), posts = samplePosts)
        "Linha Diamante" -> CommunityData(numeroLinha = "8", nomeLinha = "Linha Diamante", corPrincipal = Color(0xFF6A6A6A), posts = samplePosts)
        "Linha Esmeralda" -> CommunityData(numeroLinha = "9", nomeLinha = "Linha Esmeralda", corPrincipal = Color(0xFF00AA8C), posts = samplePosts)
        "Linha Turquesa" -> CommunityData(numeroLinha = "10", nomeLinha = "Linha Turquesa", corPrincipal = Color(0xFF00B4B4), posts = samplePosts)
        "Linha Coral" -> CommunityData(numeroLinha = "11", nomeLinha = "Linha Coral", corPrincipal = Color(0xFFE22134), posts = samplePosts)
        "Linha Safira" -> CommunityData(numeroLinha = "12", nomeLinha = "Linha Safira", corPrincipal = Color(0xFF004396), posts = samplePosts)
        "Linha Jade" -> CommunityData(numeroLinha = "13", nomeLinha = "Linha Jade", corPrincipal = Color(0xFF009F6D), posts = samplePosts)

        "Linha Azul" -> CommunityData(numeroLinha = "1", nomeLinha = "Linha Azul", corPrincipal = Color(0xFF00589F), posts = samplePosts)
        "Linha Verde" -> CommunityData(numeroLinha = "2", nomeLinha = "Linha Verde", corPrincipal = Color(0xFF008060), posts = samplePosts)
        "Linha Vermelha" -> CommunityData(numeroLinha = "3", nomeLinha = "Linha Vermelha", corPrincipal = Color(0xFFEE3124), posts = samplePosts)
        "Linha Amarela" -> CommunityData(numeroLinha = "4", nomeLinha = "Linha Amarela", corPrincipal = Color(0xFFFFCC00), posts = samplePosts)
        "Linha Lilás" -> CommunityData(numeroLinha = "5", nomeLinha = "Linha Lilás", corPrincipal = Color(0xFF9C29B2), posts = samplePosts)
        "Linha Prata" -> CommunityData(numeroLinha = "15", nomeLinha = "Linha Prata", corPrincipal = Color(0xFFC0C0C0), posts = samplePosts)

        else -> null
    }

    if (dadosDaLinha != null) {
        CommunityDetailScreen(
            nomeDaLinha = dadosDaLinha.nomeLinha,
            numeroLinha = dadosDaLinha.numeroLinha,
            corLinha = dadosDaLinha.corPrincipal,
            textSizeFactor = textSizeFactor
        )
    } else {
        Text("Comunidade da Linha $nomeDaLinha não encontrada ou não suportada.")
    }
}