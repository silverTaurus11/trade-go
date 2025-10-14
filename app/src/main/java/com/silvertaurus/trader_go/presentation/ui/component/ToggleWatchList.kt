package com.silvertaurus.trader_go.presentation.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun ToggleWatchList(isWatched: Boolean, onClick: () -> Unit) {
    IconButton(onClick = {
        onClick()
    }) {
        Icon(
            imageVector = if (isWatched) Icons.Default.Star else Icons.Default.StarBorder,
            contentDescription = "Toggle Watchlist",
            tint = if (isWatched) Color.Yellow else Color.Gray
        )
    }
}