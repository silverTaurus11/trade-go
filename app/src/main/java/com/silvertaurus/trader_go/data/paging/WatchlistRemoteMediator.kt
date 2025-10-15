package com.silvertaurus.trader_go.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.silvertaurus.trader_go.data.local.LocalAssetDataSource
import com.silvertaurus.trader_go.data.local.entity.AssetEntity
import com.silvertaurus.trader_go.data.remote.RemoteAssetDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

@OptIn(ExperimentalPagingApi::class)
class WatchlistRemoteMediator(
    private val remote: RemoteAssetDataSource,
    private val local: LocalAssetDataSource,
    private val idsFlow: Flow<List<String>>
) : RemoteMediator<Int, AssetEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, AssetEntity>
    ): MediatorResult {
        try {
            val ids = idsFlow.firstOrNull().orEmpty()
            if (ids.isEmpty()) return MediatorResult.Success(endOfPaginationReached = true)

            val remoteAssets = remote.getTopAssets(ids.joinToString(","), limit = 100, offset = 0)

            if (loadType == LoadType.REFRESH) {
                local.deleteByIds(ids)
            }

            local.cacheAssets(remoteAssets)

            return MediatorResult.Success(endOfPaginationReached = true)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}
