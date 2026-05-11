package com.silvertaurus.trader_go.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Dashboard : Screen("dashboard")
    object Detail : Screen("detail/{assetId}") {
        fun createRoute(assetId: String) = "detail/$assetId"
    }
}

sealed class Graph(val name: String) {
    object Main : Graph("main_graph")
}