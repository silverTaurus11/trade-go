package com.silvertaurus.trader_go.presentation.ui.animation

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween

object AnimationSpecs {

    fun fast() = tween<Float>(
        durationMillis = 200,
        easing = FastOutLinearInEasing
    )

    fun normal() = tween<Float>(
        durationMillis = 400,
        easing = FastOutSlowInEasing
    )

    fun slow() = tween<Float>(
        durationMillis = 700,
        easing = LinearOutSlowInEasing
    )

    fun bounce(
        damping: Float = Spring.DampingRatioMediumBouncy,
        stiffness: Float = Spring.StiffnessLow
    ) = spring<Float>(
        dampingRatio = damping,
        stiffness = stiffness
    )
}
