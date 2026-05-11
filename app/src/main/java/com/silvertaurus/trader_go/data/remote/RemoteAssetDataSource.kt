package com.silvertaurus.trader_go.data.remote

import com.silvertaurus.trader_go.domain.model.Asset
import com.silvertaurus.trader_go.domain.model.Candle
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoteAssetDataSource @Inject constructor(
    private val api: CoinCapApi,
    private val ws: CoinCapWebSocketManager
) {
    suspend fun getTopAssets(ids: String? = null, limit: Int, offset: Int): List<Asset> {
        val resp = api.getAssets(ids = ids, limit = limit, offset = offset)
        return resp.data?.mapNotNull { dto ->
            val price = dto.priceUsd?.toDoubleOrNull() ?: return@mapNotNull null
            val change = dto.changePercent24Hr?.toDoubleOrNull() ?: 0.0
            val rank = dto.rank?.toIntOrNull() ?: 0
            Asset(dto.id, dto.symbol, dto.name, price, change, rank)
        } ?: emptyList()
    }

    suspend fun getAssetHistory(
        id: String,
        start: Long,
        end: Long,
        interval: String
    ): List<Candle> {
        val res = api.getAssetHistory(id, interval, start, end)
        var previousPrice = 0.0
        return res.data.mapNotNull { dto ->
            val price = dto.priceUsd?.toDoubleOrNull() ?: return@mapNotNull null
            if (previousPrice == 0.0) previousPrice = price
            val openPrice = previousPrice                                    // simpan open sebelum diupdate
            val highPrice = if (price > previousPrice) price else previousPrice
            val lowPrice = if (price < previousPrice) price else previousPrice
            previousPrice = price                                            // update untuk iterasi berikutnya
            Candle(dto.time, openPrice, highPrice, lowPrice, price)
        }
    }

    fun observePrices(): Flow<Map<String, Double>> = ws.prices
}