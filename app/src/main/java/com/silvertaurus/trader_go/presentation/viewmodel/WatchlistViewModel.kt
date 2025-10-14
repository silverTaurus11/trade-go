package com.silvertaurus.trader_go.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silvertaurus.trader_go.domain.model.Asset
import com.silvertaurus.trader_go.domain.usecase.interfaces.IObservePriceUpdatesUseCase
import com.silvertaurus.trader_go.domain.usecase.interfaces.IObserveWatchlistUseCase
import com.silvertaurus.trader_go.domain.usecase.interfaces.IToggleWatchlistUseCase
import com.silvertaurus.trader_go.domain.utils.DispatcherProvider
import com.silvertaurus.trader_go.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val observeWatchlistUseCase: IObserveWatchlistUseCase,
    private val toggleWatchlistUseCase: IToggleWatchlistUseCase,
    private val observePriceUpdatesUseCase: IObservePriceUpdatesUseCase,
    private val dispatcher: DispatcherProvider
) : ViewModel() {
    private val _watchlistIds = MutableStateFlow<Set<String>>(emptySet())
    val watchlistIds: StateFlow<Set<String>> = _watchlistIds
    private val _uiState = MutableStateFlow<UiState<List<Asset>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getWatchList()
    }

    fun toggleWatch(assetId: String) =
        viewModelScope.launch(dispatcher.io) {
            toggleWatchlistUseCase(assetId)
            _watchlistIds.update { ids ->
                if (assetId in ids) ids - assetId else ids + assetId
            }
        }

    fun onRetry() {
        getWatchList()
    }

    private fun getWatchList() = viewModelScope.launch(dispatcher.io) {
        observeWatchlistUseCase().collect { list ->
            withContext(dispatcher.main) {
                _uiState.value = UiState.Success(list)
                _watchlistIds.value = list.map { it.id }.toSet()
            }
            startObservingPrices()
        }
    }

    private fun startObservingPrices() = viewModelScope.launch(dispatcher.io) {
        observePriceUpdatesUseCase().collect { prices ->
            val current = (_uiState.value as? UiState.Success)?.data ?: return@collect
            val updated = current.map { asset ->
                prices[asset.id]?.let { asset.copy(priceUsd = it) } ?: asset
            }
            withContext(dispatcher.main) {
                _uiState.value = UiState.Success(updated)
            }
        }
    }
}

