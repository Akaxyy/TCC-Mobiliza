package com.example.aps.Screens

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme // NOVO IMPORT: Para usar as cores do tema
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults // NOVO IMPORT: Para customizar as cores do item
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.aps.R

// Classe para representar cada item da barra de navegação
data class BottomNavigationItem(
    val title: String,
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val route: String
)

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavigationItem(
            title = "Linhas",
            selectedIcon = R.drawable.trem_icon,
            unselectedIcon = R.drawable.trem_icon,
            route = "home"
        ),
        BottomNavigationItem(
            title = "Comunidade",
            selectedIcon = R.drawable.speak_icon,
            unselectedIcon = R.drawable.speak_icon,
            route = "comunidade"
        ),
        BottomNavigationItem(
            title = "Noticias",
            selectedIcon = R.drawable.speaker_icon,
            unselectedIcon = R.drawable.speaker_icon,
            route = "noticias"
        ),
        BottomNavigationItem(
            title = "Perfil",
            selectedIcon = R.drawable.usericon,
            unselectedIcon = R.drawable.usericon,
            route = "perfil"
        )
    )

    NavigationBar(
        modifier = Modifier,
        // CORRIGIDO: Usa a cor de superfície do tema, que se adapta ao modo escuro
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { item ->
            AddItem(
                item = item,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    item: BottomNavigationItem,
    currentDestination: NavDestination?,
    navController: NavController
) {
    val isSelected = currentDestination?.hierarchy?.any { it.route == item.route } == true

    NavigationBarItem(
        label = {
            Text(
                text = item.title,
                fontSize = 12.sp,
                // O itemColors cuida da cor, mas garantimos o uso de uma cor base do tema.
                // Não precisa de 'color = Color.Black' fixo.
            )
        },
        icon = {
            Icon(
                painter = painterResource(id = if (isSelected) item.selectedIcon else item.unselectedIcon),
                contentDescription = item.title,
                modifier = Modifier.size(28.dp),
                // 🔑 CORREÇÃO CHAVE: Força o ícone a usar a cor original, evitando o cinza
                tint = Color.Unspecified
            )
        },
        selected = isSelected,
        onClick = {
            navController.navigate(item.route) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        },
        // CORRIGIDO: Usa NavigationBarItemDefaults para cores que se adaptam ao tema
        colors = NavigationBarItemDefaults.colors(
            // Cor do fundo da bolha do item selecionado
            indicatorColor = MaterialTheme.colorScheme.primaryContainer,

            // Ícones e texto selecionados usam cores fortes do tema
            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
            selectedTextColor = MaterialTheme.colorScheme.onSurface,

            // Ícones e texto não selecionados usam cores mais suaves do tema
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}