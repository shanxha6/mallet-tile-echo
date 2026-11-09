package com.mallet.echotiles.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mallet.echotiles.core.theme.Sky
import com.mallet.echotiles.core.theme.SurfaceWhite

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    labelColor: Color = Sky
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(26.dp),
        border = BorderStroke(2.dp, SurfaceWhite.copy(alpha = if (enabled) 0.95f else 0.4f)),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = SurfaceWhite.copy(alpha = 0.85f),
            contentColor = labelColor,
            disabledContainerColor = SurfaceWhite.copy(alpha = 0.45f),
            disabledContentColor = labelColor.copy(alpha = 0.45f)
        ),
        modifier = modifier.height(52.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge
        )
    }
}
