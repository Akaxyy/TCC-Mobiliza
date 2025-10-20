// src/main/java/com/example/aps/Screens/ViagensScreen.kt
package com.example.aps.Screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.aps.R
import com.example.aps.viewmodel.ViagensViewModel

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

    var origem by remember { mutableStateOf("") }
    var destino by remember { mutableStateOf("") }

    LaunchedEffect(trajetoState) {
        if (trajetoState != null) {
            Log.d("ViagensScreen", "Trajeto recebido: $trajetoState")
        } else {
            Log.d("ViagensScreen", "Trajeto está nulo.")
        }
    }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // input pra digitar de onde vai sair a viagem
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

            // input pra onde o usuário quer ir
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

            // botão pra buscar o trajeto (acho que é aqui que faz a mágica)
            Button(
                onClick = {
                    val origemFormatada = viagensViewModel.formatarNomeEstacao(origem)
                    val destinoFormatado = viagensViewModel.formatarNomeEstacao(destino)
                    viagensViewModel.buscarTrajeto(origemFormatada, destinoFormatado)
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

            Spacer(modifier = Modifier.height(16.dp))

            when {
                error != null -> {
                    // mostra erro se der ruim
                    Text(
                        text = error!!,
                        color = Color.Red,
                        fontSize = 16.sp * textSizeFactor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                trajetoState != null -> {
                    // mostra o trajeto quando acha
                    TrajetoResult(trajetoResponse = trajetoState, textSizeFactor = textSizeFactor)
                }
                else -> {
                    // tela padrão quando não tem trajeto ainda
                    Column(
                        modifier = Modifier.fillMaxSize(),
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
        // campo de texto com ícone e autocompletar
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

        // lista que aparece com as sugestões
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
