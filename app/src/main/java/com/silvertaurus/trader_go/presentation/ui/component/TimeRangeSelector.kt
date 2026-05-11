package com.silvertaurus.trader_go.presentation.ui.component

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.silvertaurus.trader_go.domain.model.CandleInterval

@Composable
fun TimeRangeSelector(
    selected: CandleInterval,
    onSelected: (CandleInterval) -> Unit
) {
    val transition = updateTransition(selected, label = "rangeTransition")
    val surfaceColor = MaterialTheme.colorScheme.surface
    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary   // hitam → kontras di atas hijau
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface   // putih/hitam sesuai mode

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(surfaceColor, RoundedCornerShape(8.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        CandleInterval.entries.forEach { interval ->

            val bg by transition.animateColor(label = "bgColor") {
                if (it == interval) primaryColor else Color.Transparent
            }
            val textColor by transition.animateColor(label = "textColor") {
                if (it == interval) onPrimaryColor else onSurfaceColor
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(bg)
                    .clickable { onSelected(interval) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    interval.label,
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}
