package com.example.aps.Model.GenericNoticias;

import StatusType


data class StatusID(val id: Long)

data class StatusDetail(
        val id: Long,
        val codigo: Int,
        val criado: String,
        val situacao: String,
        val descricao: String?
)

// tatusType está definido e importado
data class StatusUpdate(
        val timestamp: String,

        val formattedTime: String,

        val description: String,
        val status: StatusType
)
data class UpdateSection(
        val title: String, // Ex: "Hoje", "Ontem"
        val updates: List<StatusUpdate>
)