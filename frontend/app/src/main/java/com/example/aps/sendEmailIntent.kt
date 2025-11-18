package com.example.aps

// src/main/java/com/example/aps/Utils/EmailUtils.kt (Crie essa pasta se não existir)

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import android.util.Log

// ⚠️ SUBSTITUA PELO SEU E-MAIL DE SUPORTE REAL
const val EMAIL_DE_SUPORTE = "mobilizaajuda@gmail.com"

/**
 * Dispara uma Intent para abrir o cliente de e-mail do dispositivo com o assunto e corpo preenchidos.
 */
fun sendEmailIntent(context: Context, to: String, subject: String, body: String? = null) {
    try {
        // Constrói a URI, codificando Assunto e Corpo
        val uriText = "mailto:$to?subject=${Uri.encode(subject)}" +
                if (body != null) "&body=${Uri.encode(body)}" else ""

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse(uriText)
        }
        // Tenta iniciar a atividade (abre o app de e-mail)
        context.startActivity(intent)
    } catch (e: Exception) {
        // Mensagem de erro se nenhum aplicativo de e-mail for encontrado
        Toast.makeText(context, "Nenhum aplicativo de e-mail encontrado.", Toast.LENGTH_LONG).show()
        Log.e("EMAIL_INTENT", "Erro ao abrir cliente de e-mail: ${e.message}")
    }
}