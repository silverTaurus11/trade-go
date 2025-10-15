package com.silvertaurus.trader_go.data.repositoryimpl

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.silvertaurus.trader_go.data.local.LocalAssetDataSource
import com.silvertaurus.trader_go.data.local.entity.toDomain
import com.silvertaurus.trader_go.data.paging.AssetRemoteMediator
import com.silvertaurus.trader_go.data.paging.WatchlistRemoteMediator
import com.silvertaurus.trader_go.data.remote.RemoteAssetDataSource
import com.silvertaurus.trader_go.domain.model.Asset
import com.silvertaurus.trader_go.domain.model.Candle
import com.silvertaurus.trader_go.domain.repository.AssetRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
class AssetRepositoryImpl @Inject constructor(
    private val remote: RemoteAssetDataSource,
    private val local: LocalAssetDataSource
) : AssetRepository {

    override fun priceUpdatesFlow(): Flow<Map<String, Double>> {
        return remote.observePrices()
    }

    override fun getTopAssetsPager(): Flow<PagingData<Asset>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = AssetRemoteMediator(remote, local),
            pagingSourceFactory = { local.getTopAssetsPagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
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

    override fun getWatchlistPager(): Flow<PagingData<Asset>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = WatchlistRemoteMediator(remote, local, local.getWatchlistFlow()),
            pagingSourceFactory = {
                val ids = runBlocking { local.getWatchlistFlow().first() }
                local.getWatchlistPagingSource(ids)
            }
        ).flow.map { it.map { entity -> entity.toDomain() } }
    }

    override fun getWatchlistIds(): Flow<List<String>> = local.getWatchlistFlow()
}