package com.example.aps.Model.GenericScreenEstacoes

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController


// Componente de alto nível responsável por renderizar a tela
// de estações de uma linha específica, com base em dados estáticos.


@Composable
fun EstacoesWrapper(
    navController: NavHostController,
    nomeDaLinha: String,
    textSizeFactor: Float
) {
    // Definição das cores principais de cada linha (padrão SP)
    val corAzul = Color(0xFF0056A4)     // Linha 1
    val corVerde = Color(0xFF007A5F)    // Linha 2
    val corVermelha = Color(0xFFC40233) // Linha 3
    val corAmarela = Color(0xFFFFCC00)  // Linha 4
    val corLilás = Color(0xFF8A008A)    // Linha 5
    val corRubi = Color(0xFF9146A3)     // Linha 7 (CPTM)
    val corDiamante = Color(0xFF8B8C8A) // Linha 8 (CPTM)
    val corEsmeralda = Color(0xFF00A38D)// Linha 9 (CPTM)
    val corTurquesa = Color(0xFF007C78) // Linha 10 (CPTM)
    val corCoral = Color(0xFFF7941D)    // Linha 11 (CPTM)
    val corSafira = Color(0xFF00A0E3)   // Linha 12 (CPTM)
    val corJade = Color(0xFF00B09A)     // Linha 13 (CPTM)
    val corPrata = Color(0xFFB5B5B5)    // Linha 15 (Monotrilho)
    val corOuro = Color(0xFFDAA520)     // Linha 17 (Monotrilho)

    // Baldeações comuns entre linhas (reutilizáveis)


    // Metrô
    val baldeacaoSe = listOf(
        LinhaBaldeacao("1", corAzul),
        LinhaBaldeacao("3", corVermelha)
    )
    val baldeacaoParaiso = listOf(
        LinhaBaldeacao("1", corAzul),
        LinhaBaldeacao("2", corVerde)
    )
    val baldeacaoAnaRosa = baldeacaoParaiso
    val baldeacaoTamanduatei = listOf(
        LinhaBaldeacao("2", corVerde),
        LinhaBaldeacao("10", corTurquesa)
    )
    val baldeacaoVilaPrudente = listOf(LinhaBaldeacao("15", corPrata))
    val baldeacaoBarraFundaL8L7 = listOf(
        LinhaBaldeacao("3", corVermelha),
        LinhaBaldeacao("7", corRubi),
        LinhaBaldeacao("8", corDiamante)
    )

    // CPTM
    val baldeacaoLuzCPTM = listOf(
        LinhaBaldeacao("1", corAzul),
        LinhaBaldeacao("4", corAmarela),
        LinhaBaldeacao("7", corRubi),
        LinhaBaldeacao("11", corCoral)
    )
    val baldeacaoTatuape = listOf(
        LinhaBaldeacao("11", corCoral),
        LinhaBaldeacao("12", corSafira)
    )
    val baldeacaoBrás = listOf(
        LinhaBaldeacao("3", corVermelha),
        LinhaBaldeacao("10", corTurquesa),
        LinhaBaldeacao("11", corCoral),
        LinhaBaldeacao("12", corSafira)
    )
    val baldeacaoEngenheiroGoulart = listOf(
        LinhaBaldeacao("12", corSafira),
        LinhaBaldeacao("13", corJade)
    )

    // Listagem de estações por linha


    // ---------- LINHA 1 - AZUL ----------
    val estacoesLinhaAzul = listOf(
        Estacao("Tucuruvi"),
        Estacao("Parada Inglesa"),
        Estacao("Jardim São Paulo-Ayrton Senna"),
        Estacao("Santana"),
        Estacao("Carandiru"),
        Estacao("Portuguesa-Tietê"),
        Estacao("Armênia"),
        Estacao("Tiradentes"),
        Estacao("Luz", linhasBaldeacao = baldeacaoLuzCPTM),
        Estacao("São Bento"),
        Estacao("Sé", linhasBaldeacao = baldeacaoSe),
        Estacao("Liberdade"),
        Estacao("São Joaquim"),
        Estacao("Vergueiro"),
        Estacao("Paraíso", linhasBaldeacao = baldeacaoParaiso),
        Estacao("Ana Rosa", linhasBaldeacao = baldeacaoAnaRosa),
        Estacao("Vila Mariana"),
        Estacao("Santa Cruz", linhasBaldeacao = listOf(LinhaBaldeacao("5", corLilás))),
        Estacao("Praça da Árvore"),
        Estacao("Saúde"),
        Estacao("São Judas"),
        Estacao("Conceição"),
        Estacao("Jabaquara")
    )

    // ---------- LINHA 2 - VERDE ----------
    val estacoesLinhaVerde = listOf(
        Estacao("Vila Madalena"),
        Estacao("Santuário Nossa Senhora de Fátima-Sumaré"),
        Estacao("Clínicas"),
        Estacao("Consolação", linhasBaldeacao = listOf(LinhaBaldeacao("4", corAmarela))),
        Estacao("Trianon-Masp"),
        Estacao("Brigadeiro"),
        Estacao("Paraíso", linhasBaldeacao = baldeacaoParaiso),
        Estacao("Ana Rosa", linhasBaldeacao = baldeacaoAnaRosa),
        Estacao("Chácara Klabin", linhasBaldeacao = listOf(LinhaBaldeacao("5", corLilás))),
        Estacao("Santos-Imigrantes"),
        Estacao("Alto do Ipiranga"),
        Estacao("Sacomã"),
        Estacao("Tamanduateí", linhasBaldeacao = baldeacaoTamanduatei),
        Estacao("Vila Prudente", linhasBaldeacao = baldeacaoVilaPrudente)
    )

    // ---------- LINHA 3 - VERMELHA ----------
    val estacoesLinhaVermelha = listOf(
        Estacao("Palmeiras-Barra Funda", linhasBaldeacao = baldeacaoBarraFundaL8L7),
        Estacao("Marechal Deodoro"),
        Estacao("Santa Cecília"),
        Estacao("República", linhasBaldeacao = listOf(LinhaBaldeacao("4", corAmarela))),
        Estacao("Anhangabaú"),
        Estacao("Sé", linhasBaldeacao = baldeacaoSe),
        Estacao("Pedro II"),
        Estacao("Brás", linhasBaldeacao = baldeacaoBrás),
        Estacao("Bresser-Mooca"),
        Estacao("Belém"),
        Estacao("Tatuapé", linhasBaldeacao = baldeacaoTatuape),
        Estacao("Carrão"),
        Estacao("Penha"),
        Estacao("Vila Matilde"),
        Estacao("Guilhermina-Esperança"),
        Estacao("Patriarca-Vila Ré"),
        Estacao("Artur Alvim"),
        Estacao("Corinthians-Itaquera", linhasBaldeacao = listOf(LinhaBaldeacao("11", corCoral)))
    )

    // ---------- LINHA 4 - AMARELA ----------
    val estacoesLinhaAmarela = listOf(
        Estacao("Luz", linhasBaldeacao = baldeacaoLuzCPTM),
        Estacao("República", linhasBaldeacao = listOf(LinhaBaldeacao("3", corVermelha))),
        Estacao("Paulista", linhasBaldeacao = listOf(LinhaBaldeacao("2", corVerde))),
        Estacao("Faria Lima"),
        Estacao("Pinheiros", linhasBaldeacao = listOf(LinhaBaldeacao("9", corEsmeralda))),
        Estacao("São Paulo-Morumbi")
    )

    // ---------- LINHA 5 - LILÁS ----------
    val estacoesLinhaLilas = listOf(
        Estacao("Capão Redondo", linhasBaldeacao = listOf(LinhaBaldeacao("17", corOuro))),
        Estacao("Campo Limpo"),
        Estacao("Vila das Belezas"),
        Estacao("Giovanni Gronchi"),
        Estacao("Santo Amaro", linhasBaldeacao = listOf(LinhaBaldeacao("9", corEsmeralda))),
        Estacao("Largo Treze"),
        Estacao("Adolfo Pinheiro"),
        Estacao("Alto da Boa Vista"),
        Estacao("Borba Gato"),
        Estacao("Brooklin"),
        Estacao("Campo Belo"),
        Estacao("Eucaliptos"),
        Estacao("Moema"),
        Estacao("AACD-Servidor"),
        Estacao("Hospital São Paulo"),
        Estacao("Santa Cruz", linhasBaldeacao = listOf(LinhaBaldeacao("1", corAzul))),
        Estacao("Chácara Klabin", linhasBaldeacao = listOf(LinhaBaldeacao("2", corVerde)))
    )

    // ---------- LINHA 7 - RUBI ----------
    val estacoesLinhaRubi = listOf(
        Estacao("Luz", linhasBaldeacao = baldeacaoLuzCPTM),
        Estacao("Palmeiras-Barra Funda", linhasBaldeacao = baldeacaoBarraFundaL8L7),
        Estacao("Lapa"),
        Estacao("Pirituba"),
        Estacao("Perus"),
        Estacao("Jundiaí")
    )

    // ---------- LINHA 8 - DIAMANTE ----------
    val estacoesLinhaDiamante = listOf(
        Estacao("Júlio Prestes"),
        Estacao("Palmeiras-Barra Funda", linhasBaldeacao = baldeacaoBarraFundaL8L7),
        Estacao("Lapa"),
        Estacao("Domingos de Moraes"),
        Estacao("Piqueri"),
        Estacao("Presidente Altino", linhasBaldeacao = listOf(LinhaBaldeacao("9", corEsmeralda))),
        Estacao("Osasco", linhasBaldeacao = listOf(
            LinhaBaldeacao("9", corEsmeralda),
            LinhaBaldeacao("10", corTurquesa)
        )),
        Estacao("Comandante Sampaio"),
        Estacao("Quitaúna"),
        Estacao("General Miguel Costa"),
        Estacao("Carapicuíba"),
        Estacao("Santa Terezinha"),
        Estacao("Antonio João"),
        Estacao("Barueri"),
        Estacao("Jardim Belval"),
        Estacao("Jardim Silveira"),
        Estacao("Jandira"),
        Estacao("Sagrado Coração"),
        Estacao("Itapevi")
    )

    // ---------- LINHA 9 - ESMERALDA ----------
    val estacoesLinhaEsmeralda = listOf(
        Estacao("Osasco", linhasBaldeacao = listOf(LinhaBaldeacao("8", corDiamante))),
        Estacao("Presidente Altino", linhasBaldeacao = listOf(LinhaBaldeacao("8", corDiamante))),
        Estacao("Ceasa"),
        Estacao("Villa Lobos-Jaguaré"),
        Estacao("Cidade Universitária"),
        Estacao("Pinheiros", linhasBaldeacao = listOf(LinhaBaldeacao("4", corAmarela))),
        Estacao("Hebraica-Rebouças"),
        Estacao("Cidade Jardim"),
        Estacao("Vila Olímpia"),
        Estacao("Berrini"),
        Estacao("Morumbi"),
        Estacao("Granja Julieta"),
        Estacao("Santo Amaro", linhasBaldeacao = listOf(LinhaBaldeacao("5", corLilás))),
        Estacao("Socorro"),
        Estacao("Jurubatuba"),
        Estacao("Autódromo"),
        Estacao("Primavera-Interlagos"),
        Estacao("Grajaú")
    )

    // ---------- LINHA 10 - TURQUESA ----------
    val estacoesLinhaTurquesa = listOf(
        Estacao("Brás", linhasBaldeacao = baldeacaoBrás),
        Estacao("Juventus-Mooca"),
        Estacao("Ipiranga"),
        Estacao("Tamanduateí", linhasBaldeacao = baldeacaoTamanduatei),
        Estacao("São Caetano do Sul-Prefeito Walter Braido"),
        Estacao("Utinga"),
        Estacao("Santo André-Prefeito Celso Daniel"),
        Estacao("Capuava"),
        Estacao("Mauá"),
        Estacao("Guapituba"),
        Estacao("Ribeirão Pires"),
        Estacao("Rio Grande da Serra")
    )

    // ---------- LINHA 11 - CORAL ----------
    val estacoesLinhaCoral = listOf(
        Estacao("Luz", linhasBaldeacao = baldeacaoLuzCPTM),
        Estacao("Brás", linhasBaldeacao = baldeacaoBrás),
        Estacao("Tatuapé", linhasBaldeacao = baldeacaoTatuape),
        Estacao("Corinthians-Itaquera", linhasBaldeacao = listOf(LinhaBaldeacao("3", corVermelha))),
        Estacao("Dom Bosco"),
        Estacao("José Bonifácio"),
        Estacao("Poá"),
        Estacao("Suzano"),
        Estacao("Ferraz de Vasconcelos"),
        Estacao("Calmon Viana"),
        Estacao("Jundiapeba"),
        Estacao("Mogi das Cruzes"),
        Estacao("Estudantes")
    )

    // ---------- LINHA 12 - SAFIRA ----------
    val estacoesLinhaSafira = listOf(
        Estacao("Brás", linhasBaldeacao = baldeacaoBrás),
        Estacao("Tatuapé", linhasBaldeacao = baldeacaoTatuape),
        Estacao("Carrão"),
        Estacao("Penha"),
        Estacao("Engenheiro Goulart", linhasBaldeacao = baldeacaoEngenheiroGoulart),
        Estacao("São Miguel Paulista"),
        Estacao("Jardim Helena-Vila Mara"),
        Estacao("Jardim Romano"),
        Estacao("Itaim Paulista")
    )

    // ---------- LINHA 15 - PRATA ----------
    val estacoesLinhaPrata = listOf(
        Estacao("Vila Prudente", linhasBaldeacao = listOf(LinhaBaldeacao("2", corVerde))),
        Estacao("Oratório"),
        Estacao("São Lucas"),
        Estacao("Camilo Haddad"),
        Estacao("Vila Tolstói"),
        Estacao("Vila União"),
        Estacao("Jardim Planalto"),
        Estacao("Sapopemba"),
        Estacao("Fazenda da Juta"),
        Estacao("São Mateus")
    )


    val dadosDaLinha = when (nomeDaLinha) {
        "Linha Azul" -> EstacaoData("1", "Linha Azul", corAzul, estacoesLinhaAzul)
        "Linha Verde" -> EstacaoData("2", "Linha Verde", corVerde, estacoesLinhaVerde)
        "Linha Vermelha" -> EstacaoData("3", "Linha Vermelha", corVermelha, estacoesLinhaVermelha)
        "Linha Amarela" -> EstacaoData("4", "Linha Amarela", corAmarela, estacoesLinhaAmarela)
        "Linha Lilás" -> EstacaoData("5", "Linha Lilás", corLilás, estacoesLinhaLilas)
        "Linha Rubi" -> EstacaoData("7", "Linha Rubi", corRubi, estacoesLinhaRubi)
        "Linha Diamante" -> EstacaoData("8", "Linha Diamante", corDiamante, estacoesLinhaDiamante)
        "Linha Esmeralda" -> EstacaoData("9", "Linha Esmeralda", corEsmeralda, estacoesLinhaEsmeralda)
        "Linha Turquesa" -> EstacaoData("10", "Linha Turquesa", corTurquesa, estacoesLinhaTurquesa)
        "Linha Coral" -> EstacaoData("11", "Linha Coral", corCoral, estacoesLinhaCoral)
        "Linha Safira" -> EstacaoData("12", "Linha Safira", corSafira, estacoesLinhaSafira)
        "Linha Prata" -> EstacaoData("15", "Linha Prata (Monotrilho)", corPrata, estacoesLinhaPrata)
        else -> null
    }


    if (dadosDaLinha != null) {
        LineEstacoesScreen(
            data = dadosDaLinha,
            textSizeFactor = textSizeFactor
        )
    } else {
        Text("Estações da $nomeDaLinha não encontradas ou não suportadas.")
    }
}
