package com.silvertaurus.trader_go

import app.cash.turbine.test
import com.silvertaurus.trader_go.data.local.LocalAssetDataSource
import com.silvertaurus.trader_go.data.remote.RemoteAssetDataSource
import com.silvertaurus.trader_go.data.repositoryimpl.AssetRepositoryImpl
import com.silvertaurus.trader_go.domain.model.Candle
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneOffset
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class AssetRepositoryImplTest {

    private lateinit var repository: AssetRepositoryImpl
    private lateinit var remote: RemoteAssetDataSource
    private lateinit var local: LocalAssetDataSource

    @Before
    fun setup() {
        remote = mockk()
        local = mockk()
        repository = AssetRepositoryImpl(remote, local)
    }

    @Test
    fun `Given asset is not in watchlist, When toggleWatchlist called, Then it should be added`() = runTest {
        // Given
        val assetId = "BTC"
        coEvery { local.getWatchlistFlow() } returns flowOf(emptyList())
        coEvery { local.addWatchlist(assetId) } just Runs
        coEvery { local.removeWatchlist(any()) } just Runs

        // When
        repository.toggleWatchlist(assetId)

        // Then
        coVerify(exactly = 1) { local.addWatchlist(assetId) }
        coVerify(exactly = 0) { local.removeWatchlist(any()) }
    }

    @Test
    fun `Given asset is in watchlist, When toggleWatchlist called, Then it should be removed`() = runTest {
        // Given
        val assetId = "ETH"
        coEvery { local.getWatchlistFlow() } returns flowOf(listOf(assetId))
        coEvery { local.addWatchlist(any()) } just Runs
        coEvery { local.removeWatchlist(assetId) } just Runs

        // When
        repository.toggleWatchlist(assetId)

        // Then
        coVerify(exactly = 1) { local.removeWatchlist(assetId) }
        coVerify(exactly = 0) { local.addWatchlist(any()) }
    }

    @Test
    fun `Given remote emits price updates, When priceUpdatesFlow collected, Then values should match`() = runTest {
        // Given
        val expected = mapOf("BTC" to 65000.0, "ETH" to 3500.0)
        every { remote.observePrices() } returns flowOf(expected)

        // When & Then
        repository.priceUpdatesFlow().test {
            assertEquals(expected, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Given valid asset history, When getAssetHistory called, Then it should return same data`() = runTest {
        val date = LocalDate.parse("2024-01-01")
        val timestamp = date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

        // Given
        val candles = listOf(Candle(timestamp, 100.0, 110.0, 90.0, 105.0))
        coEvery { remote.getAssetHistory("BTC", any(), any(), any()) } returns candles

        // When
        val result = repository.getAssetHistory("BTC", 0L, 1L, "1h")

        // Then
        assertEquals(candles, result)
        coVerify(exactly = 1) { remote.getAssetHistory("BTC", any(), any(), any()) }
    }

    @Test
    fun `Given local emits watchlist IDs, When getWatchlistIds collected, Then values should match`() = runTest {
        // Given
        val ids = listOf("BTC", "ETH")
        every { local.getWatchlistFlow() } returns flowOf(ids)

        // When & Then
        repository.getWatchlistIds().test {
            assertEquals(ids, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
