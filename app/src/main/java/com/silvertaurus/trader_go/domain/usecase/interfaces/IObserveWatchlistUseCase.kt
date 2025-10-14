package com.silvertaurus.trader_go.domain.usecase.interfaces

import com.silvertaurus.trader_go.domain.model.Asset
import kotlinx.coroutines.flow.Flow

interface IObserveWatchlistUseCase {
    operator fun invoke(): Flow<List<Asset>>
    suspend fun isInWatchlist(id: String): Boolean
}