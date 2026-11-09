package com.mallet.echotiles.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mallet.echotiles.core.theme.SurfaceWhite
import com.mallet.echotiles.core.theme.TextPrimary

@Composable
fun ClayCard(
    modifier: Modifier = Modifier,
    containerColor: Color = SurfaceWhite,
    containerBrush: Brush? = null,
    shadowColor: Color = TextPrimary,
    cornerRadius: Dp = 28.dp,
    shadowElevation: Dp = 12.dp,
    borderColor: Color? = null,
    highlightAlpha: Float = 0.38f,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    Box(
        modifier = modifier
            .shadow(
                elevation = shadowElevation,
                shape = shape,
                ambientColor = shadowColor,
                spotColor = shadowColor
            )
            .clip(shape)
            .background(containerColor)
            .then(if (containerBrush != null) Modifier.background(containerBrush) else Modifier)
            .then(
                if (borderColor != null) {
                    Modifier.border(width = 3.dp, color = borderColor, shape = shape)
                } else {
                    Modifier
                }
            ),
        contentAlignment = contentAlignment
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        0f to Color.White.copy(alpha = highlightAlpha),
                        0.45f to Color.Transparent
                    )
                )
        )
        content()
    }
}
