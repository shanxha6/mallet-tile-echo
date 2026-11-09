package com.mallet.echotiles.core.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mallet.echotiles.R
import com.mallet.echotiles.core.theme.Honey

@Composable
fun MoleStage(
    frame: Int,
    nodTick: Int,
    contentDescription: String,
    modifier: Modifier = Modifier,
    moleSize: Dp = 132.dp,
    hatColor: Color? = null
) {
    val rotation = remember { Animatable(0f) }
    LaunchedEffect(nodTick) {
        if (nodTick > 0) {
            rotation.animateTo(
                targetValue = -9f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = 1400f
                )
            )
            rotation.animateTo(
                targetValue = 0f,
                animationSpec = spring(
                    dampingRatio = 0.5f,
                    stiffness = 700f
                )
            )
        }
    }
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .width(moleSize * 1.5f)
                .height(moleSize * 0.4f)
                .clip(RoundedCornerShape(percent = 50))
                .background(Honey.copy(alpha = 0.6f))
        )
        Box(
            modifier = Modifier
                .size(moleSize)
                .graphicsLayer {
                    rotationZ = rotation.value
                    transformOrigin = TransformOrigin(0.5f, 0.9f)
                },
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                painter = painterResource(
                    if (frame == 0) R.drawable.sprite_1 else R.drawable.sprite_2
                ),
                contentDescription = contentDescription,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(moleSize)
            )
            if (hatColor != null) {
                HatBadge(
                    color = hatColor,
                    modifier = Modifier.offset(y = (-14).dp)
                )
            }
        }
    }
}

@Composable
fun HatBadge(
    color: Color,
    modifier: Modifier = Modifier,
    width: Dp = 56.dp
) {
    Box(
        modifier = modifier.width(width),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .width(width)
                .height(10.dp)
                .clip(RoundedCornerShape(percent = 50))
                .background(color)
        )
        Box(
            modifier = Modifier
                .width(width * 0.6f)
                .height(26.dp)
                .offset(y = (-8).dp)
                .clip(RoundedCornerShape(topStart = 13.dp, topEnd = 13.dp))
                .background(color)
        )
    }
}
