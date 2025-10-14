package com.silvertaurus.trader_go.domain.model

data class Candle(
    val time: Long,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double
)