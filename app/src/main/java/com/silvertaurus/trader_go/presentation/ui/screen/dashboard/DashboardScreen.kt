package com.silvertaurus.trader_go.presentation.ui.screen.dashboard

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.silvertaurus.trader_go.R
import com.silvertaurus.trader_go.presentation.ui.screen.home.HomeScreen
import com.silvertaurus.trader_go.presentation.ui.screen.watchlist.WatchlistScreen

enum class DashboardTab { HOME, WATCHLIST }

@Composable
fun DashboardScreen(
    navController: NavHostController
) {
    var selectedTab by rememberSaveable { mutableStateOf(DashboardTab.HOME) }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                NavigationBarItem(
                    selected = selectedTab == DashboardTab.HOME,
                    onClick = { selectedTab = DashboardTab.HOME },
                    icon = { Icon(Icons.Default.Home, contentDescription = "home_tab") },
                    label = { Text(stringResource(R.string.nav_home)) }
                )
                NavigationBarItem(
                    selected = selectedTab == DashboardTab.WATCHLIST,
                    onClick = { selectedTab = DashboardTab.WATCHLIST },
                    icon = { Icon(Icons.Default.Star, contentDescription = "watchlist_tab") },
                    label = { Text(stringResource(R.string.nav_watchlist)) }
                )
            }
        }
    ) { padding ->
        when (selectedTab) {
            DashboardTab.HOME -> HomeScreen(
                navController,
                Modifier.padding(padding)
            )

            DashboardTab.WATCHLIST -> WatchlistScreen(
                navController,
                Modifier.padding(padding)
            )
        }
    }
}
