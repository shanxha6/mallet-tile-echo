package com.mallet.echotiles.core.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mallet.echotiles.core.theme.TextPrimary

private val BoardPad = 10.dp
private val BoardBorder = 3.dp
private val BarGap = 10.dp

@Composable
fun XylophoneBoard(
    barColors: List<Color>,
    litIndex: Int?,
    enabled: Boolean,
    onBarTap: (Int) -> Unit,
    barDescriptions: List<String>,
    ringingDescription: String,
    quietDescription: String,
    modifier: Modifier = Modifier
) {
    val count = barColors.size
    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val frame = BoardPad + BoardBorder
        val boardWidth = (maxWidth - 32.dp).coerceAtMost(380.dp)
        val innerWidth = boardWidth - frame * 2
        val innerHeight = maxHeight - frame * 2
        val available = innerHeight - BarGap * (count - 1)
        val barHeight = (available / count).coerceIn(28.dp, 104.dp)
        val boardHeight = barHeight * count + BarGap * (count - 1) + frame * 2

        Box(
            modifier = Modifier
                .width(boardWidth)
                .height(boardHeight)
                .shadow(
                    elevation = 14.dp,
                    shape = RoundedCornerShape(34.dp),
                    ambientColor = TextPrimary,
                    spotColor = TextPrimary
                )
                .clip(RoundedCornerShape(34.dp))
                .background(Color.White.copy(alpha = 0.72f))
                .background(
                    Brush.verticalGradient(
                        0f to Color.White.copy(alpha = 0.55f),
                        0.5f to Color.Transparent
                    )
                )
                .padding(frame),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(BarGap),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                barColors.forEachIndexed { index, color ->
                    XylophoneBar(
                        index = index,
                        color = color,
                        width = innerWidth * (1f - index * 0.052f),
                        height = barHeight,
                        lit = litIndex == index,
                        enabled = enabled,
                        onTap = onBarTap,
                        description = barDescriptions.getOrElse(index) { quietDescription },
                        ringingDescription = ringingDescription,
                        quietDescription = quietDescription
                    )
                }
            }
        }
    }
}

@Composable
private fun XylophoneBar(
    index: Int,
    color: Color,
    width: Dp,
    height: Dp,
    lit: Boolean,
    enabled: Boolean,
    onTap: (Int) -> Unit,
    description: String,
    ringingDescription: String,
    quietDescription: String
) {
    var pressed by remember { mutableStateOf(false) }
    val currentOnTap by rememberUpdatedState(onTap)
    val glow by animateFloatAsState(
        targetValue = if (lit) 1f else 0f,
        animationSpec = tween(durationMillis = if (lit) 90 else 420),
        label = "barGlow"
    )
    val press by animateDpAsState(
        targetValue = if (pressed) 6.dp else 0.dp,
        animationSpec = spring(dampingRatio = 0.45f, stiffness = 1400f),
        label = "barPress"
    )
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = spring(dampingRatio = 0.45f, stiffness = 1400f),
        label = "barScale"
    )
    val shape = RoundedCornerShape(28.dp)
    val surface = lerp(color, Color.White, 0.24f * glow)

    Box(
        modifier = Modifier
            .width(width)
            .height(height),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(width)
                .height(height)
                .graphicsLayer {
                    val halo = 1f + 0.18f * glow
                    scaleX = halo
                    scaleY = halo
                    alpha = 0.5f * glow
                }
                .clip(shape)
                .background(color)
        )
        Box(
            modifier = Modifier
                .width(width)
                .height(height)
                .offset(y = press)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    this.alpha = if (enabled) 1f else 0.5f
                }
                .shadow(
                    elevation = 10.dp,
                    shape = shape,
                    ambientColor = color,
                    spotColor = color
                )
                .clip(shape)
                .background(surface)
                .background(
                    Brush.verticalGradient(
                        0f to Color.White.copy(alpha = 0.38f),
                        0.45f to Color.Transparent
                    )
                )
                .pointerInput(enabled, index) {
                    pressed = false
                    if (!enabled) return@pointerInput
                    awaitEachGesture {
                        awaitFirstDown(requireUnconsumed = false)
                        pressed = true
                        currentOnTap(index)
                        waitForUpOrCancellation()
                        pressed = false
                    }
                }
                .semantics {
                    contentDescription = description
                    stateDescription = if (lit) ringingDescription else quietDescription
                },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                PegDot()
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                PegDot()
            }
        }
    }
}

@Composable
private fun PegDot() {
    Box(
        modifier = Modifier
            .size(14.dp)
            .clip(CircleShape)
            .background(TextPrimary.copy(alpha = 0.18f))
    )
}
