package com.mallet.echotiles.presentation.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mallet.echotiles.R
import com.mallet.echotiles.core.di.AppViewModelFactory
import com.mallet.echotiles.core.theme.BarColors
import com.mallet.echotiles.core.theme.CreamBackground
import com.mallet.echotiles.core.theme.SurfaceWhite
import com.mallet.echotiles.core.theme.TextSecondary
import com.mallet.echotiles.core.theme.WarmGlow
import com.mallet.echotiles.core.ui.ClayCard
import com.mallet.echotiles.core.ui.MoleStage
import com.mallet.echotiles.core.ui.PhraseDots
import com.mallet.echotiles.core.ui.RoundIconButton
import com.mallet.echotiles.core.ui.ScreenBackground
import com.mallet.echotiles.core.ui.SecondaryButton
import com.mallet.echotiles.core.ui.XylophoneBoard
import com.mallet.echotiles.core.ui.barDescriptions
import com.mallet.echotiles.domain.model.GamePhase
import com.mallet.echotiles.presentation.dialog.PauseDialog

@Composable
fun GameRoute(
    songIndex: Int,
    session: Int,
    onFinished: (Int, Boolean) -> Unit,
    onMenu: () -> Unit
) {
    val factory = remember(songIndex) { AppViewModelFactory(songIndex = songIndex) }
    val viewModel: GameViewModel = viewModel(
        key = "game-$session",
        factory = factory
    )
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is GameEvent.Finished -> onFinished(event.songIndex, event.hatUnlocked)
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose { viewModel.pause() }
    }

    GameScreen(
        state = state,
        onBarTap = viewModel::onBarTap,
        onReplay = viewModel::replayPhrase,
        onPause = viewModel::pause,
        onMenu = onMenu
    )
}

@Composable
fun GameScreen(
    state: GameUiState,
    onBarTap: (Int) -> Unit,
    onReplay: () -> Unit,
    onPause: () -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    var paused by remember { mutableStateOf(false) }
    val progressLabel = stringResource(
        R.string.note_progress_format,
        state.filled,
        state.phrase.size
    )
    val demoRunning = state.phase == GamePhase.Demo ||
        state.phase == GamePhase.Intro ||
        state.phase == GamePhase.Retry

    ScreenBackground(
        modifier = modifier,
        overlay = listOf(
            CreamBackground.copy(alpha = 0.96f),
            WarmGlow.copy(alpha = 0.92f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp, bottom = 12.dp)
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
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .semantics {
                            liveRegion = LiveRegionMode.Polite
                            stateDescription = progressLabel
                        },
                    contentAlignment = Alignment.Center
                ) {
                    PhraseDots(
                        notes = state.phrase,
                        filled = state.filled,
                        activeIndex = state.demoIndex,
                        barColors = BarColors
                    )
                }
                RoundIconButton(
                    painter = painterResource(R.drawable.icon_pause),
                    contentDescription = stringResource(R.string.cd_pause),
                    onClick = {
                        onPause()
                        paused = true
                    }
                )
            }

            MoleStage(
                frame = state.moleFrame,
                nodTick = state.nodTick,
                contentDescription = stringResource(R.string.cd_mole),
                moleSize = 120.dp,
                hatColor = state.hatArgb?.let { Color(it) },
                modifier = Modifier.height(140.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.weight(1f)) {
                XylophoneBoard(
                    barColors = BarColors,
                    litIndex = state.litBar,
                    enabled = !state.inputLocked,
                    onBarTap = onBarTap,
                    barDescriptions = barDescriptions(),
                    ringingDescription = stringResource(R.string.state_ringing),
                    quietDescription = stringResource(R.string.state_quiet)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.listen_then_tap),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            ClayCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp),
                containerColor = SurfaceWhite.copy(alpha = 0.92f),
                cornerRadius = 36.dp,
                shadowElevation = 14.dp,
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SecondaryButton(
                        text = stringResource(R.string.hear_it_again),
                        onClick = onReplay,
                        enabled = !demoRunning && state.phase != GamePhase.Finished,
                        modifier = Modifier.weight(1f)
                    )
                    RoundIconButton(
                        painter = painterResource(R.drawable.icon_home),
                        contentDescription = stringResource(R.string.cd_home),
                        onClick = onMenu,
                        modifier = Modifier.width(56.dp)
                    )
                }
            }
        }

        if (paused) {
            PauseDialog(
                onHearItAgain = {
                    paused = false
                    onReplay()
                },
                onMenu = onMenu,
                onDismiss = {
                    paused = false
                    onReplay()
                }
            )
        }
    }
}
