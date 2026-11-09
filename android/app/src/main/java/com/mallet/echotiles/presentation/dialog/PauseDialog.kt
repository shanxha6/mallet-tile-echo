package com.mallet.echotiles.presentation.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mallet.echotiles.R
import com.mallet.echotiles.core.theme.Sky
import com.mallet.echotiles.core.theme.SurfaceWhite
import com.mallet.echotiles.core.theme.TextPrimary
import com.mallet.echotiles.core.theme.Tomato

@Composable
fun PauseDialog(
    onHearItAgain: () -> Unit,
    onMenu: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(28.dp),
        containerColor = SurfaceWhite,
        title = {
            Text(
                text = stringResource(R.string.paused),
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            TextButton(onClick = onHearItAgain) {
                Text(
                    text = stringResource(R.string.hear_it_again),
                    style = MaterialTheme.typography.titleLarge,
                    color = Sky
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onMenu) {
                Text(
                    text = stringResource(R.string.menu),
                    style = MaterialTheme.typography.titleLarge,
                    color = Tomato
                )
            }
        }
    )
}
