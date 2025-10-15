package com.silvertaurus.trader_go.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.silvertaurus.trader_go.domain.model.Asset
import com.silvertaurus.trader_go.domain.usecase.interfaces.IObservePriceUpdatesUseCase
import com.silvertaurus.trader_go.domain.usecase.interfaces.IObserveWatchlistUseCase
import com.silvertaurus.trader_go.domain.usecase.interfaces.IToggleWatchlistUseCase
import com.silvertaurus.trader_go.domain.utils.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val observeWatchlistPagingUseCase: IObserveWatchlistUseCase,
    private val toggleWatchlistUseCase: IToggleWatchlistUseCase,
    private val observePriceUpdatesUseCase: IObservePriceUpdatesUseCase,
    private val dispatcher: DispatcherProvider
) : ViewModel() {

    private val _watchlistIds = MutableStateFlow<List<String>>(emptyList())
    val watchlistIds: StateFlow<List<String>> = _watchlistIds

    private val _watchlistFlow = MutableStateFlow<PagingData<Asset>>(PagingData.empty())
    val watchlistFlow = _watchlistFlow.asStateFlow()

    init {
        loadWatchlistIds()
        loadWatchlist()
        startObservingPrices()
    }

    fun toggleWatch(assetId: String) = viewModelScope.launch(dispatcher.io) {
        toggleWatchlistUseCase(assetId)
        loadWatchlistIds()
        loadWatchlist()
    }

    private fun loadWatchlist() = viewModelScope.launch(dispatcher.io) {
        observeWatchlistPagingUseCase()
            .cachedIn(viewModelScope)
            .collectLatest { pagingData ->
                withContext(dispatcher.main) {
                    _watchlistFlow.value = pagingData
                }
            }
    }

    private fun loadWatchlistIds() = viewModelScope.launch(dispatcher.io) {
        observeWatchlistPagingUseCase.getWatchlistIds().collectLatest { ids ->
            withContext(dispatcher.main) {
                _watchlistIds.value = ids
            }
        }
    }

    private fun startObservingPrices() = viewModelScope.launch(dispatcher.io) {
        observePriceUpdatesUseCase().collect { prices ->
            _watchlistFlow.update { current ->
                current.map { asset ->
                    prices[asset.id]?.let { updatedPrice ->
                        asset.copy(priceUsd = updatedPrice)
                    } ?: asset
                }
            }
        }
    }
}


