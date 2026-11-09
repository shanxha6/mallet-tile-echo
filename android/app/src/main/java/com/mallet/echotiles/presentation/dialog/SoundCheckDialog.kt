package com.mallet.echotiles.presentation.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mallet.echotiles.R
import com.mallet.echotiles.core.theme.Sky
import com.mallet.echotiles.core.theme.SurfaceWhite
import com.mallet.echotiles.core.theme.TextPrimary
import com.mallet.echotiles.core.theme.TextSecondary

@Composable
fun SoundCheckDialog(
    onPlayTone: () -> Unit,
    onDismiss: () -> Unit
) {
    val toneLabel = stringResource(R.string.preview_tone)
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(28.dp),
        containerColor = SurfaceWhite,
        title = {
            Text(
                text = stringResource(R.string.sound_check_title),
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.sound_check_body),
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .shadow(elevation = 12.dp, shape = CircleShape, ambientColor = Sky, spotColor = Sky)
                        .clip(CircleShape)
                        .background(Sky)
                        .background(
                            Brush.verticalGradient(
                                0f to Color.White.copy(alpha = 0.35f),
                                0.5f to Color.Transparent
                            )
                        )
                        .clickable(onClick = onPlayTone)
                        .semantics { contentDescription = toneLabel }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.ok),
                    style = MaterialTheme.typography.titleLarge,
                    color = Sky
                )
            }
        }
    )
}
