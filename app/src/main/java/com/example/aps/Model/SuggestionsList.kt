package com.example.aps.Model

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SuggestionsList(
    suggestions: List<String>,
    onSuggestionSelected: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 200.dp)
            .padding(top = 8.dp)
    ) {
        items(suggestions) { suggestion ->
            Text(
                text = suggestion,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSuggestionSelected(suggestion) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}