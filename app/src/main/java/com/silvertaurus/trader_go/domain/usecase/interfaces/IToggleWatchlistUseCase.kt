package com.silvertaurus.trader_go.domain.usecase.interfaces

interface IToggleWatchlistUseCase {
    suspend operator fun invoke(assetId: String)
}