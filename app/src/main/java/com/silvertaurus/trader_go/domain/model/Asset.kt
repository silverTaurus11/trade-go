package com.silvertaurus.trader_go.domain.model

data class Asset(
    val id: String,
    val symbol: String,
    val name: String,
    val priceUsd: Double,
    val changePercent24Hr: Double
)