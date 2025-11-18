package com.example.aps.Screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.* // <-- Importe tudo do Material3
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

enum class ContactType { PHONE, EMAIL }

data class ContactInfo(
    val name: String,
    val contact: String, // O número ou email real
    val description: String,
    val type: ContactType,
    val icon: ImageVector? = null // Ícone para contato (ex: telefone, email)
)

data class ContactCategory(
    val title: String,
    val contacts: List<ContactInfo>
)

// --- Dados de Contato ---
val contactCategories = listOf(
    ContactCategory(
        title = "Transporte e Mobilidade",
        contacts = listOf(
            ContactInfo(
                name = "CPTM – Central de Atendimento",
                contact = "0800 055 0121",
                description = "Informações sobre horários, ocorrências nas linhas, objetos perdidos, etc.",
                type = ContactType.PHONE,
                icon = Icons.Default.Call
            ),
            ContactInfo(
                name = "Metrô de São Paulo – Ouvidoria",
                contact = "0800 770 7722",
                description = "Para elogios, sugestões ou reclamações sobre o serviço do metrô.",
                type = ContactType.PHONE,
                icon = Icons.Default.Call
            ),
            ContactInfo(
                name = "SPTrans – Bilhete Único / Ônibus",
                contact = "156",
                description = "Atendimento sobre integração ônibus-trem, saldo do cartão, dúvidas.",
                type = ContactType.PHONE,
                icon = Icons.Default.Call
            )
        )
    ),
    ContactCategory(
        title = "Emergências e Segurança Pública",
        contacts = listOf(
            ContactInfo(
                name = "Polícia Militar (emergências gerais)",
                contact = "190",
                description = "Para situações de violência, furtos, ameaças nas estações ou trens.",
                type = ContactType.PHONE,
                icon = Icons.Default.Phone
            ),
            ContactInfo(
                name = "Guarda Civil Metropolitana",
                contact = "153",
                description = "Apoio em terminais e áreas urbanas da cidade.",
                type = ContactType.PHONE,
                icon = Icons.Default.Phone
            ),
            ContactInfo(
                name = "Corpo de Bombeiros",
                contact = "193",
                description = "Incêndios, acidentes e atendimentos de resgate.",
                type = ContactType.PHONE,
                icon = Icons.Default.Phone
            ),
            ContactInfo(
                name = "SAMU – Serviço de Atendimento Móvel de Urgência",
                contact = "192",
                description = "Atendimento médico de urgência em estações ou vias públicas.",
                type = ContactType.PHONE,
                icon = Icons.Default.Phone
            )
        )
    ),
    ContactCategory(
        title = "Acessibilidade e Direitos",
        contacts = listOf(
            ContactInfo(
                name = "Secretaria Municipal da Pessoa com Deficiência",
                contact = "156",
                description = "Atendimento sobre acessibilidade nas estações e direitos da pessoa com deficiência.",
                type = ContactType.PHONE,
                icon = Icons.Default.Phone
            )
        )
    ),
    ContactCategory(
        title = "Contato do Aplicativo (opcional)",
        contacts = listOf(
            ContactInfo(
                name = "Suporte do Aplicativo",
                contact = "mobilizajuda@gmail.com",
                description = "Para dúvidas técnicas, erros no app ou envio de sugestões.",
                type = ContactType.EMAIL,
                icon = Icons.Default.Email
            ),
            ContactInfo(
                name = "Atendimento WhatsApp (fictício)",
                contact = "(11)96444-5563",
                description = "Para dúvidas técnicas, erros no app ou envio de sugestões via WhatsApp.",
                type = ContactType.PHONE,
                icon = Icons.Default.Call
            )
        )
    )
)

@Composable
fun ContactItem(contact: ContactInfo, context: Context) {
    val contactAction: () -> Unit = {
        when (contact.type) {
            ContactType.PHONE -> {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${contact.contact.replace(" ", "").replace("-", "").replace("(", "").replace(")", "")}")
                }
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                }
            }
            ContactType.EMAIL -> {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(contact.contact))
                    putExtra(Intent.EXTRA_SUBJECT, "Contato via App de Mobilidade")
                }
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = contactAction)
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            contact.icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = Color.DarkGray,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = contact.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = contact.contact,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0057B8)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = contact.description,
            fontSize = 13.sp,
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun CanaisAtendimentoTelefonicoScreen(navController: NavHostController) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Canais de Atendimento e Suporte",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        contactCategories.forEachIndexed { catIndex, category ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = category.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    category.contacts.forEachIndexed { contactIndex, contact ->
                        ContactItem(contact = contact, context = context)
                        if (contactIndex < category.contacts.lastIndex) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Divider( // <-- Usando o Divider do Material3
                                color = Color.LightGray.copy(alpha = 0.5f),
                                thickness = 0.8.dp,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

// --- Preview da Tela de Canais de Atendimento ---
@Preview(showBackground = true, backgroundColor = 0xFFF0F0F0)
@Composable
fun CanaisAtendimentoTelefonicoScreenPreview() {
    val navController = rememberNavController()
    CanaisAtendimentoTelefonicoScreen(navController = navController)
}