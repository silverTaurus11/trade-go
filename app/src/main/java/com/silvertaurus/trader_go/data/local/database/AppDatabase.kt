package com.silvertaurus.trader_go.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.silvertaurus.trader_go.data.local.dao.AssetDao
import com.silvertaurus.trader_go.data.local.dao.WatchlistDao
import com.silvertaurus.trader_go.data.local.entity.AssetEntity
import com.silvertaurus.trader_go.data.local.entity.WatchlistEntity

@Database(entities = [AssetEntity::class, WatchlistEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun assetDao(): AssetDao
    abstract fun watchlistDao(): WatchlistDao
}