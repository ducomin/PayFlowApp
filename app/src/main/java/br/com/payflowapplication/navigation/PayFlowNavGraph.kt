package br.com.payflowapplication.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.payflowapplication.view.screens.CadastroAssinaturaScreen
import br.com.payflowapplication.view.screens.DetalheScreen
import br.com.payflowapplication.view.screens.SplashScreen
import br.com.payflowapplication.view.screens.HistoryScreen
import br.com.payflowapplication.view.screens.HomeDashboardScreen
import br.com.payflowapplication.view.screens.LoginScreen
import br.com.payflowapplication.view.screens.NotificacoesScreen
import br.com.payflowapplication.view.screens.ProfileScreen

@Composable
fun PayFlowNavGraph(darkTheme: Boolean,
                    onThemeChange: (Boolean) -> Unit,
                    startDestination: String = Routes.SPLASH) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {

        // ── Splash ───────────────────────────────────────────────────────────
        composable(
            route = Routes.SPLASH,
            exitTransition = { fadeOut(tween(300)) }
        ) {
            SplashScreen(
                onFinished = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        // ── Login ────────────────────────────────────────────────────────────
        composable(
            route = Routes.LOGIN,
            enterTransition = { fadeIn(tween(300)) },
            exitTransition = { fadeOut(tween(300)) }
        ) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // ── Home / Dashboard ─────────────────────────────────────────────────
        composable(
            route = Routes.HOME,
            enterTransition = {
                when (initialState.destination.route) {
                    Routes.HISTORICO    -> slideInHorizontally(tween(350)) { -it }
                    Routes.NOTIFICACOES -> slideInHorizontally(tween(350)) { it }
                    else                -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    Routes.HISTORICO    -> slideOutHorizontally(tween(350)) { -it }
                    Routes.NOTIFICACOES -> slideOutHorizontally(tween(350)) { it }
                    else                -> null
                }
            }
        ) {
            HomeDashboardScreen(
                onNovaAssinatura = { navController.navigate(Routes.CADASTRO_ASSINATURA) },
                onAssinaturaClick = { id -> navController.navigate(Routes.detalheRoute(id)) },
                onPerfilClick = { navController.navigate(Routes.PERFIL)},
                onNavigateToHistory = { navController.navigate(Routes.HISTORICO) },
                onNavigateToNotificacoes = { navController.navigate(Routes.NOTIFICACOES) }
            )
        }

        // ── History ────────────────────────────────────────────────────────────
        composable(
            route = Routes.HISTORICO,
            enterTransition = { slideInHorizontally(tween(350)) { -it } },
            exitTransition  = { slideOutHorizontally(tween(350)) { -it } }
        ) {
            HistoryScreen(
                onNavigateBack = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }

        // ── New subscription ─────────────────────────────────────────────────
        composable(Routes.CADASTRO_ASSINATURA) {
            CadastroAssinaturaScreen(
                onNavigateBack = { navController.popBackStack() },
                onSalvoComSucesso = { navController.popBackStack() }
            )
        }

        // ── Edit existing subscription ────────────────────────────────────────
        composable(
            route = Routes.CADASTRO_ASSINATURA_EDIT,
            arguments = listOf(navArgument("assinaturaId") { type = NavType.LongType })
        ) {
            CadastroAssinaturaScreen(
                onNavigateBack = { navController.popBackStack() },
                onSalvoComSucesso = { navController.popBackStack() }
            )
        }

        // ── Profile ───────────────────────────────────────────────────────────
        composable(Routes.PERFIL) {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                onLogoutClick = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                darkTheme = darkTheme,
                onThemeChange = onThemeChange
            )
        }

        // ── Detail ────────────────────────────────────────
        composable(
            route = Routes.DETALHE_ASSINATURA,
            arguments = listOf(navArgument("assinaturaId") { type = NavType.LongType })
        ) {
            DetalheScreen(
                onEditClick = { id -> navController.navigate(Routes.editRoute(id)) },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // ── Notificações ─────────────────────────────────────────────────────
        composable(
            route = Routes.NOTIFICACOES,
            enterTransition = { slideInHorizontally(tween(350)) { it } },
            exitTransition  = { slideOutHorizontally(tween(350)) { it } }
        ) {
            NotificacoesScreen(
                onNavigateBack = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                },
                onNavigateToHistory = { navController.navigate(Routes.HISTORICO) }
            )
        }
    }
}
