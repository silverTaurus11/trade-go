package com.silvertaurus.trader_go.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.silvertaurus.trader_go.data.local.entity.AssetEntity

@Dao
interface AssetDao {
    // --- Paging sources ---
    @Query("SELECT * FROM assets ORDER BY rank ASC")
    fun getTopAssetsPagingSource(): PagingSource<Int, AssetEntity>

    @Query("SELECT * FROM assets WHERE id IN (:ids) ORDER BY rank ASC")
    fun getWatchlistPagingSource(ids: List<String>): PagingSource<Int, AssetEntity>

    // --- Cache management ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(assets: List<AssetEntity>)

    @Query("SELECT * FROM assets ORDER BY name ASC LIMIT :limit OFFSET :offset")
    suspend fun getCachedAssets(limit: Int, offset: Int): List<AssetEntity>

    @Query("DELETE FROM assets")
    suspend fun clearAll()

    @Query("DELETE FROM assets WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: List<String>)
}