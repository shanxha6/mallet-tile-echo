package com.mallet.echotiles.presentation.notecolors

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mallet.echotiles.R
import com.mallet.echotiles.core.di.AppViewModelFactory
import com.mallet.echotiles.core.theme.BarColors
import com.mallet.echotiles.core.theme.CreamAlt
import com.mallet.echotiles.core.theme.CreamBackground
import com.mallet.echotiles.core.theme.Sky
import com.mallet.echotiles.core.theme.SurfaceWhite
import com.mallet.echotiles.core.theme.TextPrimary
import com.mallet.echotiles.core.ui.ClayCard
import com.mallet.echotiles.core.ui.RoundIconButton
import com.mallet.echotiles.core.ui.ScreenBackground
import com.mallet.echotiles.core.ui.barDescriptions
import com.mallet.echotiles.core.ui.noteNames
import androidx.compose.foundation.clickable

@Composable
fun NoteColorsRoute(onMenu: () -> Unit) {
    val factory = remember { AppViewModelFactory() }
    val viewModel: NoteColorsViewModel = viewModel(
        key = "notecolors",
        factory = factory
    )
    val litBar by viewModel.litBar.collectAsStateWithLifecycle()
    DisposableEffect(Unit) {
        onDispose { viewModel.stop() }
    }
    NoteColorsScreen(
        litBar = litBar,
        onPlayNote = viewModel::onPlayNote,
        onMenu = onMenu
    )
}

@Composable
fun NoteColorsScreen(
    litBar: Int?,
    onPlayNote: (Int) -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val names = noteNames()
    val descriptions = barDescriptions()
    val ringing = stringResource(R.string.state_ringing)
    val quiet = stringResource(R.string.state_quiet)

    ScreenBackground(
        modifier = modifier,
        overlay = listOf(
            CreamBackground.copy(alpha = 0.95f),
            CreamAlt.copy(alpha = 0.93f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp, bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RoundIconButton(
                    painter = painterResource(R.drawable.icon_home),
                    contentDescription = stringResource(R.string.cd_home),
                    onClick = onMenu
                )
                Text(
                    text = stringResource(R.string.colors),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = TextPrimary,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                BarColors.forEachIndexed { index, color ->
                    NoteRow(
                        color = color,
                        name = names[index],
                        description = descriptions[index],
                        stateLabel = if (litBar == index) ringing else quiet,
                        lit = litBar == index,
                        onPlay = { onPlayNote(index) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun NoteRow(
    color: Color,
    name: String,
    description: String,
    stateLabel: String,
    lit: Boolean,
    onPlay: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (lit) 1.04f else 1f,
        animationSpec = tween(durationMillis = 180),
        label = "noteRowScale"
    )
    ClayCard(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .semantics { stateDescription = stateLabel },
        containerColor = SurfaceWhite.copy(alpha = 0.92f),
        shadowColor = color,
        cornerRadius = 26.dp,
        shadowElevation = 8.dp,
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(64.dp)
                    .fillMaxHeight(0.55f)
                    .clip(RoundedCornerShape(18.dp))
                    .background(color)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 36.sp),
                color = color,
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Sky)
                    .clickable(onClick = onPlay)
                    .semantics { stateDescription = description },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(SurfaceWhite)
                )
            }
        }
    }
}
