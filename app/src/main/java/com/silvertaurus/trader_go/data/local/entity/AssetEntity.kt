package com.silvertaurus.trader_go.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.silvertaurus.trader_go.domain.model.Asset

@Entity(tableName = "assets")
data class AssetEntity(
    @PrimaryKey val id: String,
    val symbol: String,
    val name: String,
    val priceUsd: Double,
    val changePercent24Hr: Double,
    val rank: Int
)

// Mapper: Entity -> Domain
fun AssetEntity.toDomain(): Asset {
    return Asset(
        id = id,
        symbol = symbol,
        name = name,
        priceUsd = priceUsd,
        changePercent24Hr = changePercent24Hr,
        rank = rank
    )
}

// Mapper: Domain -> Entity
fun Asset.toEntity(): AssetEntity {
    return AssetEntity(
        id = id,
        symbol = symbol,
        name = name,
        priceUsd = priceUsd,
        changePercent24Hr = changePercent24Hr,
        rank = rank
    )
}