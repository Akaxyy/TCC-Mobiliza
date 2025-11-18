package com.example.aps.Model

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TextSizeController(
    onTextSizeChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentTextSize by remember { mutableStateOf(1.0f) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Tamanho do Texto:")

        Button(onClick = {
            if (currentTextSize > 0.5f) {
                currentTextSize -= 0.1f
                onTextSizeChange(currentTextSize)
            }
        }) {
            Text("-")
        }

        Text(text = "Padrão")

        Button(onClick = {
            if (currentTextSize < 1.5f) {
                currentTextSize += 0.1f
                onTextSizeChange(currentTextSize)
            }
        }) {
            Text("+")
        }
    }
}