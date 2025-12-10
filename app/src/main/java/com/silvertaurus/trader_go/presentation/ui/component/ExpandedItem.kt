package com.silvertaurus.trader_go.presentation.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.silvertaurus.trader_go.domain.model.Asset

@Composable
fun ExpandableItem(
    asset: Asset,
    onClick: () -> Unit,
    onWatchToggle: (Asset) -> Unit,
    isWatched: Boolean,
    modifier: Modifier = Modifier
) {
    var isExpanded by rememberSaveable(asset.id) { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = asset.symbol,
                style = MaterialTheme.typography.titleMedium
            )

            Text(text = if (isExpanded) "▲" else "▼")
        }

        AnimatedVisibility(visible = isExpanded) {
            Text(
                text = asset.name,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
