package com.silvertaurus.trader_go.presentation.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.silvertaurus.trader_go.domain.model.Asset
import com.silvertaurus.trader_go.presentation.ui.theme.GreenUp
import com.silvertaurus.trader_go.presentation.ui.theme.NeutralGray
import com.silvertaurus.trader_go.presentation.ui.theme.RedDown

@Composable
fun AssetItem(
    asset: Asset,
    onClick: () -> Unit,
    onWatchToggle: (Asset) -> Unit,
    isWatched: Boolean
) {
    val priceColor = if (asset.changePercent24Hr >= 0) GreenUp else RedDown
    val changeText = "${"%.2f".format(asset.changePercent24Hr)}%"

    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(asset.name, style = MaterialTheme.typography.titleMedium)
            Text(asset.symbol, color = NeutralGray, style = MaterialTheme.typography.labelSmall)
        }
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$${"%.2f".format(asset.priceUsd)}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(changeText, color = priceColor, style = MaterialTheme.typography.bodyMedium)
            }
            ToggleWatchList(isWatched) { onWatchToggle(asset) }
        }
    }
}