package com.example.aps.Model.GenericNoticias

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.aps.Model.GenericNoticias.LineUpdatesScreen



class LineUpdateViewModelFactory(private val repository: LineUpdateRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LineUpdateViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LineUpdateViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
// -------------------------------------------------------------------


@Composable
fun LineUpdatesWrapper(
    lineId: Int,
    lineName: String,
    corPrincipalHex: String,
    textSizeFactor: Float
) {
    // Inicialização do Retrofit/API (Lógica de dados)
    val retrofit = remember {
        Retrofit.Builder()
            .baseUrl("https://www.diretodostrens.com.br/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val trainApi = remember { retrofit.create(TrainApi::class.java) }
    val lineUpdateRepository = remember { LineUpdateRepository(trainApi) }

    // Converte a cor HEX String de volta para Color
    val corPrincipal = try {
        Color(android.graphics.Color.parseColor(corPrincipalHex))
    } catch (e: IllegalArgumentException) {
        Color.Black
    }

    // Instancia o ViewModel usando a Factory
    val lineUpdateViewModel: LineUpdateViewModel = viewModel(
        factory = LineUpdateViewModelFactory(lineUpdateRepository)
    )

    // Chama a tela final de exibição, passando o ViewModel
    LineUpdatesScreen(
        lineId = lineId,
        lineName = lineName,
        corPrincipal = corPrincipal,
        textSizeFactor = textSizeFactor,
        viewModel = lineUpdateViewModel
    )
}