package com.silvertaurus.trader_go.data.remote

import com.silvertaurus.trader_go.domain.model.Asset
import com.silvertaurus.trader_go.domain.model.Candle
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoteAssetDataSource @Inject constructor(
    private val api: CoinCapApi,
    private val ws: CoinCapWebSocketManager
) {
    suspend fun getTopAssets(limit: Int): List<Asset> {
        val resp = api.getAssets(limit = limit)
        return resp.data?.mapNotNull { dto ->
            val price = dto.priceUsd?.toDoubleOrNull() ?: return@mapNotNull null
            val change = dto.changePercent24Hr?.toDoubleOrNull() ?: 0.0
            Asset(dto.id, dto.symbol, dto.name, price, change)
        } ?: listOf()
    }

    suspend fun getAssetHistory(
        id: String,
        start: Long,
        end: Long,
        interval: String
    ): List<Candle> {
        val res = api.getAssetHistory(id, interval, start, end)
        return res.data.mapNotNull { dto ->
            val price = dto.priceUsd?.toDoubleOrNull() ?: return@mapNotNull null
            Candle(dto.time, price, price, price, price)
        }
    }

    fun observePrices(): Flow<Map<String, Double>> = ws.prices
}