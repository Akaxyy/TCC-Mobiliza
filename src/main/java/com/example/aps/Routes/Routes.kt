package com.example.aps.Routes


// Objeto que contém todas as constantes de navegacao para evitar erros de digitacao
object Routes {
    // Rotas Principais
    const val HOME = "home"
    const val COMUNIDADE = "comunidade"
    const val NOTICIAS = "noticias"
    const val FAVORITOS = "favoritos"
    const val PERFIL = "perfil"

    // Rotas Secundárias
    const val MAPA_SCREEN = "mapa_screen"
    const val VIAGENS_SCREEN = "viagens_screen"
    const val ESTACOES_SCREEN = "estacoes_screen"
    const val SUPORTE_SCREEN = "suporte_screen"
    const val CANAIS_ATENDIMENTO = "canais_atendimento_telef"
    const val DICAS_SUPORTE = "dicas_suporte"

    // Rotas Genéricas/Detalhes
    const val COMMUNITY_DETAIL = "community_detail/{nomeDaLinha}"
    const val ESTACOES_WRAPPER = "estacoes_detail/{nomeDaLinha}"

    const val LINE_UPDATES_SCREEN = "line_updates/{lineId}/{lineName}/{corPrincipalHex}/{textSizeFactor}"
}