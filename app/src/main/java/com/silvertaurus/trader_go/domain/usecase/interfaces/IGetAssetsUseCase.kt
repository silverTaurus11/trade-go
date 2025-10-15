package com.silvertaurus.trader_go.domain.usecase.interfaces

import androidx.paging.PagingData
import com.silvertaurus.trader_go.domain.model.Asset
import kotlinx.coroutines.flow.Flow

interface IGetAssetsUseCase {
    suspend operator fun invoke(): Flow<PagingData<Asset>>
}