package com.silvertaurus.trader_go.presentation.ui.animation

import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable

object TransitionAnimations {

    @Composable
    fun <T> rememberTransition(state: T) =
        updateTransition(state, label = "AppTransition")
}
