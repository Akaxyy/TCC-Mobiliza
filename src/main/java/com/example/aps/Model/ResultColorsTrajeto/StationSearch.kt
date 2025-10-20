import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Para o preview, se você for usá-lo:
// import androidx.compose.runtime.remember

@Composable
fun StationSearch(
    label: String, // Ex: "Estação de Origem" ou "Estação de Destino"
    allStations: List<String>,
    onStationSelected: (String) -> Unit, // Callback quando uma estação for selecionada
    textSizeFactor: Float
) {
    // 1. Estado do texto digitado pelo usuário
    var searchText by remember { mutableStateOf("") }
    // 2. Estado do foco (para saber se deve mostrar a lista)
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    // 3. Lista de sugestões filtradas
    val filteredStations = remember(searchText) {
        if (searchText.isBlank()) {
            emptyList()
        } else {
            // Usa 'startsWith' para sugestões mais relevantes, ou 'contains' se preferir mais resultados
            allStations.filter {
                it.contains(searchText, ignoreCase = true)
            }.take(5) // Limita a 5 sugestões
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        // --- CAMPO DE TEXTO PRINCIPAL ---
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text(label, fontSize = 16.sp * textSizeFactor) },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            singleLine = true // Recomendado para campos de busca
        )

        // --- LISTA DE SUGESTÕES (Aparece SOMENTE se houver sugestões) ---
        if (filteredStations.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    // Altura máxima para evitar que a lista ocupe a tela toda
                    .heightIn(max = 200.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(filteredStations) { stationName ->
                        Text(
                            text = stationName,
                            fontSize = 16.sp * textSizeFactor,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // 1. Define o texto do campo como a estação selecionada
                                    searchText = stationName
                                    // 2. Chama o callback para a tela principal usar o valor
                                    onStationSelected(stationName)
                                    // 3. Remove o foco para fechar o teclado e esconder a lista
                                    focusManager.clearFocus()
                                }
                                .padding(12.dp)
                        )
                        Divider()
                    }
                }
            }
        }
    }
}