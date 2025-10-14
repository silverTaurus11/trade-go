package com.silvertaurus.trader_go.presentation.ui.screen.detail

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.silvertaurus.trader_go.presentation.state.UiState
import com.silvertaurus.trader_go.presentation.ui.component.ChartViewLine
import com.silvertaurus.trader_go.presentation.ui.component.ErrorView
import com.silvertaurus.trader_go.presentation.ui.component.LoadingView
import com.silvertaurus.trader_go.presentation.ui.component.TimeRangeSelector
import com.silvertaurus.trader_go.presentation.ui.component.ToggleWatchList
import com.silvertaurus.trader_go.presentation.viewmodel.DetailViewModel
import com.silvertaurus.trader_go.presentation.viewmodel.WatchlistViewModel

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    assetId: String,
    navController: NavHostController,
    viewModel: DetailViewModel = hiltViewModel(),
    watchlistViewModel: WatchlistViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val watchlistIds by watchlistViewModel.watchlistIds.collectAsState()
    val isWatched = assetId in watchlistIds
    val livePrice by viewModel.livePrice.collectAsState()
    val selectedInterval by viewModel.selectedInterval.collectAsState()

    LaunchedEffect(assetId) {
        viewModel.loadWithInterval(assetId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(assetId.uppercase()) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                actions = {
                    ToggleWatchList(isWatched) {
                        watchlistViewModel.toggleWatch(assetId)
                    }
                },
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            livePrice?.let {
                Text(
                    text = "$${String.format("%.2f", it)}",
                    color = Color(0xFF00E676),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(Modifier.height(16.dp))

            TimeRangeSelector(
                selected = selectedInterval,
                onSelected = { interval ->
                    viewModel.setInterval(interval, assetId)
                }
            )

            Spacer(Modifier.height(8.dp))

            AnimatedContent(
                modifier = Modifier.fillMaxHeight(),
                targetState = state,
                transitionSpec = {
                    fadeIn(tween(250)) togetherWith fadeOut(tween(250))
                },
                label = "chartTransition"
            ) { ui ->
                when (ui) {
                    is UiState.Loading -> LoadingView()
                    is UiState.Success -> {
                        ChartViewLine(
                            prices = ui.data.map { it.close },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        )
                    }

                    is UiState.Error -> ErrorView(
                        message = ui.message,
                        onRetry = {
                            viewModel.loadWithInterval(assetId)
                        }
                    )
                }
            }
        }
    }
}
