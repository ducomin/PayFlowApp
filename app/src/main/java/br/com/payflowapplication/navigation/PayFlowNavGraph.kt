package br.com.payflowapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.payflowapplication.view.screens.CadastroAssinaturaScreen

@Composable
fun PayFlowNavGraph(startDestination: String = Routes.CADASTRO_ASSINATURA) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {

        // New subscription
        composable(Routes.CADASTRO_ASSINATURA) {
            CadastroAssinaturaScreen(
                onNavigateBack = { navController.popBackStack() },
                onSalvoComSucesso = { navController.popBackStack() }
            )
        }

        // Edit existing subscription
        composable(
            route = Routes.CADASTRO_ASSINATURA_EDIT,
            arguments = listOf(navArgument("assinaturaId") { type = NavType.LongType })
        ) {
            CadastroAssinaturaScreen(
                onNavigateBack = { navController.popBackStack() },
                onSalvoComSucesso = { navController.popBackStack() }
            )
        }
    }
}

