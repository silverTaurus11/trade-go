package com.silvertaurus.trader_go.presentation.ui.component

import android.graphics.Paint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.silvertaurus.trader_go.domain.model.Candle
import com.silvertaurus.trader_go.presentation.ui.theme.GreenUp
import com.silvertaurus.trader_go.presentation.ui.theme.NeutralGray
import com.silvertaurus.trader_go.presentation.ui.theme.RedDown

@Composable
fun ChartView(candles: List<Candle>, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            CandleStickChart(context).apply {
                description.isEnabled = false
                legend.isEnabled = false
                setBackgroundColor(Color.Transparent.toArgb())
                setGridBackgroundColor(Color.Transparent.toArgb())

                xAxis.apply {
                    textColor = Color.White.toArgb()
                    setDrawGridLines(false)
                    position = XAxis.XAxisPosition.BOTTOM
                }

                axisLeft.apply {
                    textColor = Color.White.toArgb()
                    setDrawGridLines(true)
                    gridColor = Color.Gray.toArgb()
                }

                axisRight.isEnabled = false

                setTouchEnabled(true)
                isHighlightPerDragEnabled = true
                setDrawBorders(false)
            }
        },
        update = { chart ->
            val entries = candles.mapIndexed { i, c ->
                CandleEntry(
                    i.toFloat(),
                    c.high.toFloat(),
                    c.low.toFloat(),
                    c.open.toFloat(),
                    c.close.toFloat()
                )
            }

            val set = CandleDataSet(entries, "").apply {
                // ✅ Atur warna candlestick
                decreasingColor = RedDown.toArgb()
                increasingColor = GreenUp.toArgb()
                neutralColor = NeutralGray.toArgb()

                // ✅ Gunakan body solid, bukan garis tipis
                decreasingPaintStyle = Paint.Style.FILL
                increasingPaintStyle = Paint.Style.FILL

                // ✅ Tampilkan shadow warna sesuai candle
                shadowColorSameAsCandle = true

                // ✅ Hapus nilai di atas candle
                setDrawValues(false)

                // ✅ Sedikit tebal agar terlihat
                barSpace = 0.2f
                shadowWidth = 1.5f
            }

            chart.data = CandleData(set)
            chart.invalidate()
        }
    )
}
