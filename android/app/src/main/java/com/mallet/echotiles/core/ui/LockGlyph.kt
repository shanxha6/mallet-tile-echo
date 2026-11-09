package com.mallet.echotiles.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun LockGlyph(
    color: Color,
    contentDescription: String,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp
) {
    Canvas(
        modifier = modifier
            .size(size)
            .semantics { this.contentDescription = contentDescription }
    ) {
        val width = this.size.width
        val height = this.size.height
        val bodyHeight = height * 0.52f
        val bodyWidth = width * 0.78f
        val bodyLeft = (width - bodyWidth) / 2f
        val bodyTop = height - bodyHeight
        drawRoundRect(
            color = color,
            topLeft = Offset(bodyLeft, bodyTop),
            size = Size(bodyWidth, bodyHeight),
            cornerRadius = CornerRadius(bodyHeight * 0.3f, bodyHeight * 0.3f)
        )
        val shackleStroke = width * 0.14f
        val shackleWidth = bodyWidth * 0.58f
        val shackleLeft = (width - shackleWidth) / 2f
        drawArc(
            color = color,
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(shackleLeft, shackleStroke / 2f),
            size = Size(shackleWidth, shackleWidth),
            style = Stroke(width = shackleStroke, cap = StrokeCap.Round)
        )
    }
}
