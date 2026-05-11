package com.silvertaurus.trader_go.presentation.ui.animation

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween

object PhysicsAnimations {

    fun bounce(): AnimationSpec<Float> =
        AnimationSpecs.bounce()

    fun softSpring(): AnimationSpec<Float> =
        spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessLow
        )

    fun meteorFall(): AnimationSpec<Float> =
        tween(
            durationMillis = 700,
            easing = FastOutLinearInEasing
        )

    fun impact(): AnimationSpec<Float> =
        spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
}
