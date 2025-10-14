package com.silvertaurus.trader_go.data.model

data class AssetsResponse(
    val data: List<AssetsDto>? = null
)

data class AssetsDto(
    val id: String,
    val rank: String?,
    val symbol: String,
    val name: String,
    val priceUsd: String?,
    val changePercent24Hr: String?
)