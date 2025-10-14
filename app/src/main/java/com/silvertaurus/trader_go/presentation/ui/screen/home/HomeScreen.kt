package com.silvertaurus.trader_go.presentation.ui.screen.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.silvertaurus.trader_go.domain.model.Asset
import com.silvertaurus.trader_go.presentation.navigation.Screen
import com.silvertaurus.trader_go.presentation.state.UiState
import com.silvertaurus.trader_go.presentation.ui.component.AssetItem
import com.silvertaurus.trader_go.presentation.ui.component.ErrorView
import com.silvertaurus.trader_go.presentation.ui.component.LoadingView
import com.silvertaurus.trader_go.presentation.viewmodel.AssetListViewModel
import com.silvertaurus.trader_go.presentation.viewmodel.WatchlistViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: AssetListViewModel,
    watchlistViewModel: WatchlistViewModel
) {
    val state by viewModel.uiState.collectAsState()

    when (state) {
        is UiState.Loading -> LoadingView()
        is UiState.Success -> {
            val assets = (state as UiState.Success<List<Asset>>).data
            LazyColumn(modifier = modifier.fillMaxSize()) {
                items(items = assets, key = { it.id }) { asset ->
                    val watchlistIds by watchlistViewModel.watchlistIds.collectAsState()
                    val isWatched = asset.id in watchlistIds

                    AssetItem(
                        asset = asset,
                        onClick = {
                            navController.navigate(Screen.Detail.createRoute(asset.id))
                        }, onWatchToggle = {
                            watchlistViewModel.toggleWatch(asset.id)
                        },
                        isWatched = isWatched
                    )
                }
            }
        }

        is UiState.Error -> {
            ErrorView(
                message = (state as UiState.Error).message,
                onRetry = {
                    viewModel.onRetry()
                }
            )
        }
    }
}

