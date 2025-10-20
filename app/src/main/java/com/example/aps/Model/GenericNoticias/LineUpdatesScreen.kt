package com.example.aps.Model.GenericNoticias

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LineUpdatesScreen(
    lineId: Int,
    lineName: String,
    corPrincipal: Color, // Cor da linha (passada do EstacoesWrapper)
    textSizeFactor: Float,

    viewModel: LineUpdateViewModel
) {
    val state by viewModel.state.collectAsState()


    LaunchedEffect(lineId) {
        viewModel.fetchUpdates(lineId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(corPrincipal)
            .padding(top = 16.dp)
    ) {

        Text(
            text = "Atualizações - $lineName",
            color = Color.White,
            fontSize = 24.sp * textSizeFactor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 16.dp)
        )


        when {
            state.isLoading -> {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
            state.error != null -> {
                Text(
                    text = state.error!!,
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {

                    item {
                        UpdateLegend(
                            updates = state.legend.map { statusType ->

                                StatusUpdate(
                                    timestamp = "",
                                    formattedTime = "",
                                    description = statusType.description,
                                    status = statusType
                                )
                            },
                            textSizeFactor = textSizeFactor
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }


                    items(state.updatesByDay) { section ->
                        UpdatesCard(section = section, textSizeFactor = textSizeFactor)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}