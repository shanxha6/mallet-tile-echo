package com.mallet.echotiles.core.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mallet.echotiles.core.config.GameConfig
import com.mallet.echotiles.core.theme.Honey
import com.mallet.echotiles.core.theme.Tomato

@Composable
fun HoldToOpenTile(
    onUnlocked: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    size: Dp = 120.dp,
    ringColor: Color = Tomato,
    containerColor: Color = Honey
) {
    var holding by remember { mutableStateOf(false) }
    val progress = remember { Animatable(0f) }
    val currentOnUnlocked by rememberUpdatedState(onUnlocked)

    LaunchedEffect(holding) {
        if (holding) {
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = GameConfig.PARENT_GATE_MS.toInt(),
                    easing = LinearEasing
                )
            )
            currentOnUnlocked()
        } else {
            progress.snapTo(0f)
        }
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(containerColor)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        holding = true
                        tryAwaitRelease()
                        holding = false
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val stroke = 10.dp.toPx()
            val inset = stroke / 2f
            drawArc(
                color = ringColor.copy(alpha = 0.25f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = Size(this.size.width - stroke, this.size.height - stroke),
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
            drawArc(
                color = ringColor,
                startAngle = -90f,
                sweepAngle = 360f * progress.value,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = Size(this.size.width - stroke, this.size.height - stroke),
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
        }
        Box(
            modifier = Modifier
                .size(size * 0.45f)
                .clip(CircleShape)
                .background(ringColor.copy(alpha = 0.18f))
        ) {
            LockGlyph(
                color = ringColor,
                contentDescription = contentDescription,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
