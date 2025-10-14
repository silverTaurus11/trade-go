package com.silvertaurus.trader_go.data.repositoryimpl

import com.silvertaurus.trader_go.data.local.LocalAssetDataSource
import com.silvertaurus.trader_go.data.remote.RemoteAssetDataSource
import com.silvertaurus.trader_go.domain.model.Asset
import com.silvertaurus.trader_go.domain.model.Candle
import com.silvertaurus.trader_go.domain.repository.AssetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AssetRepositoryImpl @Inject constructor(
    private val remote: RemoteAssetDataSource,
    private val local: LocalAssetDataSource
) : AssetRepository {

    override fun priceUpdatesFlow(): Flow<Map<String, Double>> {
       return remote.observePrices()
    }

    override suspend fun getTopAssets(limit: Int): List<Asset> {
        val list = remote.getTopAssets(limit)
        local.cacheAssets(list)
        return list
    }

    override suspend fun getCachedAssets(): List<Asset> {
        return local.getCachedAssets().first()
    }

    override suspend fun getAssetHistory(
        id: String,
        start: Long,
        end: Long,
        interval: String
    ): List<Candle> {
        return remote.getAssetHistory(id, start, end, interval)
    }

    override suspend fun toggleWatchlist(assetId: String) {
        val watchlist = local.getWatchlistFlow().first()
        if (watchlist.contains(assetId)) {
            local.removeWatchlist(assetId)
        } else {
            local.addWatchlist(assetId)
        }
    }

    override fun watchlistFlow(): Flow<List<Asset>> {
        return combine(local.getWatchlistFlow(), local.getCachedAssets()) { ids, cached ->
            cached.filter { it.id in ids }
        }
    }

    override suspend fun isInWatchlist(id: String): Boolean = local.isInWatchlist(id)
}