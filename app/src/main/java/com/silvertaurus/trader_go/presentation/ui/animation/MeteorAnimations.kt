package com.silvertaurus.trader_go.presentation.ui.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

object MeteorAnimations {

    @Composable
    fun rememberMeteorState(
        startOffset: Float = -600f,
        endOffset: Float = 0f
    ): Triple<Animatable<Float, AnimationVector1D>,
            Animatable<Float, AnimationVector1D>,
            Animatable<Float, AnimationVector1D>> {

        val offsetY = remember { Animatable(startOffset) }
        val scale = remember { Animatable(0.8f) }
        val alpha = remember { Animatable(1f) }

        return Triple(offsetY, scale, alpha)
    }

    suspend fun launchMeteor(
        offsetY: Animatable<Float, AnimationVector1D>,
        scale: Animatable<Float, AnimationVector1D>,
        alpha: Animatable<Float, AnimationVector1D>
    ) {
        coroutineScope {
            launch {
                offsetY.animateTo(0f, PhysicsAnimations.meteorFall())
            }
            launch {
                scale.animateTo(1.05f, PhysicsAnimations.meteorFall())
            }
        }
        //to hide content after falling down
        //alpha.animateTo(0f, tween(200))
        scale.animateTo(1f, PhysicsAnimations.bounce())
    }


}