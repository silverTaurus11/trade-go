package com.silvertaurus.trader_go.presentation.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun ToggleWatchList(isWatched: Boolean, onClick: () -> Unit) {
    val inactiveTint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)

    IconButton(onClick = onClick) {
        Icon(
            imageVector = if (isWatched) Icons.Default.Star else Icons.Default.StarBorder,
            contentDescription = "Toggle Watchlist",
            tint = if (isWatched) Color(0xFFFFD600) else inactiveTint  // kuning amber solid, abu adaptive
        )
    }
}
