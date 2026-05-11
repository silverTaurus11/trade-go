package com.silvertaurus.trader_go.presentation.ui.component

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.silvertaurus.trader_go.R
import com.silvertaurus.trader_go.domain.model.Candle

class CandleMarkerView(
    context: Context,
    bgColor: Int,
    private val labelColor: Int,
    private val upColor: Int,
    private val downColor: Int,
    private val neutralColor: Int,
) : MarkerView(context, R.layout.marker_candle) {

    var candles: List<Candle> = emptyList()

    private val tvOpen: TextView = findViewById(R.id.tvOpen)
    private val tvHigh: TextView = findViewById(R.id.tvHigh)
    private val tvLow: TextView = findViewById(R.id.tvLow)
    private val tvClose: TextView = findViewById(R.id.tvClose)

    init {
        background = GradientDrawable().apply {
            setColor(bgColor)
            cornerRadius = 16f
        }
    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        val candleEntry = e as? CandleEntry ?: return
        val candle = candles.getOrNull(candleEntry.x.toInt()) ?: return

        val closeColor = when {
            candle.close > candle.open -> upColor
            candle.close < candle.open -> downColor
            else -> neutralColor
        }

        tvOpen.text  = "O  ${formatPrice(candle.open)}"
        tvHigh.text  = "H  ${formatPrice(candle.high)}"
        tvLow.text   = "L  ${formatPrice(candle.low)}"
        tvClose.text = "C  ${formatPrice(candle.close)}"

        tvOpen.setTextColor(labelColor)
        tvHigh.setTextColor(upColor)
        tvLow.setTextColor(downColor)
        tvClose.setTextColor(closeColor)

        super.refreshContent(e, highlight)
    }

    // Posisi popup: di atas candle, tengah horizontal
    override fun getOffset(): MPPointF = MPPointF(-(width / 2f), -(height.toFloat() + 16f))

    private fun formatPrice(value: Double): String {
        return if (value >= 1_000) "%.2f".format(value)
        else "%.4f".format(value)
    }
}
