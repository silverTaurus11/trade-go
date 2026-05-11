package com.silvertaurus.trader_go.presentation.ui.animation

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.Composable

object ValueAnimations {

    @Composable
    fun scale(
        target: Float,
        spec: AnimationSpec<Float> = AnimationSpecs.normal()
    ) = animateFloatAsState(target, spec)

    @Composable
    fun alpha(
        target: Float,
        spec: AnimationSpec<Float> = AnimationSpecs.fast()
    ) = animateFloatAsState(target, spec)

}
