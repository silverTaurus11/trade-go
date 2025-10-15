package com.silvertaurus.trader_go.domain.usecase.impl

import androidx.paging.PagingData
import com.silvertaurus.trader_go.domain.model.Asset
import com.silvertaurus.trader_go.domain.repository.AssetRepository
import com.silvertaurus.trader_go.domain.usecase.interfaces.IObserveWatchlistUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveWatchlistUseCase @Inject constructor(
    private val repository: AssetRepository
) : IObserveWatchlistUseCase {
    override fun invoke(): Flow<PagingData<Asset>> {
        return repository.getWatchlistPager()
    }

    override fun getWatchlistIds(): Flow<List<String>> {
        return repository.getWatchlistIds()
    }
}