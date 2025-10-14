package com.silvertaurus.trader_go.domain.usecase.interfaces

import kotlinx.coroutines.flow.Flow

interface IObservePriceUpdatesUseCase {
    operator fun invoke(): Flow<Map<String, Double>>
}