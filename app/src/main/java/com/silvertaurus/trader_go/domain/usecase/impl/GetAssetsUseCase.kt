package com.silvertaurus.trader_go.domain.usecase.impl

import androidx.paging.PagingData
import com.silvertaurus.trader_go.domain.model.Asset
import com.silvertaurus.trader_go.domain.repository.AssetRepository
import com.silvertaurus.trader_go.domain.usecase.interfaces.IGetAssetsUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAssetsUseCase @Inject constructor(
    private val repository: AssetRepository
) : IGetAssetsUseCase {

    override suspend fun invoke(): Flow<PagingData<Asset>> = repository.getTopAssetsPager()
}