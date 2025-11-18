package com.example.aps.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aps.API.RetrofitClient
import com.example.aps.API.ApiServiceTrajeto
import com.example.aps.LineRanking.LineRankingItem
import com.example.aps.Model.GTFS.StationCoordinates
import com.example.aps.Model.GTFS.TrainArrival
import com.example.aps.Model.TrajetoResponse
import com.example.aps.data.repository.LineRankingRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import kotlin.math.roundToInt


class ViagensViewModel(
    private val lineRankingRepository: LineRankingRepository = LineRankingRepository()
) : ViewModel() {

    // Estado da busca de trajeto (Rota)
    val trajetoState = mutableStateOf<TrajetoResponse?>(null)
    val isLoading = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)

    // Estado da previsão de chegada (ETA)
    private val _trainArrival = MutableStateFlow<TrainArrival?>(null)
    val trainArrival: StateFlow<TrainArrival?> = _trainArrival.asStateFlow()

    private val apiService: ApiServiceTrajeto = RetrofitClient.apiService
    // A lista deve ser preenchida na inicialização
    private var lineRankings = listOf<LineRankingItem>()

    // ------------------------------------------------------------
    // 🔥 NOVO: BLOCO INIT PARA CARREGAR OS RANKINGS
    // ------------------------------------------------------------
    init {
        loadLineRankings()
    }

    // ------------------------------------------------------------
    // 🔥 NOVO: FUNÇÃO PARA CARREGAR OS RANKINGS
    // ------------------------------------------------------------
    private fun loadLineRankings() {
        viewModelScope.launch {
            try {
                // Chama a função do repositório para buscar os rankings (deve ser implementado no LineRankingRepository)
                lineRankings = lineRankingRepository.loadRankings()
                Log.d("LineRanking", "Rankings carregados: ${lineRankings.size} itens.")
            } catch (e: Exception) {
                Log.e("LineRanking", "Erro ao carregar LineRankings: ${e.localizedMessage}")
            }
        }
    }


    // ------------------------------------------------------------
    // DADOS DE ESTAÇÕES E COORDENADAS (APENAS REFERÊNCIA)
    // ... (MANTIDO O MESMO CÓDIGO)
    // ------------------------------------------------------------

    private val cptmCoordinates = mapOf(
        "Grajaú" to Pair(-23.7446, -46.6931),
        "Jurubatuba" to Pair(-23.6807, -46.7015),
        "Socorro" to Pair(-23.6503, -46.7074),
        "Primavera-Interlagos" to Pair(-23.6843, -46.7100),
        "Lapa" to Pair(-23.5177, -46.7058),
        "Pinheiros" to Pair(-23.5671, -46.6956),
        "Osasco" to Pair(-23.5324, -46.7926),
        "Santo André" to Pair(-23.6606, -46.5335),
        "Mauá" to Pair(-23.6679, -46.4607),
        "Brás" to Pair(-23.5426, -46.6164),
        "Luz" to Pair(-23.5365, -46.6335),
        "Tamanduateí" to Pair(-23.6039, -46.6032)
    )

    private val allCoordinates = StationCoordinates.map + cptmCoordinates

    private val allStations = listOf(
        "Tucuruvi", "Parada Inglesa", "Jardim São Paulo-Ayrton Senna", "Santana", "Carandiru", "Portuguesa-Tietê", "Armênia",
        "Tiradentes", "Luz", "São Bento", "Sé", "Japão-Liberdade", "São Joaquim", "Vergueiro", "Paraíso",
        "Ana Rosa", "Vila Mariana", "Santa Cruz", "Praça da Árvore", "Saúde-Ultrafarma", "São Judas", "Conceição", "Jabaquara",
        "Vila Madalena", "Sumaré", "Clínicas", "Consolação", "Trianon-Masp", "Brigadeiro", "Chácara Klabin", "Tamanduateí", "Vila Prudente", "Sacomã",
        "Alto do Ipiranga", "Santos-Imigrantes",
        "Palmeiras-Barra Funda", "Marechal Deodoro", "Santa Cecília", "República", "Anhangabaú", "Pedro II", "Brás", "Bresser-Mooca", "Belém", "Tatuapé",
        "Carrão-Assaí Atacadista", "Penha-Lojas Besni", "Vila Matilde", "Guilhermina-Esperança", "Patriarca-Vila Ré", "Artur Alvim", "Corinthians-Itaquera",
        "Higienópolis-Mackenzie", "Paulista", "Oscar Freire", "Fradique Coutinho", "Faria Lima", "Pinheiros", "Butantã", "São Paulo-Morumbi", "Vila Sônia",
        "Capão Redondo", "Campo Limpo", "Vila das Belezas", "Giovanni Gronchi", "Santo Amaro", "Largo Treze", "Adolfo Pinheiro", "Alto da Boa Vista", "Borba Gato",
        "Brooklin", "Campo Belo", "Eucaliptos", "Moema", "AACD-Servidor", "Hospital São Paulo",
        "Vila Prudente", "Oratório", "São Lucas", "Camilo Haddad", "Vila Tolstói", "Vila União", "Jardim Planalto", "Sapopemba", "Fazenda da Juta", "São Mateus", "Jardim Colonial",
        "Júlio Prestes", "Lapa", "Domingos de Moraes", "Imperatriz Leopoldina", "Presidente Altino", "Osasco", "Comandante Sampaio", "Quitaúna", "General Miguel Costa", "Carapicuíba", "Santa Terezinha", "Antônio João", "Barueri", "Jardim Belval", "Jandira", "Sagrado Coração", "Engenheiro Cardoso", "Itapevi", "Amador Bueno",
        "Grajaú", "Mendes-Vila Natal", "Autódromo", "Interlagos", "Jurubatuba", "Socorro", "Granja Julieta", "Morumbi", "Berrini", "Vila Olímpia", "Cidade Jardim", "Ceasa", "Villa Lobos-Jaguaré",
        "Ferraz de Vasconcelos", "Poá", "Calmon Viana", "Suzano", "Jundiapeba", "Brás Cubas", "Mogi das Cruzes", "Estudantes", "Engenheiro Goulart", "USP Leste", "Comendador Ermelino", "São Miguel Paulista", "Jardim Helena", "Itaim Paulista", "Jardim Romano", "Engenheiro Manoel Feio", "Itaquaquecetuba", "Aracaré", "CECAP", "Aeroporto-Guarulhos"
    )

    // ... (restante do código de allLineDistances, allLineIndices, getLineSpeedKmH)
    private val allLineDistances = mapOf(
        "L01" to listOf(
            "Tucuruvi" to 0.890, "Parada Inglesa" to 1.000, "Jardim São Paulo" to 1.520,
            "Santana" to 0.730, "Carandiru" to 0.777, "Portuguesa-Tietê" to 1.234,
            "Armênia" to 0.750, "Tiradentes" to 0.547, "Luz" to 0.890,
            "São Bento" to 0.700, "Sé" to 0.620, "Japão-Liberdade" to 0.807,
            "São Joaquim" to 0.791, "Vergueiro" to 0.693, "Paraíso" to 0.685,
            "Ana Rosa" to 0.991, "Vila Mariana" to 1.100, "Santa Cruz" to 1.280,
            "Praça da Árvore" to 0.904, "Saúde" to 0.794, "São Judas" to 1.070,
            "Conceição" to 1.230, "Jabaquara" to 0.0
        ),
        "L02" to listOf(
            "Vila Madalena" to 1.400, "Sumaré" to 0.813, "Clínicas" to 0.818,
            "Consolação" to 0.951, "Trianon-Masp" to 0.860, "Brigadeiro" to 1.000,
            "Paraíso" to 1.500,
            "Ana Rosa" to 0.0,
            "Chácara Klabin" to 1.110, "Santos-Imigrantes" to 1.110,
            "Alto do Ipiranga" to 1.000, "Sacomã" to 1.690,
            "Tamanduateí" to 1.280, "Vila Prudente" to 0.0
        ),
        "L03" to listOf(
            "Palmeiras-Barra Funda" to 1.640, "Marechal Deodoro" to 0.904, "Santa Cecília" to 0.851,
            "República" to 0.592, "Anhangabaú" to 0.618, "Sé" to 0.799,
            "Pedro II" to 1.040, "Brás" to 0.891, "Bresser-Mooca" to 1.830,
            "Belém" to 1.350, "Tatuapé" to 1.300, "Carrão" to 2.220,
            "Penha" to 1.220, "Vila Matilde" to 1.510, "Guilhermina-Esperança" to 1.700,
            "Patriarca" to 2.140, "Artur Alvim" to 1.400, "Corinthians-Itaquera" to 0.0
        ),
        "L04" to listOf(
            "Luz" to 1.290, "República" to 1.150, "Higienópolis-Mackenzie" to 1.220,
            "Paulista" to 1.180, "Oscar Freire" to 1.400, "Fradique Coutinho" to 0.990,
            "Faria Lima" to 0.896, "Pinheiros" to 0.876, "Butantã" to 2.450,
            "São Paulo-Morumbi" to 1.500, "Vila Sônia" to 0.0
        ),
        "L05" to listOf(
            "Chácara Klabin" to 0.944, "Santa Cruz" to 0.896, "Hospital São Paulo" to 0.692,
            "AACD-Servidor" to 1.270, "Moema" to 0.928, "Eucaliptos" to 1.760,
            "Campo Belo" to 1.050, "Brooklin" to 0.932, "Borba Gato" to 1.090,
            "Alto da Boa Vista" to 1.100, "Adolfo Pinheiro" to 0.922,
            "Largo Treze" to 0.837, "Santo Amaro" to 2.200, "Giovanni Gronchi" to 1.580,
            "Vila das Belezas" to 1.820, "Campo Limpo" to 1.470, "Capão Redondo" to 0.0
        ),
        "L08" to listOf(
            "Júlio Prestes" to 3.190, "Barra Funda" to 3.340, "Lapa" to 2.490,
            "Domingos de Moraes" to 1.810, "Imperatriz Leopoldina" to 2.850,
            "Presidente Altino" to 1.670, "Osasco" to 2.280, "Comandante Sampaio" to 1.240,
            "Quitaúna" to 0.850, "General Miguel Costa" to 2.200, "Carapicuíba" to 1.340,
            "Santa Terezinha" to 1.040, "Antônio João" to 2.250, "Barueri" to 1.810,
            "Jardim Belval" to 1.110, "Jardim Silveira" to 1.190, "Jandira" to 1.420,
            "Sagrado Coração" to 1.650, "Engenheiro Cardoso" to 1.670, "Itapevi" to 5.130,
            "Várzea Paulista" to 5.830, "Campo Limpo Paulista" to 3.980, "Botujuru" to 6.600,
            "Francisco Morato" to 3.720, "Baltazar Fidélis" to 2.370, "Franco da Rocha" to 4.930,
            "Caieiras" to 4.730, "Perus" to 3.980, "Vila Aurora" to 2.350,
            "Jaraguá" to 2.030, "Vila Clarice" to 2.780, "Pirituba" to 2.170,
            "Piqueri" to 2.140, "Água Branca" to 2.230, "Barra Funda" to 0.0
        ),
        "L09" to listOf(
            "Osasco" to 2.300, "Presidente Altino" to 1.430, "Ceasa" to 2.500,
            "Villa Lobos-Jaguaré" to 1.510, "Pinheiros" to 0.817, "Hebraica-Cidade Jardim" to 1.540,
            "Cidade Jardim" to 0.949, "Vila Olímpia" to 1.340, "Berrini" to 1.950,
            "Morumbi" to 1.230, "Granja Julieta" to 1.810, "João Dias" to 1.999,
            "Santo Amaro" to 1.310, "Socorro" to 1.760, "Jurubatuba" to 3.920,
            "Autódromo" to 1.880, "Interlagos" to 1.690, "Grajaú" to 0.0
        ),
        "L10" to listOf(
            "Luz" to 2.130, "Brás" to 1.720, "Mooca" to 2.950, "Ipiranga" to 1.350,
            "Tamanduateí" to 2.900, "São Caetano" to 3.300, "Utinga" to 1.600,
            "Prefeito Saladino" to 1.800, "Santo André" to 4.130, "Capuava" to 3.270,
            "Mauá" to 3.030, "Guapituba" to 4.450, "Ribeirão Pires" to 4.480,
            "Rio Grande da Serra" to 0.0
        ),
        "L11" to listOf(
            "Luz" to 4.230, "Brás" to 11.560, "Tatuapé" to 2.400, "Corinthians-Itaquera" to 2.400,
            "Dom Bosco" to 1.710, "José Bonifácio" to 1.740, "Guaianases" to 3.740,
            "Antônio Gianetti" to 2.320, "Ferraz de Vasconcelos" to 3.060, "Poá" to 1.140,
            "Calmon Viana" to 2.760, "Suzano" to 5.410, "Jundiapeba" to 3.480,
            "Brás Cubas" to 3.380, "Mogi das Cruzes" to 1.450, "Estudantes" to 0.0
        ),
        "L12" to listOf(
            "Brás" to 4.230, "Tatuapé" to 8.840, "Engenheiro Goulart" to 2.580,
            "USP Leste" to 2.130, "Comendador Ermelino" to 4.550, "São Miguel Paulista" to 2.330,
            "Jardim Helena" to 1.970, "Itaim Paulista" to 2.050, "Jardim Romano" to 1.960,
            "Engenheiro Manoel Feio" to 2.300, "Itaquaquecetuba" to 2.760, "Aracaré" to 3.040,
            "Calmon Viana" to 0.0
        ),
        "L13" to listOf(
            "Engenheiro Goulart" to 7.140, "CECAP" to 1.620, "Aeroporto-Guarulhos" to 0.0
        ),
        "L15" to listOf(
            "Vila Prudente" to 2.160, "Oratório" to 2.000, "São Lucas" to 1.060,
            "Camilo Haddad" to 1.200, "Vila Tolstói" to 1.250, "Vila União" to 0.904,
            "Jardim Planalto" to 1.260, "Sapopemba" to 1.500, "Fazenda da Juta" to 1.210,
            "São Mateus" to 1.770, "Jardim Colonial" to 0.0
        )
    )
    private val allLineIndices = allLineDistances.mapValues { (_, stations) ->
        stations.mapIndexed { index, pair -> pair.first to index }.toMap()
    }

    private fun getLineSpeedKmH(lineId: String): Double {
        return when (lineId) {
            "L01", "L02", "L03", "L04", "L05", "L15" -> 40.0
            "L07", "L08", "L09", "L10", "L11", "L12", "L13" -> 35.0
            else -> 35.0
        } / 60.0
    }

    fun calcularEtaParaLinha(lineId: String, estacao: String, headway: Int): Int {
        val distancias = allLineDistances[lineId] ?: return -1
        val indices = allLineIndices[lineId] ?: return -1
        val velocidadeKmMin = getLineSpeedKmH(lineId)

        val idx = indices[estacao] ?: return -1

        var distanciaTotalKm = 0.0
        for (i in 0 until idx) {
            distanciaTotalKm += distancias[i].second
        }
        val tempoDistancia = distanciaTotalKm / velocidadeKmMin

        val nowMin = System.currentTimeMillis().toDouble() / 60000.0
        val phase = nowMin % headway

        val raw = ((tempoDistancia - phase) % headway + headway) % headway
        return raw.roundToInt().coerceAtLeast(1)
    }

    private fun startCountdown(stationName: String, lineId: String, initialEta: Int) {
        viewModelScope.launch {
            var currentEta = initialEta.coerceAtLeast(0)

            Log.d("ETA-COUNTDOWN", "Iniciando contagem p/ $stationName ($lineId) com $currentEta min")

            while (currentEta >= 0) {
                _trainArrival.value = TrainArrival(
                    stationName = stationName,
                    lineId = lineId,
                    arrivalTimeInMinutes = currentEta,
                    isDelayed = currentEta > 10,
                    status = "Próximo trem em $currentEta min"
                )

                if (currentEta == 0) break

                delay(60000L)
                currentEta--
            }

            Log.d("ETA-COUNTDOWN", "Contagem zerada. Buscando nova previsão para $stationName.")
            fetchTrainArrivalByStationName(stationName)
        }
    }

    private fun fetchHeadway(lineId: String): Int {
        return when (lineId) {
            "L01", "L03" -> 3
            "L02", "L04", "L05" -> 4
            "L08", "L09", "L15" -> 6
            "L07", "L10", "L11", "L12", "L13" -> 7
            else -> 6
        }.coerceAtLeast(1)
    }

    fun fetchTrainArrivalByStationName(stationName: String) {
        Log.d("ETA-DEBUG", "============================")
        Log.d("ETA-DEBUG", "Solicitando previsão → $stationName")

        _trainArrival.value = null

        viewModelScope.launch {
            val formatted = formatarNomeEstacao(stationName)
            val lineId = findLineIdForStation(formatted)
            val headway = fetchHeadway(lineId)

            if (lineId in allLineDistances.keys && formatted in allLineIndices[lineId]!!.keys) {
                val eta = calcularEtaParaLinha(lineId, formatted, headway)
                Log.d("ETA-DEBUG", "MODELO ALTA FIDELIDADE ($lineId): ETA = $eta min")

                startCountdown(formatted, lineId, eta)
                return@launch
            }

            if (lineId != "??") {
                val randomEta = (1..headway).random()

                Log.d("ETA-DEBUG", "MODELO PLÁUSIVEL ($lineId): Headway $headway min, ETA Inicial $randomEta min")

                startCountdown(formatted, lineId, randomEta)
                return@launch
            }

            _trainArrival.value = TrainArrival(
                stationName = formatted,
                lineId = "??",
                arrivalTimeInMinutes = null,
                isDelayed = false,
                status = "Sem previsão disponível."
            )
        }
    }

    // -----------------------------------------------------------
    // FUNÇÕES DE BUSCA DE TRAJETO E AUXILIARES
    // -----------------------------------------------------------

    private fun findLineIdForStation(stationName: String): String {
        return when (stationName) {
            "Jabaquara", "Conceição", "São Judas", "Saúde", "Praça da Árvore", "Santa Cruz", "Vila Mariana", "Ana Rosa", "Paraíso", "Vergueiro", "São Joaquim", "Japão-Liberdade", "Sé", "São Bento", "Luz", "Tiradentes", "Armênia", "Portuguesa-Tietê", "Carandiru", "Santana", "Jardim São Paulo", "Parada Inglesa", "Tucuruvi" -> "L01"
            "Vila Madalena", "Sumaré", "Clínicas", "Consolação", "Trianon-Masp", "Brigadeiro", "Chácara Klabin", "Santos-Imigrantes", "Alto do Ipiranga", "Sacomã", "Tamanduateí", "Vila Prudente" -> "L02"
            "Palmeiras-Barra Funda", "Marechal Deodoro", "Santa Cecília", "República", "Anhangabaú", "Pedro II", "Brás", "Bresser-Mooca", "Belém", "Tatuapé", "Carrão", "Penha", "Vila Matilde", "Guilhermina-Esperança", "Patriarca", "Artur Alvim", "Corinthians-Itaquera" -> "L03"
            "Higienópolis-Mackenzie", "Paulista", "Oscar Freire", "Fradique Coutinho", "Faria Lima", "Butantã", "São Paulo-Morumbi", "Vila Sônia" -> "L04"
            "Capão Redondo", "Campo Limpo", "Vila das Belezas", "Giovanni Gronchi", "Largo Treze", "Adolfo Pinheiro", "Alto da Boa Vista", "Borba Gato", "Brooklin", "Campo Belo", "Eucaliptos", "Moema", "AACD-Servidor", "Hospital São Paulo", "Santa Cruz", "Chácara Klabin" -> "L05"
            "Júlio Prestes", "Barra Funda", "Lapa", "Domingos de Moraes", "Imperatriz Leopoldina", "Presidente Altino", "Osasco", "Comandante Sampaio", "Quitaúna", "General Miguel Costa", "Carapicuíba", "Santa Terezinha", "Antônio João", "Barueri", "Jardim Belval", "Jandira", "Sagrado Coração", "Engenheiro Cardoso", "Itapevi", "Amador Bueno", "Várzea Paulista", "Campo Limpo Paulista", "Botujuru", "Francisco Morato", "Baltazar Fidélis", "Franco da Rocha", "Caieiras", "Perus", "Vila Aurora", "Jaraguá", "Vila Clarice", "Pirituba", "Piqueri", "Água Branca" -> "L08"
            "Grajaú", "Mendes-Vila Natal", "Autódromo", "Interlagos", "Jurubatuba", "Socorro", "Santo Amaro", "Granja Julieta", "Morumbi", "Berrini", "Vila Olímpia", "Cidade Jardim", "Hebraica-Cidade Jardim", "Pinheiros", "Ceasa", "Villa Lobos-Jaguaré", "Presidente Altino", "Osasco" -> "L09"
            "Luz", "Brás", "Mooca", "Ipiranga", "Tamanduateí", "São Caetano", "Utinga", "Prefeito Saladino", "Santo André", "Capuava", "Mauá", "Guapituba", "Ribeirão Pires", "Rio Grande da Serra" -> "L10"
            "Luz", "Brás", "Tatuapé", "Corinthians-Itaquera", "Dom Bosco", "José Bonifácio", "Guaianases", "Antônio Gianetti", "Ferraz de Vasconcelos", "Poá", "Calmon Viana", "Suzano", "Jundiapeba", "Brás Cubas", "Mogi das Cruzes", "Estudantes" -> "L11"
            "Brás", "Tatuapé", "Engenheiro Goulart", "USP Leste", "Comendador Ermelino", "São Miguel Paulista", "Jardim Helena", "Itaim Paulista", "Jardim Romano", "Engenheiro Manoel Feio", "Itaquaquecetuba", "Aracaré", "Calmon Viana" -> "L12"
            "Engenheiro Goulart", "CECAP", "Aeroporto-Guarulhos" -> "L13"
            "Vila Prudente", "Oratório", "São Lucas", "Camilo Haddad", "Vila Tolstói", "Vila União", "Jardim Planalto", "Sapopemba", "Fazenda da Juta", "São Mateus", "Jardim Colonial" -> "L15"
            else -> "??"
        }
    }


    fun buscarTrajeto(origem: String, destino: String) {
        trajetoState.value = null
        isLoading.value = true
        error.value = null

        viewModelScope.launch {
            try {
                val urlCompleta = "http://srv1070702.hstgr.cloud:5000/api/rota?partida=${origem}&chegada=${destino}"
                val response: TrajetoResponse = apiService.getTrajetoComUrlCompleta(urlCompleta)

                if (response.caminho.isNotEmpty()) {
                    val linhaPrincipalId = getLineIdFromTrajeto(response)

                    // ✅ AQUI: lineRankings já deve estar preenchida (via init)
                    val score = lineRankings.find { it.lineId == linhaPrincipalId }?.score

                    val estatisticasComScore = response.estatisticas.copy(
                        lineScore = score
                    )

                    trajetoState.value = response.copy(
                        estatisticas = estatisticasComScore
                    )

                } else {
                    error.value = "Não foi possível encontrar um trajeto válido entre as estações."
                }

            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string() ?: "N/A"
                error.value = "Erro de servidor: ${e.code()} - $errorBody"
                Log.e("API-ERROR", "HTTP Exception: ${e.code()} - $errorBody")
            } catch (e: IOException) {
                error.value = "Erro de rede. Verifique sua conexão."
                Log.e("API-ERROR", "Network Error: ${e.localizedMessage}")
            } catch (e: Exception) {
                error.value = "Erro inesperado: ${e.localizedMessage}"
                Log.e("API-ERROR", "Unexpected Error: ${e.localizedMessage}")
            } finally {
                isLoading.value = false
            }
        }
    }


    private fun getLineIdFromTrajeto(trajeto: TrajetoResponse): Int {
        val linhaNome = trajeto.caminho.firstOrNull()?.linha ?: return -1

        return when {
            linhaNome.contains("Azul", true) -> 1
            linhaNome.contains("Verde", true) -> 2
            linhaNome.contains("Vermelha", true) -> 3
            linhaNome.contains("Prata", true) -> 15
            linhaNome.contains("Amarela", true) -> 4
            linhaNome.contains("Lilás", true) -> 5
            linhaNome.contains("Diamante", true) -> 8
            linhaNome.contains("Esmeralda", true) -> 9
            linhaNome.contains("Turquesa", true) -> 10
            linhaNome.contains("Coral", true) -> 11
            linhaNome.contains("Safira", true) -> 12
            linhaNome.contains("Jade", true) -> 13
            linhaNome.contains("Rubi", true) -> 7
            else -> -1
        }
    }

    fun formatarNomeEstacao(nome: String): String {
        return nome.trim()
            .replace("Estação", "", true)
            .trim()
            .split(" ")
            .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
            .replace("Jardim São Paulo-Ayrton Senna", "Jardim São Paulo")
            .replace("Penha-Lojas Besni", "Penha")
            .replace("Carrão-Assaí Atacadista", "Carrão")
            .replace("Patriarca-Vila Ré", "Patriarca")
            .replace("Saúde-Ultrafarma", "Saúde")
            .replace("Villa Lobos – Jaguaré", "Villa Lobos-Jaguaré")
            .replace("Comandante Sampaio", "Comandante Sampaio")
            .replace("Japão-Liberdade", "Japão-Liberdade")
            .replace("Trianon-Masp", "Trianon-Masp")
            .replace("Portuguesa-Tietê", "Portuguesa-Tietê")
    }

    fun filterStations(query: String): List<String> {
        if (query.isBlank()) return emptyList()
        val normalized = query.trim().lowercase()
        return allStations
            .filter { it.lowercase().contains(normalized) }
            .sortedBy { if (it.lowercase().startsWith(normalized)) 0 else 1 }
    }
}