package com.silvertaurus.trader_go.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silvertaurus.trader_go.domain.model.Candle
import com.silvertaurus.trader_go.domain.model.CandleInterval
import com.silvertaurus.trader_go.domain.usecase.interfaces.IGetAssetHistoryUseCase
import com.silvertaurus.trader_go.domain.usecase.interfaces.IObservePriceUpdatesUseCase
import com.silvertaurus.trader_go.domain.utils.DispatcherProvider
import com.silvertaurus.trader_go.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getAssetHistoryUseCase: IGetAssetHistoryUseCase,
    private val observePriceUpdatesUseCase: IObservePriceUpdatesUseCase,
    private val dispatcher: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Candle>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _livePrice = MutableStateFlow<Double?>(null)
    val livePrice: StateFlow<Double?> = _livePrice

    private val _selectedInterval = MutableStateFlow(CandleInterval.ONE_HOUR)
    val selectedInterval: StateFlow<CandleInterval> = _selectedInterval

    private var baseCandles = listOf<Candle>()

    private var priceJob: Job? = null

    fun setInterval(interval: CandleInterval, assetId: String) {
        if (interval == _selectedInterval.value) return
        _selectedInterval.value = interval
        loadWithInterval(assetId)
    }

    fun loadWithInterval(assetId: String){
        val now = System.currentTimeMillis()
        val past = now - when (_selectedInterval.value) {
            CandleInterval.ONE_MINUTE -> 60 * 60 * 1000L
            CandleInterval.FIFTEEN_MINUTES -> 6 * 60 * 60 * 1000L
            CandleInterval.ONE_HOUR -> 24 * 60 * 60 * 1000L
        }
        load(assetId, past, now, _selectedInterval.value.apiParam)
    }

    private fun load(assetId: String, start: Long, end: Long, interval: String = "m15") {
        viewModelScope.launch(dispatcher.io) {
            _uiState.value = UiState.Loading
            try {
                val candles = getAssetHistoryUseCase(assetId, start, end, interval)
                baseCandles = candles
                withContext(dispatcher.main) {
                    _livePrice.value = candles.last().close
                    _uiState.value = UiState.Success(candles)
                }
                startObservingPrices(assetId)
            } catch (e: Exception) {
                withContext(dispatcher.main) {
                    _uiState.value = UiState.Error(e.message ?: "Failed to load chart")
                }
            }
        }
    }

    private fun startObservingPrices(assetId: String) {
        if (priceJob?.isActive == true) return

        priceJob = viewModelScope.launch(dispatcher.io) {
            observePriceUpdatesUseCase().collect { prices ->
                prices[assetId]?.also { price ->
                    _livePrice.value = price

                    val current = (_uiState.value as? UiState.Success)?.data ?: baseCandles
                    val updated = current.toMutableList()

                    if (updated.isNotEmpty()) {
                        val last = updated.last()
                        val newCandle = last.copy(
                            close = prices[assetId] ?: 0.0,
                            high = maxOf(last.high, price),
                            low = minOf(last.low, price)
                        )
                        updated[updated.lastIndex] = newCandle
                        withContext(dispatcher.main) {
                            _uiState.value = UiState.Success(updated)
                        }
                    }
                }
            }
        }
    }

    override fun onCleared() {
        priceJob?.cancel()
        super.onCleared()
    }
}