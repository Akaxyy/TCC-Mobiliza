package com.example.aps.Screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.aps.Model.Etapa
import com.example.aps.R
import com.example.aps.viewmodel.ViagensViewModel
import com.example.aps.Model.TrajetoResponse
import com.example.aps.Model.GTFS.TrainArrival


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViagensScreen(
    navController: NavController,
    textSizeFactor: Float,
    onTextSizeChange: (Float) -> Unit,
    viagensViewModel: ViagensViewModel = viewModel()
) {
    val trajetoState by viagensViewModel.trajetoState
    val isLoading by viagensViewModel.isLoading
    val error by viagensViewModel.error
    // Observa o estado da previsão de chegada (ETA)
    val trainArrival by viagensViewModel.trainArrival.collectAsState()

    var origem by remember { mutableStateOf("") }
    var destino by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Pesquisar Viagem",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp * textSizeFactor
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(top = 0.dp, bottom = 16.dp)
        ) {

            // --- ITEM 1: Input Fields e Botão de Busca ---
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    StationTextFieldWithSuggestions(
                        value = origem,
                        onValueChange = { origem = it },
                        onSuggestionSelected = { origem = it },
                        placeholder = "Digite o nome da estação origem",
                        iconResId = R.drawable.origem,
                        viagensViewModel = viagensViewModel,
                        textSizeFactor = textSizeFactor
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    StationTextFieldWithSuggestions(
                        value = destino,
                        onValueChange = { destino = it },
                        onSuggestionSelected = { destino = it },
                        placeholder = "Digite o nome da estação destino",
                        iconResId = R.drawable.destino,
                        viagensViewModel = viagensViewModel,
                        textSizeFactor = textSizeFactor
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val origemFormatada = viagensViewModel.formatarNomeEstacao(origem)
                            val destinoFormatado = viagensViewModel.formatarNomeEstacao(destino)

                            // 1. Busca o trajeto e as estatísticas de score
                            viagensViewModel.buscarTrajeto(origemFormatada, destinoFormatado)

                            // 2. Inicia a busca da previsão de chegada (ETA) para a ORIGEM
                            viagensViewModel.fetchTrainArrivalByStationName(origemFormatada)

                        },
                        enabled = !isLoading && origem.isNotBlank() && destino.isNotBlank(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6200EE),
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                "Buscar Trajeto",
                                fontSize = 18.sp * textSizeFactor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }


            // --- ITEM 2: Resultados (Score, GTFS, Trajeto, Erro ou Placeholder) ---

            if (error != null) {
                item {
                    Text(
                        text = error!!,
                        color = Color.Red,
                        fontSize = 16.sp * textSizeFactor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else if (trajetoState != null) {

                // Agrupamento para os cartões de informação
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // 1. Mostrar o Score da Linha Principal
                        LineScoreCard(
                            trajeto = trajetoState,
                            textSizeFactor = textSizeFactor
                        )

                        // 2. Mostrar o Cartão de Previsão GTFS (ETA)
                        TrainArrivalCard(
                            trainArrival = trainArrival,
                            textSizeFactor = textSizeFactor
                        )
                    }
                }

                // 3. Mostrar os detalhes e a timeline do trajeto (Seu componente TrajetoResult)
                item {
                    // 🎉 INTEGRAÇÃO DO SEU TrajetoResult
                    TrajetoResult(
                        trajetoResponse = trajetoState,
                        textSizeFactor = textSizeFactor
                    )
                }

            } else if (origem.isBlank() && destino.isBlank()) {
                // Tela padrão quando ainda não há busca
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillParentMaxHeight(0.8f)
                            .padding(vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_mulher_esperando),
                            contentDescription = "Pessoas esperando trajeto",
                            modifier = Modifier
                                .size(250.dp)
                                .padding(bottom = 16.dp)
                        )
                        Text(
                            "Estamos esperando seu trajeto",
                            fontSize = 18.sp * textSizeFactor,
                            color = Color.DarkGray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

// ------------------------------------------------------------------------------------
// ⭐️ COMPOSABLE PARA EXIBIR A PREVISÃO GTFS (TrainArrivalCard) - MANTIDO DA VERSÃO ANTERIOR
// ------------------------------------------------------------------------------------

@Composable
fun TrainArrivalCard(
    trainArrival: TrainArrival?,
    textSizeFactor: Float = 1.0f
) {
    if (trainArrival == null) return

    val isPredictionAvailable = trainArrival.arrivalTimeInMinutes != null && trainArrival.arrivalTimeInMinutes!! >= 0
    val corStatus = when {
        trainArrival.isDelayed -> Color(0xFFF44336) // Vermelho: Atraso
        isPredictionAvailable && trainArrival.arrivalTimeInMinutes!! <= 5 -> Color(0xFF4CAF50) // Verde: Próximo
        isPredictionAvailable -> Color(0xFFFFC107) // Amarelo: Tempo Normal
        else -> Color(0xFFFFA500) // Laranja: Indisponível/Buscando
    }
    val iconStatus = if (isPredictionAvailable) Icons.Default.CheckCircle else Icons.Default.Warning

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "Previsão de Chegada (Origem)",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp * textSizeFactor,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Divider(color = Color.LightGray)

            Spacer(modifier = Modifier.height(8.dp))

            // 1. Estação e Linha
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // ASSUMINDO QUE R.drawable.origem EXISTE
                Icon(
                    painter = painterResource(id = R.drawable.origem),
                    contentDescription = "Origem",
                    tint = Color.DarkGray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${trainArrival.stationName} (Linha ${trainArrival.lineId.replace("L0", "").replace("L", "")})",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp * textSizeFactor
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 2. Tempo de Chegada
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = iconStatus,
                    contentDescription = "Tempo",
                    tint = corStatus,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                val arrivalText = if (isPredictionAvailable) {
                    if (trainArrival.arrivalTimeInMinutes!! == 0) {
                        "Próximo trem Chegando!"
                    } else {
                        "Próximo trem em média: ${trainArrival.arrivalTimeInMinutes} min"
                    }
                } else {
                    "Status: ${trainArrival.status}"
                }
                Text(
                    text = arrivalText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp * textSizeFactor,
                    color = corStatus
                )

                // ⭐️ INSERÇÃO DO ÍCONE DE AJUDA
                Spacer(modifier = Modifier.width(4.dp))
                EtaHelpDialog(textSizeFactor = textSizeFactor)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 3. Status/Observação
            if (trainArrival.status.isNotBlank()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Status",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = trainArrival.status,
                        fontSize = 14.sp * textSizeFactor,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}


// ------------------------------------------------------------------------------------
// COMPOSABLE PARA EXIBIR O SCORE DA LINHA (MANTIDO)
// ------------------------------------------------------------------------------------

@Composable
fun LineScoreCard(
    trajeto: TrajetoResponse?,
    textSizeFactor: Float = 1.0f
) {
    val primeiroSegmento = trajeto?.caminho?.firstOrNull()
    val score = trajeto?.estatisticas?.lineScore

    if (primeiroSegmento == null || score == null || score < 0) return

    // Assumindo que seu modelo 'TrajetoResponse.caminho' agora contém objetos com a propriedade 'linha'
    val linhaNome = if (primeiroSegmento.javaClass.simpleName == "Etapa") {
        (primeiroSegmento as? Etapa)?.linha ?: "Linha Principal"
    } else {
        "Linha Principal" // Fallback seguro
    }

    val scoreFormatado = "%.2f".format(score)

    val corScore = when {
        score >= 8.0 -> Color(0xFF4CAF50) // Verde: Ótimo
        score >= 5.0 -> Color(0xFFFFC107) // Amarelo: Bom/Regular
        else -> Color(0xFFF44336) // Vermelho: Ruim
    }


}


// ------------------------------------------------------------------------------------
// DEFINIÇÕES AUXILIARES (StationTextFieldWithSuggestions - MANTIDO)
// ------------------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationTextFieldWithSuggestions(
    value: String,
    onValueChange: (String) -> Unit,
    onSuggestionSelected: (String) -> Unit,
    placeholder: String,
    iconResId: Int,
    viagensViewModel: ViagensViewModel,
    textSizeFactor: Float
) {
    var expanded by remember { mutableStateOf(false) }
    val suggestions = viagensViewModel.filterStations(value)

    LaunchedEffect(suggestions) {
        expanded = suggestions.isNotEmpty() && value.isNotBlank()
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
                expanded = true
            },
            placeholder = { Text(placeholder, fontSize = 16.sp * textSizeFactor) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = placeholder,
                    modifier = Modifier.size(24.dp)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = MaterialTheme.colorScheme.primary
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            suggestions.forEach { suggestion ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = suggestion,
                            fontSize = 16.sp * textSizeFactor
                        )
                    },
                    onClick = {
                        onSuggestionSelected(suggestion)
                        expanded = false
                    },
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}