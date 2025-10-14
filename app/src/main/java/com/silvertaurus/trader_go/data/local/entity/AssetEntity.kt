package com.silvertaurus.trader_go.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assets")
data class AssetEntity(
    @PrimaryKey val id: String,
    val symbol: String,
    val name: String,
    val priceUsd: Double,
    val changePercent24Hr: Double
)