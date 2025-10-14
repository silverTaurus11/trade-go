package com.silvertaurus.trader_go.di

import com.silvertaurus.trader_go.domain.usecase.impl.GetAssetHistoryUseCase
import com.silvertaurus.trader_go.domain.usecase.impl.GetAssetsUseCase
import com.silvertaurus.trader_go.domain.usecase.impl.ObservePriceUpdatesUseCase
import com.silvertaurus.trader_go.domain.usecase.impl.ObserveWatchlistUseCase
import com.silvertaurus.trader_go.domain.usecase.impl.ToggleWatchlistUseCase
import com.silvertaurus.trader_go.domain.usecase.interfaces.IGetAssetHistoryUseCase
import com.silvertaurus.trader_go.domain.usecase.interfaces.IGetAssetsUseCase
import com.silvertaurus.trader_go.domain.usecase.interfaces.IObservePriceUpdatesUseCase
import com.silvertaurus.trader_go.domain.usecase.interfaces.IObserveWatchlistUseCase
import com.silvertaurus.trader_go.domain.usecase.interfaces.IToggleWatchlistUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    @Singleton
    abstract fun bindGetAssetsUseCase(impl: GetAssetsUseCase): IGetAssetsUseCase

    @Binds
    @Singleton
    abstract fun bindGetAssetHistoryUseCase(impl: GetAssetHistoryUseCase): IGetAssetHistoryUseCase

    @Binds
    @Singleton
    abstract fun bindToggleWatchlistUseCase(impl: ToggleWatchlistUseCase): IToggleWatchlistUseCase

    @Binds
    @Singleton
    abstract fun bindObserveWatchlistUseCase(impl: ObserveWatchlistUseCase): IObserveWatchlistUseCase

    @Binds
    @Singleton
    abstract fun bindObservePriceUpdatesUseCase(impl: ObservePriceUpdatesUseCase): IObservePriceUpdatesUseCase
}