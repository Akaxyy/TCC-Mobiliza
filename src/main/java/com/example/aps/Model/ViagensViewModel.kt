package com.example.aps.viewmodel

import android.util.Log // Adicionado para logging
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aps.API.RetrofitClient
import com.example.aps.API.ApiServiceTrajeto
import com.example.aps.LineRanking.LineRankingItem
import com.example.aps.Model.TrajetoResponse
import com.example.aps.data.repository.LineRankingRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ViagensViewModel(
    private val lineRankingRepository: LineRankingRepository = LineRankingRepository()
) : ViewModel() {

    val trajetoState = mutableStateOf<TrajetoResponse?>(null)
    val isLoading = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)

    private val apiService: ApiServiceTrajeto = RetrofitClient.apiService

    private var lineRankings = listOf<LineRankingItem>()

    private val allStations = listOf(
        // LINHA 1 - AZUL
        "Tucuruvi", "Parada Inglesa", "Jardim São Paulo-Ayrton Senna",
        "Santana", "Carandiru", "Portuguesa-Tietê", "Armênia",
        "Tiradentes", "Luz", "São Bento", "Sé",
        "Japão-Liberdade", "São Joaquim", "Vergueiro", "Paraíso",
        "Ana Rosa", "Vila Mariana", "Santa Cruz", "Praça da Árvore",
        "Saúde-Ultrafarma", "São Judas", "Conceição", "Jabaquara",

        // LINHA 2 - VERDE
        "Vila Madalena", "Sumaré", "Clínicas", "Consolação",
        "Trianon-Masp", "Brigadeiro", "Ana Rosa", "Paraíso",
        "Chácara Klabin", "Tamanduateí", "Vila Prudente", "Sacomã",
        "Alto do Ipiranga", "Santos-Imigrantes",

        // LINHA 3 - VERMELHA
        "Palmeiras-Barra Funda", "Marechal Deodoro", "Santa Cecília",
        "República", "Anhangabaú", "Sé", "Pedro II",
        "Brás", "Bresser-Mooca", "Belém", "Tatuapé",
        "Carrão-Assaí Atacadista", "Penha-Lojas Besni",
        "Vila Matilde", "Guilhermina-Esperança", "Patriarca-Vila Ré",
        "Artur Alvim", "Corinthians-Itaquera",

        // LINHA 4 - AMARELA (ViaQuatro)
        "Luz", "República", "Higienópolis-Mackenzie", "Paulista",
        "Oscar Freire", "Fradique Coutinho", "Faria Lima", "Pinheiros",
        "Butantã", "São Paulo-Morumbi", "Vila Sônia",

        // LINHA 5 - LILÁS (ViaMobilidade)
        "Capão Redondo", "Campo Limpo", "Vila das Belezas", "Giovanni Gronchi",
        "Santo Amaro", "Largo Treze", "Adolfo Pinheiro", "Alto da Boa Vista",
        "Borba Gato", "Brooklin", "Campo Belo", "Eucaliptos",
        "Moema", "AACD-Servidor", "Hospital São Paulo", "Santa Cruz",
        "Chácara Klabin",

        // LINHA 15 - PRATA (Monotrilho)
        "Vila Prudente", "Oratório", "São Lucas", "Vila Tolstói",
        "Vila União", "Jardim Planalto", "Sapopemba", "Fazenda da Juta",
        "São Mateus", "Jardim Colonial",

        // CPTM - Estações Chave e de Baldeação
        "Palmeiras-Barra Funda", "Brás", "Luz", "Tamanduateí",
        "Pinheiros", "Osasco", "Santo André", "Mauá",
        "Grajaú", "Jurubatuba", "Socorro", "Primavera-Interlagos"
    )

    init {
        fetchLineRankings()
    }

    private fun fetchLineRankings() {
        viewModelScope.launch {
            lineRankingRepository.fetchLineRanking()
                .onSuccess { rankings ->
                    lineRankings = rankings
                    Log.d("ViagensViewModel", "Rankings carregados com sucesso: ${rankings.size} itens.")
                }
                .onFailure { e ->
                    // A falha aqui não impede a busca de trajeto, apenas logamos o erro
                    Log.e("ViagensViewModel", "Falha ao carregar rankings: ${e.message}")
                }
        }
    }



    fun buscarTrajeto(origem: String, destino: String) {
        trajetoState.value = null
        isLoading.value = true
        error.value = null

        viewModelScope.launch {
            try {
                val urlCompleta = "https://348106ec772d.ngrok-free.app/api/rota?partida=${origem}&chegada=${destino}"
                var response: TrajetoResponse = apiService.getTrajetoComUrlCompleta(urlCompleta)

                if (response.caminho.isNotEmpty()) {
                    // Lógica para injetar o score na estatística
                    val linhaPrincipalId = getLineIdFromTrajeto(response)
                    val score = lineRankings.find { it.lineId == linhaPrincipalId }?.score

                    // Cria uma nova Estatisticas com o score injetado
                    val estatisticasComScore = response.estatisticas.copy(
                        lineScore = score
                    )

                    // Cria um novo TrajetoResponse com as estatísticas atualizadas
                    val trajetoAtualizado = response.copy(
                        estatisticas = estatisticasComScore
                    )

                    trajetoState.value = trajetoAtualizado
                    Log.d("ViagensViewModel", "Trajeto e ranking carregados para linha ID: $linhaPrincipalId, Score: $score")
                } else {
                    error.value = "Não foi possível encontrar um trajeto válido entre as estações."
                }

            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string() ?: "N/A"
                error.value = "Erro de servidor: ${e.code()}. Detalhes: $errorBody. Verifique as estações."
                Log.e("ViagensViewModel", "HTTP Exception: ${e.code()} - $errorBody")
            } catch (e: IOException) {
                error.value = "Erro de rede. Verifique sua conexão com a internet."
                Log.e("ViagensViewModel", "Network Error: ${e.message}")
            } catch (e: Exception) {
                error.value = "Ocorreu um erro inesperado: ${e.localizedMessage ?: "Erro desconhecido"}"
                Log.e("ViagensViewModel", "Unexpected Error: ${e.message}", e)
            } finally {
                isLoading.value = false
            }
        }
    }

    // FUNÇÃO AUXILIAR PARA PEGAR O ID DA LINHA PRINCIPAL
    private fun getLineIdFromTrajeto(trajeto: TrajetoResponse): Int {
        val linhaNome = trajeto.caminho.firstOrNull()?.linha ?: return -1

        return when {
            // IDs: 1 a 3 e 15 (Monotrilho)
            linhaNome.contains("L1-Azul", ignoreCase = true) || linhaNome.contains("Azul", ignoreCase = true) -> 1
            linhaNome.contains("L2-Verde", ignoreCase = true) || linhaNome.contains("Verde", ignoreCase = true) -> 2
            linhaNome.contains("L3-Vermelha", ignoreCase = true) || linhaNome.contains("Vermelha", ignoreCase = true) -> 3
            linhaNome.contains("L15-Prata", ignoreCase = true) || linhaNome.contains("Prata", ignoreCase = true) -> 15

            // IDs: 4 e 5
            linhaNome.contains("L4-Amarela", ignoreCase = true) || linhaNome.contains("Amarela", ignoreCase = true) -> 4
            linhaNome.contains("L5-Lilás", ignoreCase = true) || linhaNome.contains("Lilás", ignoreCase = true) -> 5


            linhaNome.contains("L8-Diamante", ignoreCase = true) || linhaNome.contains("Diamante", ignoreCase = true) -> 8
            linhaNome.contains("L9-Esmeralda", ignoreCase = true) || linhaNome.contains("Esmeralda", ignoreCase = true) -> 9

            // CPTM - Estaduais
            linhaNome.contains("L7-Rubi", ignoreCase = true) || linhaNome.contains("Rubi", ignoreCase = true) -> 7
            linhaNome.contains("L10-Turquesa", ignoreCase = true) || linhaNome.contains("Turquesa", ignoreCase = true) -> 10
            linhaNome.contains("L11-Coral", ignoreCase = true) || linhaNome.contains("Coral", ignoreCase = true) -> 11
            linhaNome.contains("L12-Safira", ignoreCase = true) || linhaNome.contains("Safira", ignoreCase = true) -> 12
            linhaNome.contains("L13-Jade", ignoreCase = true) || linhaNome.contains("Jade", ignoreCase = true) -> 13
            else -> {
                Log.w("ViagensViewModel", "Nome da linha '$linhaNome' não mapeado para Line ID de ranking. Retornando -1.")
                -1
            }
        }
    }

    fun formatarNomeEstacao(nome: String): String {
        return nome.trim()
            .replace("Estação", "", ignoreCase = true)
            .trim()
            .split(" ")
            .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
    }

    fun filterStations(query: String): List<String> {
        if (query.isBlank()) return emptyList()

        val normalizedQuery = query.trim().lowercase()

        return allStations
            .filter { it.lowercase().contains(normalizedQuery) }
            .sortedBy {
                if (it.lowercase().startsWith(normalizedQuery)) 0 else 1
            }
    }
}