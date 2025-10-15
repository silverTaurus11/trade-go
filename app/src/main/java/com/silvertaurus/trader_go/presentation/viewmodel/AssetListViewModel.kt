package com.silvertaurus.trader_go.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.silvertaurus.trader_go.domain.model.Asset
import com.silvertaurus.trader_go.domain.usecase.interfaces.IGetAssetsUseCase
import com.silvertaurus.trader_go.domain.usecase.interfaces.IObservePriceUpdatesUseCase
import com.silvertaurus.trader_go.domain.utils.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssetListViewModel @Inject constructor(
    private val getAssetsUseCase: IGetAssetsUseCase,
    private val observePriceUpdatesUseCase: IObservePriceUpdatesUseCase,
    private val dispatcher: DispatcherProvider
) : ViewModel() {
    private val _assetsFlow = MutableStateFlow<PagingData<Asset>>(PagingData.empty())
    val assetsFlow = _assetsFlow.asStateFlow()

    init {
        loadAssets()
        startObservingPrices()
    }

    private fun loadAssets() = viewModelScope.launch(dispatcher.io) {
        getAssetsUseCase()
            .cachedIn(viewModelScope)
            .collect { pagingData ->
                _assetsFlow.value = pagingData
            }
    }

    private fun startObservingPrices() = viewModelScope.launch(dispatcher.io) {
        observePriceUpdatesUseCase().collect { prices ->
            _assetsFlow.update { current ->
                current.map { asset ->
                    prices[asset.id]?.let { updatedPrice ->
                        asset.copy(priceUsd = updatedPrice)
                    } ?: asset
                }
            }
        }
    }
}

