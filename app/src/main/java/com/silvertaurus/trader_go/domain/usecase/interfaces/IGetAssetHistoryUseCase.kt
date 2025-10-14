package com.silvertaurus.trader_go.domain.usecase.interfaces

import com.silvertaurus.trader_go.domain.model.Candle

interface IGetAssetHistoryUseCase {
    suspend operator fun invoke(
        id: String,
        start: Long,
        end: Long,
        interval: String = "m15"
    ): List<Candle>
}