package com.silvertaurus.trader_go.presentation.ui.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.silvertaurus.trader_go.presentation.navigation.Screen
import com.silvertaurus.trader_go.presentation.ui.component.AssetItem
import com.silvertaurus.trader_go.presentation.ui.component.ErrorView
import com.silvertaurus.trader_go.presentation.ui.component.LoadingView
import com.silvertaurus.trader_go.presentation.viewmodel.AssetListViewModel
import com.silvertaurus.trader_go.presentation.viewmodel.WatchlistViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: AssetListViewModel = hiltViewModel(),
    watchlistViewModel: WatchlistViewModel = hiltViewModel()
) {
    val assets = viewModel.assetsFlow.collectAsLazyPagingItems()
    val watchlistIds by watchlistViewModel.watchlistIds.collectAsState()

    when (assets.loadState.refresh) {
        is LoadState.Loading -> {
            LoadingView()
        }

        is LoadState.Error -> {
            val e = assets.loadState.refresh as LoadState.Error
            ErrorView(
                message = e.error.message ?: "Failed to load assets",
                onRetry = { assets.retry() }
            )
        }

        else -> {
            LazyColumn(
                modifier = modifier.fillMaxSize()
            ) {
                items(
                    count = assets.itemCount,
                    key = assets.itemKey { it.id }
                ) { index ->
                    val asset = assets[index]

                    if (asset != null) {
                        val isWatched = asset.id in watchlistIds

                        AssetItem(
                            asset = asset,
                            onClick = {
                                navController.navigate(Screen.Detail.createRoute(asset.id))
                            },
                            onWatchToggle = {
                                watchlistViewModel.toggleWatch(asset.id)
                            },
                            isWatched = isWatched
                        )
                    }
                }

                // 🔁 Paging append state
                when (assets.loadState.append) {
                    is LoadState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    is LoadState.Error -> {
                        val e = assets.loadState.append as LoadState.Error
                        item {
                            ErrorView(
                                message = e.error.message ?: "Failed to load more",
                                onRetry = { assets.retry() }
                            )
                        }
                    }

                    else -> Unit
                }
            }
        }
    }
}


