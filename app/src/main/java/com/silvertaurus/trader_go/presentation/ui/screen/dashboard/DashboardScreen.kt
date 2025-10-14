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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.silvertaurus.trader_go.R
import com.silvertaurus.trader_go.presentation.ui.screen.home.HomeScreen
import com.silvertaurus.trader_go.presentation.ui.screen.watchlist.WatchlistScreen
import com.silvertaurus.trader_go.presentation.viewmodel.AssetListViewModel
import com.silvertaurus.trader_go.presentation.viewmodel.WatchlistViewModel

@Composable
fun DashboardScreen(
    navController: NavHostController,
    assetListViewModel: AssetListViewModel = hiltViewModel(),
    watchlistViewModel: WatchlistViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text(stringResource(R.string.nav_home)) }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Star, contentDescription = null) },
                    label = { Text(stringResource(R.string.nav_watchlist)) }
                )
            }
        }
    ) { padding ->
        when (selectedTab) {
            0 -> HomeScreen(
                navController,
                Modifier.padding(padding),
                assetListViewModel,
                watchlistViewModel
            )

            1 -> WatchlistScreen(
                navController,
                Modifier.padding(padding),
                watchlistViewModel
            )
        }
    }
}
