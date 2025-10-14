package com.silvertaurus.trader_go.domain.usecase.impl

import com.silvertaurus.trader_go.domain.repository.AssetRepository
import com.silvertaurus.trader_go.domain.usecase.interfaces.IObservePriceUpdatesUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePriceUpdatesUseCase @Inject constructor(
    private val repository: AssetRepository
) : IObservePriceUpdatesUseCase {
    override fun invoke(): Flow<Map<String, Double>> {
        return repository.priceUpdatesFlow()
    }
}