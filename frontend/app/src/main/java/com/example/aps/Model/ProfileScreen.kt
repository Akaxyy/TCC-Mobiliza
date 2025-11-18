package com.example.aps.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.SwitchAccount
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.aps.R
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import com.example.aps.ViewModel.ProfileViewModel

// IMPORT NECESSÁRIO PARA O MODO ESCURO
import com.example.aps.DarkTheme.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    textSizeFactor: Float,
    onTextSizeChange: (Float) -> Unit,
    profileViewModel: ProfileViewModel = viewModel(),
    settingsViewModel: SettingsViewModel
) {
    val context = LocalContext.current
    val userProfile by profileViewModel.userProfile.collectAsState(initial = null)

    // Lendo o estado do DataStore para o Modo Escuro
    val isDarkMode by settingsViewModel.getDarkModeState(context).collectAsState(initial = false)


    LaunchedEffect(Unit) {
        profileViewModel.fetchUserProfile(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meu Perfil", fontWeight = FontWeight.Bold, fontSize = 20.sp * textSizeFactor) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary, // Cor Primária
                    titleContentColor = MaterialTheme.colorScheme.onPrimary // Texto Branco/OnPrimary
                )
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // --- 1. Informações Básicas do Usuário (Header Limpo) ---
            item {
                userProfile?.let { user ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp, 16.dp, 24.dp, 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = user.photoUrl,
                                // Fallback para ícone local caso a URL falhe
                                error = painterResource(id = R.drawable.ic_launcher_foreground)
                            ),
                            contentDescription = "Foto de Perfil",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(4.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = user.displayName ?: "Nome de Usuário",
                            fontSize = 24.sp * textSizeFactor,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = user.email ?: "email@exemplo.com",
                            fontSize = 16.sp * textSizeFactor,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // --- 2. Configurações ---
            item {
                SectionContainer(title = "Configurações", textSizeFactor = textSizeFactor) {

                    // ❌ Opção de Notificações removida daqui.

                    // Opção 1: Modo Escuro
                    ProfileOption(
                        icon = Icons.Default.SwitchAccount, // Usando SwitchAccount como ícone representativo
                        title = "Modo Escuro",
                        textSizeFactor = textSizeFactor,
                        trailingContent = {
                            Switch(
                                checked = isDarkMode,
                                onCheckedChange = { isChecked ->
                                    settingsViewModel.setDarkMode(context, isChecked) // Salva o novo estado
                                }
                            )
                        }
                    )
                    Divider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.8.dp, modifier = Modifier.padding(horizontal = 16.dp))

                    // Opção 2: Acessibilidade (com o controle de texto)
                    ProfileOption(
                        icon = Icons.Default.ArrowForwardIos, // Reutilizando um ícone simples
                        title = "Acessibilidade",
                        textSizeFactor = textSizeFactor,
                        trailingContent = {
                            AccessibilityOptions(
                                onTextSizeChange = onTextSizeChange
                            )
                        }
                    )
                }
            }

            // --- 3. Logout ---
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        profileViewModel.signOut(context) {
                            // Navega para a tela de login
                            navController.navigate("login_screen") {
                                popUpTo("splash_screen") { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Sair",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Sair da Conta",
                        fontSize = 16.sp * textSizeFactor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun SectionContainer(title: String, textSizeFactor: Float, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            fontSize = 18.sp * textSizeFactor,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
        )
        // O conteúdo das opções é passado aqui
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                // Usa a cor surfaceVariant que funciona bem em ambos os temas
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            content()
        }
    }
}

// Componente para controlar o tamanho do texto
@Composable
fun AccessibilityOptions(
    onTextSizeChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    // Usando 1.0f como valor inicial padrão (sempre) para Compose/View Models
    var currentTextSize by remember { mutableStateOf(1.0f) }

    Row(
        modifier = modifier.wrapContentWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Botão para diminuir
        IconButton(onClick = {
            // Limite inferior para o fator de escala de texto
            if (currentTextSize > 0.8f) {
                currentTextSize -= 0.2f
                onTextSizeChange(currentTextSize)
            }
        }, modifier = Modifier.size(36.dp)) {
            Icon(imageVector = Icons.Default.Remove, contentDescription = "Diminuir tamanho do texto")
        }

        // Indicador de tamanho atual
        Text(
            text = "Aa",
            fontSize = 18.sp * currentTextSize,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        // Botão para aumentar
        IconButton(onClick = {
            // Limite superior para o fator de escala de texto
            if (currentTextSize < 1.4f) {
                currentTextSize += 0.2f
                onTextSizeChange(currentTextSize)
            }
        }, modifier = Modifier.size(36.dp)) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Aumentar tamanho do texto")
        }
    }
}

// Componente de Opção de Perfil
@Composable
fun ProfileOption(
    icon: ImageVector,
    title: String,
    textSizeFactor: Float,
    onClick: (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                fontSize = 16.sp * textSizeFactor,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
        }
        if (trailingContent != null) {
            trailingContent()
        } else if (onClick != null) {
            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = "Avançar",
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.size(14.dp)
            )
        }
    }
}