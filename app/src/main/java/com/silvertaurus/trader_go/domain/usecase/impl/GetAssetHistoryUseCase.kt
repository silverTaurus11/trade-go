package com.silvertaurus.trader_go.domain.usecase.impl

import com.silvertaurus.trader_go.domain.model.Candle
import com.silvertaurus.trader_go.domain.repository.AssetRepository
import com.silvertaurus.trader_go.domain.usecase.interfaces.IGetAssetHistoryUseCase
import javax.inject.Inject

class GetAssetHistoryUseCase @Inject constructor(
    private val repository: AssetRepository
) : IGetAssetHistoryUseCase {
    override suspend operator fun invoke(id: String, start: Long, end: Long, interval: String): List<Candle> =
        repository.getAssetHistory(id, start, end, interval)
}