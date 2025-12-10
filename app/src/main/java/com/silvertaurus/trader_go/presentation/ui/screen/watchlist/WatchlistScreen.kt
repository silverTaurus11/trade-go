package com.silvertaurus.trader_go.presentation.ui.screen.watchlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.silvertaurus.trader_go.R
import com.silvertaurus.trader_go.presentation.navigation.Screen
import com.silvertaurus.trader_go.presentation.ui.component.AssetItem
import com.silvertaurus.trader_go.presentation.ui.component.EmptyView
import com.silvertaurus.trader_go.presentation.ui.component.ErrorView
import com.silvertaurus.trader_go.presentation.ui.component.LoadingView
import com.silvertaurus.trader_go.presentation.viewmodel.WatchlistViewModel

@Composable
fun WatchlistScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: WatchlistViewModel = hiltViewModel()
) {
    val watchlist = viewModel.watchlistFlow.collectAsLazyPagingItems()
    val watchlistIds by viewModel.watchlistIds.collectAsStateWithLifecycle()

    when (watchlist.loadState.refresh) {
        is LoadState.Loading -> {
            LoadingView()
        }

        is LoadState.Error -> {
            val e = watchlist.loadState.refresh as LoadState.Error
            ErrorView(
                message = e.error.message ?: "Failed to load watchlist",
                onRetry = { watchlist.retry() }
            )
        }

        else -> {
            if (watchlist.itemCount == 0) {
                EmptyView(stringResource(R.string.empty_watchlist))
            } else {
                LazyColumn(
                    modifier = modifier.fillMaxSize()
                ) {
                    items(
                        count = watchlist.itemCount,
                        key = watchlist.itemKey { it.id }
                    ) { index ->
                        val asset = watchlist[index]
                        if (asset != null) {
                            val isWatched = asset.id in watchlistIds

                            AssetItem(
                                asset = asset,
                                onClick = {
                                    navController.navigate(Screen.Detail.createRoute(asset.id))
                                },
                                onWatchToggle = { id ->
                                    viewModel.toggleWatch(id)
                                },
                                isWatched = isWatched
                            )
                        } else {
                            AssetPlaceholderRow()
                        }
                    }

                    when (watchlist.loadState.append) {
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
                            val e = watchlist.loadState.append as LoadState.Error
                            item {
                                ErrorView(
                                    message = e.error.message ?: "Failed to load more",
                                    onRetry = { watchlist.retry() }
                                )
                            }
                        }

                        else -> Unit
                    }
                }
            }
        }
    }
}

@Composable
fun AssetPlaceholderRow() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

