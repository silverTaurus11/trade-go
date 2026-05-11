package com.silvertaurus.trader_go.presentation.ui.animation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically

object VisibilityAnimations {

    fun fadeSlideIn(): EnterTransition =
        fadeIn(AnimationSpecs.normal()) +
                slideInVertically(
                    initialOffsetY = { it / 4 }
                )

    fun fadeSlideOut(): ExitTransition =
        fadeOut(AnimationSpecs.fast()) +
                slideOutVertically(
                    targetOffsetY = { it / 4 }
                )
}
