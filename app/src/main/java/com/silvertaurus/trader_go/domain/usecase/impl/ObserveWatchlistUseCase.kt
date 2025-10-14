package com.silvertaurus.trader_go.domain.usecase.impl

import com.silvertaurus.trader_go.domain.model.Asset
import com.silvertaurus.trader_go.domain.repository.AssetRepository
import com.silvertaurus.trader_go.domain.usecase.interfaces.IObserveWatchlistUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveWatchlistUseCase @Inject constructor(
    private val repository: AssetRepository
) : IObserveWatchlistUseCase {
    override operator fun invoke(): Flow<List<Asset>> =
        repository.watchlistFlow()

    override suspend fun isInWatchlist(id: String): Boolean = repository.isInWatchlist(id)
}