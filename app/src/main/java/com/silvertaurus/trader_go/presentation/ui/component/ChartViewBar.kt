package com.silvertaurus.trader_go.presentation.ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.silvertaurus.trader_go.domain.model.Candle

private val ColorUp = android.graphics.Color.rgb(38, 166, 154)
private val ColorDown = android.graphics.Color.rgb(239, 83, 80)
private val ColorNeutral = android.graphics.Color.rgb(120, 120, 120)

@Composable
fun ChartViewBar(
    candles: List<Candle>,
    modifier: Modifier = Modifier
) {
    val axisTextColor = MaterialTheme.colorScheme.onSurface.toArgb()
    val gridColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f).toArgb()
    val highlightColor = MaterialTheme.colorScheme.onSurface.toArgb()

    AndroidView(
        modifier = modifier,
        factory = { context ->
            BarChart(context).apply {
                description.isEnabled = false
                legend.isEnabled = false
                setTouchEnabled(true)
                setPinchZoom(true)
                setBackgroundColor(Color.Transparent.toArgb())

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    textColor = axisTextColor
                    setDrawGridLines(false)
                    setDrawLabels(false)
                }

                axisLeft.apply {
                    textColor = axisTextColor
                    this.gridColor = gridColor
                    setDrawGridLines(true)
                }

                axisRight.isEnabled = false
                setFitBars(true)
            }
        },
        update = { chart ->
            chart.xAxis.textColor = axisTextColor
            chart.axisLeft.textColor = axisTextColor
            chart.axisLeft.gridColor = gridColor

            if (candles.isEmpty()) return@AndroidView

            val entries = candles.mapIndexed { index, candle ->
                BarEntry(index.toFloat(), candle.close.toFloat())
            }

            val colors = candles.map { candle ->
                when {
                    candle.close > candle.open -> ColorUp
                    candle.close < candle.open -> ColorDown
                    else -> ColorNeutral
                }
            }

            val dataSet = BarDataSet(entries, "Price").apply {
                setColors(colors)
                setDrawValues(false)
                highLightColor = highlightColor
            }

            chart.data = BarData(dataSet)
            chart.isAutoScaleMinMaxEnabled = true
            chart.animateY(300)
            chart.invalidate()
        }
    )
}
