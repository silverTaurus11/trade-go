package com.silvertaurus.trader_go.data.model

data class HistoryResponse(
    val data: List<HistoryDto>
)

data class HistoryDto(
    val priceUsd: String?,
    val time: Long
)