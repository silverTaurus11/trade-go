package com.silvertaurus.trader_go.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.silvertaurus.trader_go.data.local.entity.WatchlistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(item: WatchlistEntity)

    @Query("DELETE FROM watchlist WHERE assetId = :id")
    suspend fun remove(id: String)

    @Query("SELECT assetId FROM watchlist")
    fun watchlistFlow(): Flow<List<String>>
}