package com.silvertaurus.trader_go.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.silvertaurus.trader_go.presentation.ui.screen.dashboard.DashboardScreen
import com.silvertaurus.trader_go.presentation.ui.screen.detail.DetailScreen
import com.silvertaurus.trader_go.presentation.ui.screen.splash.SplashScreen

@Composable
fun CryptoNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(onNavigateHome = {
                navController.navigate(Screen.Dashboard.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(navController)
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("assetId") { type = NavType.StringType })
        ) { backStackEntry ->
            val assetId = backStackEntry.arguments?.getString("assetId") ?: ""
            DetailScreen(assetId = assetId, navController = navController)
        }
    }
}
