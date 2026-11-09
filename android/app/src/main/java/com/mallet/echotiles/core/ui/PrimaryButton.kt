package com.mallet.echotiles.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.mallet.echotiles.core.theme.TextOnAccent
import com.mallet.echotiles.core.theme.Tomato
import com.mallet.echotiles.core.theme.TomatoEnd

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    startColor: Color = Tomato,
    endColor: Color = TomatoEnd,
    labelColor: Color = TextOnAccent
) {
    val shape = RoundedCornerShape(32.dp)
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.96f else 1f,
        label = "primaryButtonScale"
    )
    val contentAlpha = if (enabled) 1f else 0.45f
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        interactionSource = interactionSource,
        contentPadding = PaddingValues(0.dp),
        elevation = null,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = TextOnAccent,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = TextOnAccent.copy(alpha = 0.7f)
        ),
        modifier = modifier
            .height(64.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = if (enabled) 14.dp else 0.dp,
                shape = shape,
                ambientColor = startColor,
                spotColor = startColor
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            startColor.copy(alpha = contentAlpha),
                            endColor.copy(alpha = contentAlpha)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            0f to Color.White.copy(alpha = 0.30f),
                            0.5f to Color.Transparent
                        )
                    )
            )
            Text(
                text = text,
                style = MaterialTheme.typography.headlineMedium,
                color = labelColor.copy(alpha = contentAlpha)
            )
        }
    }
}
