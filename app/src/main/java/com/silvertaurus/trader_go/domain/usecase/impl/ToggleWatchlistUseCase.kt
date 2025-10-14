package com.silvertaurus.trader_go.domain.usecase.impl

import com.silvertaurus.trader_go.domain.repository.AssetRepository
import com.silvertaurus.trader_go.domain.usecase.interfaces.IToggleWatchlistUseCase
import javax.inject.Inject

class ToggleWatchlistUseCase @Inject constructor(
    private val repository: AssetRepository
) : IToggleWatchlistUseCase {
    override suspend operator fun invoke(assetId: String) =
        repository.toggleWatchlist(assetId)
}