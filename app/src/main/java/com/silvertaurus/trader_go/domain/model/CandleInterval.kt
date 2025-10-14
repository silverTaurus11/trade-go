package com.silvertaurus.trader_go.domain.model

enum class CandleInterval(val label: String, val apiParam: String, val durationMs: Long) {
    ONE_MINUTE("1m", "m1", 60_000L),
    FIFTEEN_MINUTES("15m", "m15", 15 * 60_000L),
    ONE_HOUR("1h", "h1", 60 * 60_000L)
}