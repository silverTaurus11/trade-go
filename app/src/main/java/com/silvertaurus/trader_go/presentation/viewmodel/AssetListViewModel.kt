package com.silvertaurus.trader_go.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silvertaurus.trader_go.domain.model.Asset
import com.silvertaurus.trader_go.domain.usecase.interfaces.IGetAssetsUseCase
import com.silvertaurus.trader_go.domain.usecase.interfaces.IObservePriceUpdatesUseCase
import com.silvertaurus.trader_go.domain.utils.DispatcherProvider
import com.silvertaurus.trader_go.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AssetListViewModel @Inject constructor(
    private val getAssetsUseCase: IGetAssetsUseCase,
    private val observePriceUpdatesUseCase: IObservePriceUpdatesUseCase,
    private val dispatcher: DispatcherProvider
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<List<Asset>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadAssets()
    }

    fun onRetry() {
        loadAssets()
    }

    private fun loadAssets() = viewModelScope.launch(dispatcher.io) {
        try {
            val assets = getAssetsUseCase()
            withContext(dispatcher.main) {
                _uiState.value = UiState.Success(assets)
            }
            startObservingPrices()
        } catch (e: Exception) {
            _uiState.value = UiState.Error(e.message ?: "Failed to load assets")
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

