package com.silvertaurus.trader_go.presentation.ui.component

import android.graphics.Paint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.silvertaurus.trader_go.domain.model.Candle
import com.silvertaurus.trader_go.domain.model.CandleInterval
import com.silvertaurus.trader_go.presentation.ui.theme.GreenUp
import com.silvertaurus.trader_go.presentation.ui.theme.NeutralGray
import com.silvertaurus.trader_go.presentation.ui.theme.RedDown
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private class TimeAxisFormatter(
    private val candles: List<Candle>,
    private val interval: CandleInterval
) : ValueFormatter() {
    private val fmt = SimpleDateFormat(
        when (interval) {
            CandleInterval.ONE_MINUTE      -> "HH:mm"
            CandleInterval.FIFTEEN_MINUTES -> "HH:mm"
            CandleInterval.ONE_HOUR        -> "HH:mm"
        },
        Locale.getDefault()
    )

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        val index = value.toInt()
        return if (index in candles.indices) fmt.format(Date(candles[index].time)) else ""
    }
}

@Composable
fun ChartView(
    candles: List<Candle>,
    interval: CandleInterval = CandleInterval.ONE_HOUR,
    modifier: Modifier = Modifier
) {
    // Ambil warna dari tema di composable scope — otomatis recompose saat mode berubah
    val axisTextColor = MaterialTheme.colorScheme.onSurface.toArgb()
    val gridColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f).toArgb()
    val highlightColor = MaterialTheme.colorScheme.onSurface.toArgb()

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            CandleStickChart(context).apply {
                description.isEnabled = false
                legend.isEnabled = false
                setBackgroundColor(Color.Transparent.toArgb())
                setGridBackgroundColor(Color.Transparent.toArgb())

                xAxis.apply {
                    textColor = axisTextColor
                    setDrawGridLines(false)
                    position = XAxis.XAxisPosition.BOTTOM
                    setLabelCount(5, false)
                    granularity = 1f
                }

                axisLeft.apply {
                    textColor = axisTextColor
                    setDrawGridLines(true)
                    this.gridColor = gridColor
                }

                axisRight.isEnabled = false

                setTouchEnabled(true)
                isHighlightPerDragEnabled = true
                setDrawBorders(false)
            }
        },
        update = { chart ->
            // Update warna di sini agar ikut perubahan dark/light mode
            chart.xAxis.textColor = axisTextColor
            chart.axisLeft.textColor = axisTextColor
            chart.axisLeft.gridColor = gridColor

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
                decreasingColor = RedDown.toArgb()
                increasingColor = GreenUp.toArgb()
                neutralColor = NeutralGray.toArgb()
                decreasingPaintStyle = Paint.Style.FILL
                increasingPaintStyle = Paint.Style.FILL
                shadowColorSameAsCandle = true
                highLightColor = highlightColor
                setDrawValues(false)
                barSpace = 0.2f
                shadowWidth = 1.5f
            }

            chart.xAxis.valueFormatter = TimeAxisFormatter(candles, interval)
            chart.data = CandleData(set)
            chart.invalidate()
        }
    )
}
