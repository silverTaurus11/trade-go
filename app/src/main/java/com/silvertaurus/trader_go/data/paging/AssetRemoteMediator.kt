package com.silvertaurus.trader_go.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.silvertaurus.trader_go.data.local.LocalAssetDataSource
import com.silvertaurus.trader_go.data.local.entity.AssetEntity
import com.silvertaurus.trader_go.data.remote.RemoteAssetDataSource

@OptIn(ExperimentalPagingApi::class)
class AssetRemoteMediator(
    private val remote: RemoteAssetDataSource,
    private val local: LocalAssetDataSource
) : RemoteMediator<Int, AssetEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, AssetEntity>
    ): MediatorResult {
        try {
            val pageSize = state.config.pageSize
            val offset = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    lastItem?.rank ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val remoteAssets = remote.getTopAssets(limit = pageSize, offset = offset)

            if (loadType == LoadType.REFRESH) {
                local.clearAssets()
            }
            local.cacheAssets(remoteAssets)

            val endReached = remoteAssets.isEmpty()
            return MediatorResult.Success(endOfPaginationReached = endReached)

        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}