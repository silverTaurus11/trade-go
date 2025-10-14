package com.silvertaurus.trader_go.domain.repository

import com.silvertaurus.trader_go.domain.model.Asset
import com.silvertaurus.trader_go.domain.model.Candle
import kotlinx.coroutines.flow.Flow

interface AssetRepository {
    suspend fun getTopAssets(limit: Int = 100): List<Asset>
    suspend fun getCachedAssets(): List<Asset>
    suspend fun getAssetHistory(id: String, start: Long, end: Long, interval: String): List<Candle>
    fun priceUpdatesFlow(): Flow<Map<String, Double>>
    suspend fun toggleWatchlist(assetId: String)
    fun watchlistFlow(): Flow<List<Asset>>
    suspend fun isInWatchlist(id: String): Boolean
}