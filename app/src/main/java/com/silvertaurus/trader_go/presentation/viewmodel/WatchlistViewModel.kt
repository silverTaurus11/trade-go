package com.silvertaurus.trader_go.presentation.viewmodel

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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val observeWatchlistPagingUseCase: IObserveWatchlistUseCase,
    private val toggleWatchlistUseCase: IToggleWatchlistUseCase,
    private val observePriceUpdatesUseCase: IObservePriceUpdatesUseCase,
    private val dispatcher: DispatcherProvider
) : ViewModel() {

    private val _watchlistIds = MutableStateFlow<Set<String>>(emptySet())
    val watchlistIds: StateFlow<Set<String>> = _watchlistIds

    private val _watchlistFlow = MutableStateFlow<PagingData<Asset>>(PagingData.empty())
    val watchlistFlow = _watchlistFlow.asStateFlow()

    private var loadWatchListJob: Job? = null
    private var loadWatchListIdsJob: Job? = null

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

    private fun loadWatchlist() {
        if (loadWatchListJob?.isActive == true) loadWatchListJob?.cancel()

        loadWatchListJob = viewModelScope.launch(dispatcher.io) {
            observeWatchlistPagingUseCase()
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _watchlistFlow.value = pagingData
                }
        }
    }

    private fun loadWatchlistIds() {
        if (loadWatchListIdsJob?.isActive == true) loadWatchListIdsJob?.cancel()

        loadWatchListIdsJob = viewModelScope.launch(dispatcher.io) {
            observeWatchlistPagingUseCase
                .getWatchlistIds()
                .map { it.toSet() }
                .distinctUntilChanged()
                .collectLatest { ids ->
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

    override fun onCleared() {
        super.onCleared()
        if (loadWatchListIdsJob?.isActive == true) loadWatchListIdsJob?.cancel()
        if (loadWatchListJob?.isActive == true) loadWatchListJob?.cancel()
    }
}


