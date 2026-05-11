package com.silvertaurus.trader_go.domain.utils

object PriceHelper {

    fun print(price: Double?): String {
        return price?.let { "$${formatPrice(it)} " } ?: "-"
    }

    fun formatPrice(value: Double): String {
        return if (value >= 1_000) "%.2f".format(value)
        else "%.4f".format(value)
    }
}