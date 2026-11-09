package com.mallet.echotiles.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mallet.echotiles.core.theme.SurfaceWhite
import com.mallet.echotiles.core.theme.TextSecondary

data class StatItem(
    val value: Int,
    val label: String,
    val accent: Color
)

@Composable
fun StatCard(
    value: String,
    label: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    ClayCard(
        modifier = modifier,
        containerColor = SurfaceWhite,
        shadowColor = accent,
        cornerRadius = 22.dp,
        shadowElevation = 10.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(accent)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold
                ),
                color = accent
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                color = TextSecondary
            )
        }
    }
}

@Composable
fun StatRow(
    items: List<StatItem>,
    modifier: Modifier = Modifier
) {
    val visible = items.filter { it.value > 0 }
    if (visible.size < 2) return
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        visible.forEach { item ->
            StatCard(
                value = item.value.toString(),
                label = item.label,
                accent = item.accent,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
