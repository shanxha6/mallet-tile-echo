package com.mallet.echotiles.core.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.mallet.echotiles.core.theme.LockedTile

@Composable
fun PhraseDots(
    notes: List<Int>,
    filled: Int,
    activeIndex: Int?,
    barColors: List<Color>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        notes.forEachIndexed { index, note ->
            val isFilled = index < filled
            val isActive = activeIndex == index
            val target by animateColorAsState(
                targetValue = if (isFilled) {
                    barColors.getOrElse(note) { LockedTile }
                } else {
                    LockedTile
                },
                animationSpec = tween(180),
                label = "dotColor"
            )
            val scale by animateFloatAsState(
                targetValue = when {
                    isActive -> 1.4f
                    isFilled -> 1.15f
                    else -> 1f
                },
                animationSpec = tween(180),
                label = "dotScale"
            )
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
                    .clip(CircleShape)
                    .background(target)
            )
        }
    }
}
