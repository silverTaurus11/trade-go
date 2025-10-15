package com.silvertaurus.trader_go.domain.repository

import androidx.paging.PagingData
import com.silvertaurus.trader_go.domain.model.Asset
import com.silvertaurus.trader_go.domain.model.Candle
import kotlinx.coroutines.flow.Flow

interface AssetRepository {
    fun getTopAssetsPager(): Flow<PagingData<Asset>>
    suspend fun getAssetHistory(id: String, start: Long, end: Long, interval: String): List<Candle>
    fun priceUpdatesFlow(): Flow<Map<String, Double>>
    suspend fun toggleWatchlist(assetId: String)
    fun getWatchlistPager(): Flow<PagingData<Asset>>
    fun getWatchlistIds(): Flow<List<String>>
}