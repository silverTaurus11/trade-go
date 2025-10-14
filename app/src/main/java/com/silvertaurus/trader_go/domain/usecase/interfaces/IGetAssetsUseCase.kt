package com.silvertaurus.trader_go.domain.usecase.interfaces

import com.silvertaurus.trader_go.domain.model.Asset

interface IGetAssetsUseCase {
    suspend operator fun invoke(limit: Int = 100): List<Asset>
}