package com.silvertaurus.trader_go.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.silvertaurus.trader_go.presentation.ui.screen.dashboard.DashboardScreen
import com.silvertaurus.trader_go.presentation.ui.screen.detail.DetailScreen
import com.silvertaurus.trader_go.presentation.ui.screen.splash.SplashScreen
import com.silvertaurus.trader_go.presentation.viewmodel.WatchlistViewModel

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
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Dashboard.route)
            }
            val watchlistViewModel: WatchlistViewModel = hiltViewModel(parentEntry)

            DetailScreen(assetId = assetId, navController = navController, watchlistViewModel = watchlistViewModel)
        }
    }
}
