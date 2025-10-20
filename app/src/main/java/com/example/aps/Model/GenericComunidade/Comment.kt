package com.example.aps.Model.GenericComunidade


data class Comment(
    val id: String? = null, // DeixeI como nulo para inserção no Supabase (se o Supabase gerar o ID)
    val user_id: String,
    val line_id: String,    // Ex: "Linha Rubi"
    val content: String,
    val created_at: String? = null // Gerado pelo bd
)