package com.silvertaurus.trader_go.data.local

import com.silvertaurus.trader_go.data.local.dao.AssetDao
import com.silvertaurus.trader_go.data.local.dao.WatchlistDao
import com.silvertaurus.trader_go.data.local.entity.AssetEntity
import com.silvertaurus.trader_go.data.local.entity.WatchlistEntity
import com.silvertaurus.trader_go.domain.model.Asset
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalAssetDataSource @Inject constructor(
    private val assetDao: AssetDao,
    private val watchlistDao: WatchlistDao
) {
    fun getCachedAssets(): Flow<List<Asset>> =
        assetDao.getAssets().map { list ->
            list.map { e -> Asset(e.id, e.symbol, e.name, e.priceUsd, e.changePercent24Hr) }
        }

    suspend fun cacheAssets(assets: List<Asset>) {
        val entities = assets.map { a ->
            AssetEntity(a.id, a.symbol, a.name, a.priceUsd, a.changePercent24Hr)
        }
        assetDao.insertAll(entities)
    }

    fun getWatchlistFlow(): Flow<List<String>> = watchlistDao.watchlistFlow()

    suspend fun addWatchlist(assetId: String) {
        watchlistDao.add(WatchlistEntity(assetId))
    }

    suspend fun removeWatchlist(assetId: String) {
        watchlistDao.remove(assetId)
    }

    suspend fun isInWatchlist(id: String): Boolean = watchlistDao.isInWatchlist(id)

}