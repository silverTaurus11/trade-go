package com.silvertaurus.trader_go.presentation.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.silvertaurus.trader_go.presentation.ui.theme.GreenUp

@Composable
fun ChartViewLine(
    prices: List<Double>,
    modifier: Modifier = Modifier
) {
    val animatedPrices by animateFloatListAsState(prices)

    AndroidView(
        modifier = modifier,
        factory = { context ->
            LineChart(context).apply {
                description.isEnabled = false
                legend.isEnabled = false
                setTouchEnabled(true)
                setPinchZoom(true)
                setBackgroundColor(Color.Transparent.toArgb())

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    textColor = Color.White.toArgb()
                    setDrawGridLines(false)
                }

                axisLeft.apply {
                    textColor = Color.White.toArgb()
                    gridColor = android.graphics.Color.DKGRAY
                    setDrawGridLines(true)
                }

                axisRight.isEnabled = false
            }
        },
        update = { chart ->
            if (animatedPrices.isEmpty()) return@AndroidView

            val entries = animatedPrices.mapIndexed { index, price ->
                Entry(index.toFloat(), price.toFloat())
            }

            val dataSet = LineDataSet(entries, "Price").apply {
                color = GreenUp.toArgb()
                lineWidth = 2.5f
                setDrawCircles(false)
                setDrawValues(false)
                setDrawFilled(true)
                fillColor = Color(0xFF003300).toArgb()
                fillAlpha = 200
                mode = LineDataSet.Mode.CUBIC_BEZIER
                highLightColor = android.graphics.Color.WHITE
            }

            chart.data = LineData(dataSet)
            chart.isAutoScaleMinMaxEnabled = true
            chart.animateX(300) // smooth transition
            chart.invalidate()
        }
    )
}

@Composable
fun animateFloatListAsState(targetValues: List<Double>): MutableState<List<Double>> {
    val animated = remember { Animatable(0f) }
    val state = remember { mutableStateOf(targetValues) }

    LaunchedEffect(targetValues) {
        if (targetValues.isNotEmpty()) {
            animated.snapTo(0f)
            animated.animateTo(1f, tween(300))
            state.value = targetValues
        }
    }
    return state
}

