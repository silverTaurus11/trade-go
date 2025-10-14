package com.silvertaurus.trader_go.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val GreenPrimary = Color(0xFF00C853)
private val GreenAccent = Color(0xFF69F0AE)
private val RedAccent = Color(0xFFFF5252)
private val BlackBackground = Color(0xFF121212)
private val DarkGraySurface = Color(0xFF1E1E1E)

private val DarkColorScheme = darkColorScheme(
    primary = GreenPrimary,
    onPrimary = Color.White,
    secondary = GreenAccent,
    background = BlackBackground,
    surface = DarkGraySurface,
    onSurface = Color.White,
    error = RedAccent,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = Color.White,
    secondary = GreenAccent,
    background = Color.White,
    surface = Color(0xFFF2F2F2),
    onSurface = Color.Black,
    error = RedAccent,
    onError = Color.White
)

@Composable
fun CryptoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = CryptoTypography,
        content = content
    )
}