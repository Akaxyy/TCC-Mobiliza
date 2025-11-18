package com.example.aps.Screens
import androidx.lifecycle.viewmodel.compose.viewModel
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.aps.ViewModel.ProfileViewModel

// IMPORT NECESSÁRIO PARA O TEMA ESCURO (SettingsViewModel)
import com.example.aps.DarkTheme.SettingsViewModel

// IMPORTES DOS WRAPPERS
import com.example.aps.Model.GenericComunidade.CommunityWrapper
import com.example.aps.Model.GenericScreenEstacoes.EstacoesWrapper
import com.example.aps.Model.GenericNoticias.LineUpdatesWrapper
import com.example.aps.Routes.Routes

// --- Imports das Telas
import com.example.aps.Screens.Comunidades.LinhaAmarelaComunidadeScreen
import com.example.aps.Screens.NoticiasScreens.LinhaAmarelaNoticiasScreen
import com.example.aps.Screens.NoticiasScreens.LinhaAzulNoticiasScreen
import com.example.aps.Screens.NoticiasScreens.LinhaCoralNoticiasScreen
import com.example.aps.Screens.NoticiasScreens.LinhaDiamanteNoticiasScreen
import com.example.aps.Screens.NoticiasScreens.LinhaEsmeraldaNoticiasScreen
import com.example.aps.Screens.NoticiasScreens.LinhaPrataNoticiasScreen
import com.example.aps.Screens.NoticiasScreens.LinhaRubiNoticiasScreen
import com.example.aps.Screens.NoticiasScreens.LinhaSafiraNoticiasScreen
import com.example.aps.Screens.NoticiasScreens.LinhaTurquesaNoticiasScreen
import com.example.aps.Screens.NoticiasScreens.LinhaVerdeNoticiasScreen
import com.example.aps.Screens.NoticiasScreens.LinhaVermelhaNoticiasScreen
// AppNavigation.kt



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navgation(
    textSizeFactor: Float,
    onTextSizeChange: (Float) -> Unit,
    settingsViewModel: SettingsViewModel
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME, // Usando a constante
            modifier = Modifier.padding(paddingValues)
        ) {
            // Rotas Principais
            composable(Routes.HOME) { HomeScreen(navController, textSizeFactor, onTextSizeChange) }
            composable(Routes.COMUNIDADE) { ComunidadeScreen(navController, textSizeFactor, onTextSizeChange) }
            composable(Routes.NOTICIAS) { NoticiasScreen(navController, textSizeFactor, onTextSizeChange) }
            composable(Routes.FAVORITOS) { FavoritesScreen(navController, textSizeFactor, onTextSizeChange) }

            // Rota Perfil
            composable(Routes.PERFIL) {
                ProfileScreen(
                    navController = navController,
                    textSizeFactor = textSizeFactor,
                    onTextSizeChange = onTextSizeChange,
                    settingsViewModel = settingsViewModel
                )
            }

            // Rotas Secundárias
            composable (Routes.MAPA_SCREEN){ MapaScreen(navController, textSizeFactor, onTextSizeChange) }
            composable (Routes.VIAGENS_SCREEN){ ViagensScreen(navController, textSizeFactor, onTextSizeChange) }
            composable (Routes.ESTACOES_SCREEN){ EstacoesScreen(navController, textSizeFactor, onTextSizeChange) }
            composable (Routes.SUPORTE_SCREEN){ SuporteScreen(navController, textSizeFactor, onTextSizeChange) }

            composable (Routes.CANAIS_ATENDIMENTO){ CanaisAtendimentoScreen(navController, textSizeFactor, onTextSizeChange) }
            composable (Routes.DICAS_SUPORTE){ SuporteDicasScreen(navController, textSizeFactor, onTextSizeChange) }

            // -------------------------------------------------------------------
            // --- ROTA GENÉRICA PARA COMUNIDADE ---
            // -------------------------------------------------------------------
            composable(
                route = Routes.COMMUNITY_DETAIL,
                arguments = listOf(
                    navArgument("nomeDaLinha") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val nomeDaLinha = backStackEntry.arguments?.getString("nomeDaLinha") ?: "Linha Não Mapeada"

                CommunityWrapper(
                    navController = navController,
                    nomeDaLinha = nomeDaLinha,
                    textSizeFactor = textSizeFactor
                )
            }
            // -------------------------------------------------------------------

            // -------------------------------------------------------------------
            // --- ROTA GENÉRICA PARA ESTAÇÕES ---
            // -------------------------------------------------------------------
            composable(
                route = Routes.ESTACOES_WRAPPER,
                arguments = listOf(
                    navArgument("nomeDaLinha") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val nomeDaLinha = backStackEntry.arguments?.getString("nomeDaLinha") ?: "Linha Não Mapeada"

                EstacoesWrapper(
                    navController = navController,
                    nomeDaLinha = nomeDaLinha,
                    textSizeFactor = textSizeFactor
                )
            }
            // -------------------------------------------------------------------

            // -------------------------------------------------------------------
            // --- NOVA ROTA PARA STATUS DE LINHA (USANDO O NOVO WRAPPER) ---
            // Esta rota substitui todas as rotas estáticas de notícias.
            // -------------------------------------------------------------------
            composable(
                route = Routes.LINE_UPDATES_SCREEN,
                arguments = listOf(
                    navArgument("lineId") { type = NavType.IntType },
                    navArgument("lineName") { type = NavType.StringType },
                    navArgument("corPrincipalHex") { type = NavType.StringType },
                    navArgument("textSizeFactor") { type = NavType.FloatType }
                )
            ) { backStackEntry ->
                val lineId = backStackEntry.arguments?.getInt("lineId") ?: 0
                val lineName = backStackEntry.arguments?.getString("lineName") ?: "Linha Desconhecida"
                val corPrincipalHex = backStackEntry.arguments?.getString("corPrincipalHex") ?: "#FFFFFF"
                val textSizeFactor = backStackEntry.arguments?.getFloat("textSizeFactor") ?: 1.0f

                // Chama o Wrapper que lida com a lógica de API e ViewModel
                LineUpdatesWrapper(
                    lineId = lineId,
                    lineName = lineName,
                    corPrincipalHex = corPrincipalHex,
                    textSizeFactor = textSizeFactor
                )
            }

        }
    }
}


@Composable
fun FavoritesScreen(navController: NavHostController, textSizeFactor: Float, onTextSizeChange: (Float) -> Unit) { /* Implementação */ }

