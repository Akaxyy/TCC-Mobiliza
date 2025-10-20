package com.example.aps.Model.TrajetoEstacoes


import androidx.compose.ui.graphics.Color


data class Estacao(
    val nome: String,
    val temAcessibilidade: Boolean = false,
    val tempoEstimadoProximaEstacao: String? = null
)

data class Linha(
    val nome: String,
    val cor: Color,
    val numeroLinha: Int,
    val iconResId: Int? = null,
    val estacoes: List<Estacao>
)

object LinhaColors {
    val Azul = Color(0xFF2A64AD) // Exemplo de cor para Linha Azul
    val Verde = Color(0xFF008B4B) // Exemplo de cor para Linha Verde
    val Vermelha = Color(0xFFEE3E33) // Exemplo de cor para Linha Vermelha
    val Amarela = Color(0xFFFCE300) // Exemplo de cor para Linha Amarela
    val Lilas = Color(0xFF8C228E) // Exemplo de cor para Linha Lilás
    val Rubi = Color(0xFF8C1B4C) // CPTM - Linha 7
    val Diamante = Color(0xFF9B9A98) // CPTM - Linha 8
    val Esmeralda = Color(0xFF00B89C) // CPTM - Linha 9
    val Turquesa = Color(0xFF00768C) // CPTM - Linha 10
    val Coral = Color(0xFFED1C24) // CPTM - Linha 11
    val Safira = Color(0xFF2966AE) // CPTM - Linha 12
    val Jade = Color(0xFF00B261) // CPTM - Linha 13 (Aeroporto)
    val Prata = Color(0xFFA0A0A0) // Monotrilho - Linha 15
    val Bronze = Color(0xFF964B00) // Monotrilho - Linha 17 (Exemplo de cor, se houver)
}



val todasAsLinhasDeTransporte = listOf(
    Linha(
        nome = "Linha Azul",
        numeroLinha = 1,
        cor = LinhaColors.Azul,

        estacoes = listOf(
            Estacao("Jabaquara", true),
            Estacao("Conceição"),
            Estacao("São Judas"),
            Estacao("Saúde"),
            Estacao("Praça da Árvore"),
            Estacao("Santa Cruz", true),
            Estacao("Vila Mariana"),
            Estacao("Ana Rosa", true),
            Estacao("Paraíso", true),
            Estacao("Vergueiro"),
            Estacao("São Joaquim"),
            Estacao("Liberdade"),
            Estacao("Sé", true),
            Estacao("São Bento"),
            Estacao("Luz", true),
            Estacao("Tiradentes"),
            Estacao("Armênia"),
            Estacao("Portuguesa-Tietê"),
            Estacao("Carandiru"),
            Estacao("Santana"),
            Estacao("Jardim São Paulo-Ayton Senna"),
            Estacao("Parada Inglesa"),
            Estacao("Tucuruvi", true)
        )
    ),
    Linha(
        nome = "Linha Verde",
        numeroLinha = 2,
        cor = LinhaColors.Verde,
        // iconResId = R.drawable.linha_verde_metro,
        estacoes = listOf(
            Estacao("Vila Prudente", true),
            Estacao("Tamanduateí", true),
            Estacao("Sacomã"),
            Estacao("Alto do Ipiranga"),
            Estacao("Santos-Imigrantes"),
            Estacao("Chácara Klabin", true),
            Estacao("Santa Cruz", true),
            Estacao("Ana Rosa", true),
            Estacao("Paraíso", true),
            Estacao("Brigadeiro"),
            Estacao("Trianon-Masp"),
            Estacao("Consolação", true),
            Estacao("Clínicas"),
            Estacao("Sumaré"),
            Estacao("Vila Madalena", true)
        )
    ),
    Linha(
        nome = "Linha Vermelha",
        numeroLinha = 3,
        cor = LinhaColors.Vermelha,

        estacoes = listOf(
            Estacao("Palmeiras-Barra Funda", true),
            Estacao("Marechal Deodoro"),
            Estacao("Santa Cecília"),
            Estacao("República", true),
            Estacao("Anhangabaú"),
            Estacao("Sé", true),
            Estacao("Pedro II"),
            Estacao("Brás", true),
            Estacao("Bresser-Mooca"),
            Estacao("Belém"),
            Estacao("Tatuapé", true),
            Estacao("Carrão"),
            Estacao("Penha"),
            Estacao("Vila Matilde"),
            Estacao("Artur Alvim"),
            Estacao("Corinthians-Itaquera", true)
        )
    ),
    Linha(
        nome = "Linha Amarela",
        numeroLinha = 4,
        cor = LinhaColors.Amarela,

        estacoes = listOf(
            Estacao("Luz", true),
            Estacao("República", true),
            Estacao("Higienópolis-Mackenzie", true),
            Estacao("Paulista", true),
            Estacao("Fradique Coutinho", true),
            Estacao("Faria Lima", true),
            Estacao("Pinheiros", true),
            Estacao("Butantã", true),
            Estacao("São Paulo-Morumbi", true),
            Estacao("Vila Sônia", true)
        )
    ),
    Linha(
        nome = "Linha Lilás",
        numeroLinha = 5,
        cor = LinhaColors.Lilas,
        estacoes = listOf(
            Estacao("Capão Redondo", true),
            Estacao("Campo Limpo", true),
            Estacao("Vila das Belezas", true),
            Estacao("Giovanni Gronchi", true),
            Estacao("Santo Amaro", true),
            Estacao("Largo Treze", true),
            Estacao("Adolfo Pinheiro", true),
            Estacao("Alto da Boa Vista", true),
            Estacao("Borba Gato", true),
            Estacao("Campo Belo", true),
            Estacao("Eucaliptos", true),
            Estacao("Moema", true),
            Estacao(" AACD-Servidor", true),
            Estacao("Hospital São Paulo", true),
            Estacao("Santa Cruz", true),
            Estacao("Chácara Klabin", true)
        )
    ),
    Linha(
        nome = "Linha Rubi",
        numeroLinha = 7,
        cor = LinhaColors.Rubi,
        estacoes = listOf(
            Estacao("Luz", true),
            Estacao("Palmeiras-Barra Funda", true),
            Estacao("Água Branca"),
            Estacao("Lapa"),
            Estacao("Piqueri"),
            Estacao("Pirituba"),
            Estacao("Vila Aurora"),
            Estacao("Jaraguá"),
            Estacao("Campo Limpo Paulista"),
            Estacao("Várzea Paulista"),
            Estacao("Jundiaí")
        )
    ),
    Linha(
        nome = "Linha Diamante",
        numeroLinha = 8,
        cor = LinhaColors.Diamante,
        estacoes = listOf(
            Estacao("Júlio Prestes", true),
            Estacao("Palmeiras-Barra Funda", true),
            Estacao("Lapa"),
            Estacao("Osasco", true),
            Estacao("Carapicuíba"),
            Estacao("Itapevi"),
            Estacao("Jandira"),
            Estacao("Cotia")
        )
    ),
    Linha(
        nome = "Linha Esmeralda",
        numeroLinha = 9,
        cor = LinhaColors.Esmeralda,
        estacoes = listOf(
            Estacao("Osasco", true),
            Estacao("Presidente Altino"),
            Estacao("Ceasa"),
            Estacao("Villa Lobos-Jaguaré"),
            Estacao("Cidade Universitária"),
            Estacao("Pinheiros", true),
            Estacao("Cidade Jardim"),
            Estacao("Morumbi"),
            Estacao("Berrini"),
            Estacao("Vila Olímpia"),
            Estacao("Cidade Dutra"),
            Estacao("Grajaú")
        )
    ),
    Linha(
        nome = "Linha Turquesa",
        numeroLinha = 10,
        cor = LinhaColors.Turquesa,
        estacoes = listOf(
            Estacao("Brás", true),
            Estacao("Mooca"),
            Estacao("Ipiranga"),
            Estacao("Tamanduateí", true),
            Estacao("São Caetano do Sul"),
            Estacao("Santo André"),
            Estacao("São Bernardo do Campo"),
            Estacao("Rio Grande da Serra")
        )
    ),
    Linha(
        nome = "Linha Coral",
        numeroLinha = 11,
        cor = LinhaColors.Coral,
        estacoes = listOf(
            Estacao("Luz", true),
            Estacao("Brás", true),
            Estacao("Tatuapé", true),
            Estacao("Corinthians-Itaquera", true),
            Estacao("Suzano"),
            Estacao("Mogi das Cruzes"),
            Estacao("Estudantes")
        )
    ),
    Linha(
        nome = "Linha Safira",
        numeroLinha = 12,
        cor = LinhaColors.Safira,
        estacoes = listOf(
            Estacao("Brás", true),
            Estacao("Tatuapé", true),
            Estacao("Engenheiro Goulart", true),
            Estacao("Comendador Ermelindo"),
            Estacao("Guarulhos-Cecap"),
            Estacao("Aeroporto-Guarulhos")
        )
    ),
    Linha(
        nome = "Linha Prata",
        numeroLinha = 15,
        cor = LinhaColors.Prata,
        estacoes = listOf(
            Estacao("Vila Prudente", true),
            Estacao("Oratório", true),
            Estacao("São Lucas"),
            Estacao("Sapopemba"),
            Estacao("Fazenda da Juta"),
            Estacao("São Mateus", true)
        )
    )  )

