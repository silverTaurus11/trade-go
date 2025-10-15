package com.silvertaurus.trader_go.data.local

import androidx.paging.PagingSource
import com.silvertaurus.trader_go.data.local.dao.AssetDao
import com.silvertaurus.trader_go.data.local.dao.WatchlistDao
import com.silvertaurus.trader_go.data.local.entity.AssetEntity
import com.silvertaurus.trader_go.data.local.entity.WatchlistEntity
import com.silvertaurus.trader_go.data.local.entity.toEntity
import com.silvertaurus.trader_go.domain.model.Asset
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalAssetDataSource @Inject constructor(
    private val assetDao: AssetDao,
    private val watchlistDao: WatchlistDao
) {
    fun getTopAssetsPagingSource(): PagingSource<Int, AssetEntity> =
        assetDao.getTopAssetsPagingSource()

    fun getWatchlistPagingSource(ids: List<String>): PagingSource<Int, AssetEntity> =
        assetDao.getWatchlistPagingSource(ids)

    suspend fun cacheAssets(assets: List<Asset>) {
        assetDao.insertAll(assets.map { it.toEntity() })
    }

    suspend fun deleteByIds(ids: List<String>) {
        assetDao.deleteByIds(ids)
    }

    suspend fun clearAssets() = assetDao.clearAll()

    fun getWatchlistFlow(): Flow<List<String>> = watchlistDao.watchlistFlow()

    suspend fun addWatchlist(assetId: String) {
        watchlistDao.add(WatchlistEntity(assetId))
    }

    suspend fun removeWatchlist(assetId: String) {
        watchlistDao.remove(assetId)
    }
}