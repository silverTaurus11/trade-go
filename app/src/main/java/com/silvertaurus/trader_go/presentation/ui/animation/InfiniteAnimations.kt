package com.silvertaurus.trader_go.presentation.ui.animation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

object InfiniteAnimations {

    @Composable
    fun pulse(
        min: Float = 0.9f,
        max: Float = 1f,
        duration: Int = 1000
    ): State<Float> {
        val infinite = rememberInfiniteTransition()
        return infinite.animateFloat(
            initialValue = min,
            targetValue = max,
            animationSpec = infiniteRepeatable(
                tween(duration, easing = LinearEasing),
                RepeatMode.Reverse
            )
        )
    }
}
