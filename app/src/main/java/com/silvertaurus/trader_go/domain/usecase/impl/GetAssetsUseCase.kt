package com.silvertaurus.trader_go.domain.usecase.impl

import com.silvertaurus.trader_go.domain.model.Asset
import com.silvertaurus.trader_go.domain.repository.AssetRepository
import com.silvertaurus.trader_go.domain.usecase.interfaces.IGetAssetsUseCase
import javax.inject.Inject

class GetAssetsUseCase @Inject constructor(
    private val repository: AssetRepository
) : IGetAssetsUseCase {
    override suspend operator fun invoke(limit: Int): List<Asset> =
        repository.getTopAssets(limit)
}