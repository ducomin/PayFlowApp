package br.com.payflowapplication.view.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import br.com.payflowapplication.ui.theme.*

/** Active bottom navigation tab */
enum class NavTab { HOME, HISTORICO, AVISOS, PERFIL }

/**
 * MD3 NavigationBar with pill indicator on active item and badge support on AVISOS.
 */
@Composable
fun PayFlowNavBar(
    selected: NavTab,
    onSelect: (NavTab) -> Unit,
    avisosBadge: Int = 0,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = 0.dp
    ) {
        NavTab.entries.forEach { tab ->
            val isActive = tab == selected
            NavigationBarItem(
                selected = isActive,
                onClick = { onSelect(tab) },
                icon = {
                    BadgedBox(
                        badge = {
                            if (tab == NavTab.AVISOS && avisosBadge > 0) {
                                Badge { Text(avisosBadge.toString()) }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = tabIcon(tab),
                            contentDescription = tabLabel(tab)
                        )
                    }
                },
                label = { Text(tabLabel(tab)) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            )
        }
    }
}

private fun tabLabel(tab: NavTab) = when (tab) {
    NavTab.HOME      -> "Home"
    NavTab.HISTORICO -> "Histórico"
    NavTab.AVISOS    -> "Avisos"
    NavTab.PERFIL    -> "Perfil"
}

private fun tabIcon(tab: NavTab): ImageVector = when (tab) {
    NavTab.HOME      -> Icons.Default.Home
    NavTab.HISTORICO -> Icons.AutoMirrored.Filled.List
    NavTab.AVISOS    -> Icons.Default.Notifications
    NavTab.PERFIL    -> Icons.Default.Person
}




