package com.example.aps.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect // Import para configurar a barra de status
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat // Import para a barra de status

// IMPORTANTE: Suas cores primárias, secundárias e terciárias devem ser definidas
// no arquivo Colors.kt. Estou assumindo que Purple80, Purple40, etc., estão lá.

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Color(0xFF1C1B1F), // Cor de fundo escura explícita
    surface = Color(0xFF1C1B1F),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = Color(0xFFFFFBFE), // Cor de fundo clara explícita
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
)

@Composable
fun APSTheme(
    // Este valor é o que vem do seu SettingsViewModel na MainActivity
    darkTheme: Boolean = isSystemInDarkTheme(),

    // Mantemos o dynamicColor, mas vamos priorizar o 'darkTheme' manual
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // Lógica de seleção de cores APLICADA:

        // 1. Se dynamicColor for TRUE e o dispositivo for S+, usa a cor dinâmica
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            // A cor dinâmica usa a preferência do usuário (darkTheme)
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        // 2. Se a cor dinâmica for FALSE ou o dispositivo for antigo,
        //    usa o nosso esquema de cores manual (DarkColorScheme ou LightColorScheme)
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // --- Configuração da Barra de Status (System UI) ---
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            // 1. Define a cor da Barra de Status para a cor primária do tema
            window.statusBarColor = colorScheme.primary.toArgb()

            // 2. Define o estilo dos ícones da barra de status (ícones escuros no tema claro)
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}