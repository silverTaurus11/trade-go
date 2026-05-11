package com.silvertaurus.trader_go.presentation.ui.screen.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.silvertaurus.trader_go.R
import com.silvertaurus.trader_go.presentation.ui.animation.MeteorAnimations.launchMeteor
import com.silvertaurus.trader_go.presentation.ui.animation.MeteorAnimations.rememberMeteorState
import com.silvertaurus.trader_go.presentation.ui.animation.PhysicsAnimations
import com.silvertaurus.trader_go.presentation.ui.animation.TransitionAnimations
import com.silvertaurus.trader_go.presentation.ui.animation.VisibilityAnimations
import com.silvertaurus.trader_go.presentation.viewmodel.SplashViewModel
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun SplashScreen(
    onNavigateHome: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val isReady by viewModel.isReady.collectAsStateWithLifecycle()

    //val scale = remember { Animatable(0.3f) }

    val (offsetY, scale, alpha) = rememberMeteorState()

    LaunchedEffect(Unit) {
        launchMeteor(offsetY, scale, alpha)
    }

    LaunchedEffect(Unit) {
        /*scale.animateTo(
            targetValue = 1f,
            animationSpec = PhysicsAnimations.softSpring()
        )*/
        viewModel.run()
    }

    LaunchedEffect(isReady) {
        delay(500)
        if (isReady) onNavigateHome()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            /*AnimatedVisibility(
                visible = isReady,
                enter = VisibilityAnimations.fadeSlideIn(),
                exit = VisibilityAnimations.fadeSlideOut()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(120.dp)
                        //.scale(scale.value)
                )
            }*/

            Box(
                modifier = Modifier
                    .offset { IntOffset(0, offsetY.value.roundToInt()) }
                    .scale(scale.value)
                    .alpha(alpha.value)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(120.dp)
                    //.scale(scale.value)
                )
            }

            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.loading_market_data),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}