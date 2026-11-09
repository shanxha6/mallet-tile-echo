package com.mallet.echotiles.presentation.menu

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mallet.echotiles.R
import com.mallet.echotiles.core.di.AppViewModelFactory
import com.mallet.echotiles.core.theme.BarColors
import com.mallet.echotiles.core.theme.CreamAlt
import com.mallet.echotiles.core.theme.CreamBackground
import com.mallet.echotiles.core.theme.CreamSunken
import com.mallet.echotiles.core.theme.Honey
import com.mallet.echotiles.core.theme.Leaf
import com.mallet.echotiles.core.theme.Sky
import com.mallet.echotiles.core.theme.SurfaceWhite
import com.mallet.echotiles.core.theme.TextPrimary
import com.mallet.echotiles.core.theme.TextSecondary
import com.mallet.echotiles.core.theme.Tomato
import com.mallet.echotiles.core.theme.TomatoEnd
import com.mallet.echotiles.core.ui.ClayCard
import com.mallet.echotiles.core.ui.FlowerCell
import com.mallet.echotiles.core.ui.LockGlyph
import com.mallet.echotiles.core.ui.PrimaryButton
import com.mallet.echotiles.core.ui.ScreenBackground
import com.mallet.echotiles.core.ui.StatItem
import com.mallet.echotiles.core.ui.StatRow
import com.mallet.echotiles.presentation.dialog.ParentGateDialog
import com.mallet.echotiles.presentation.dialog.SoundCheckDialog

@Composable
fun MenuRoute(
    onPlay: (Int) -> Unit,
    onFreePlay: () -> Unit,
    onWardrobe: () -> Unit,
    onNoteColors: () -> Unit,
    onGrownUps: () -> Unit
) {
    val viewModel: MenuViewModel = viewModel(
        key = "menu",
        factory = AppViewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    MenuScreen(
        state = state,
        onPlay = { onPlay(state.nextSongIndex) },
        onFreePlay = onFreePlay,
        onWardrobe = onWardrobe,
        onNoteColors = onNoteColors,
        onGrownUps = onGrownUps,
        onSoundCheckTone = viewModel::onSoundCheckTone,
        onSoundCheckDismissed = viewModel::onSoundCheckDismissed
    )
}

@Composable
fun MenuScreen(
    state: MenuUiState,
    onPlay: () -> Unit,
    onFreePlay: () -> Unit,
    onWardrobe: () -> Unit,
    onNoteColors: () -> Unit,
    onGrownUps: () -> Unit,
    onSoundCheckTone: () -> Unit,
    onSoundCheckDismissed: () -> Unit,
    modifier: Modifier = Modifier
) {
    var gateVisible by remember { mutableStateOf(false) }

    ScreenBackground(
        modifier = modifier,
        overlay = listOf(
            CreamBackground.copy(alpha = 0.94f),
            CreamAlt.copy(alpha = 0.90f),
            CreamSunken.copy(alpha = 0.94f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp)
                .padding(top = 12.dp, bottom = 12.dp)
        ) {
            MenuHeader(
                bloomed = state.bloomedCount,
                total = state.totalSongs
            )
            Spacer(modifier = Modifier.height(14.dp))
            PlayTile(
                songNumber = state.nextSongIndex + 1,
                total = state.totalSongs,
                onPlay = onPlay
            )
            Spacer(modifier = Modifier.height(12.dp))
            StatRow(
                items = listOf(
                    StatItem(state.bloomedCount, stringResource(R.string.stat_songs), Tomato),
                    StatItem(state.bloomedCount, stringResource(R.string.stat_flowers), Leaf),
                    StatItem(state.unlockedHats, stringResource(R.string.stat_hats), Sky)
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            GardenGrid(
                total = state.totalSongs,
                bloomed = state.bloomedSongs,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            SideTileRow(
                onFreePlay = onFreePlay,
                onWardrobe = onWardrobe,
                onNoteColors = onNoteColors
            )
            Spacer(modifier = Modifier.height(10.dp))
            GrownUpsTile(onClick = { gateVisible = true })
        }

        if (state.showSoundCheck) {
            SoundCheckDialog(
                onPlayTone = onSoundCheckTone,
                onDismiss = onSoundCheckDismissed
            )
        }

        if (gateVisible) {
            ParentGateDialog(
                onUnlocked = {
                    gateVisible = false
                    onGrownUps()
                },
                onDismiss = { gateVisible = false }
            )
        }
    }
}

@Composable
private fun MenuHeader(bloomed: Int, total: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = stringResource(R.string.cd_logo),
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(44.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = stringResource(R.string.song_garden),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
            color = TextPrimary,
            modifier = Modifier.weight(1f)
        )
        ClayCard(
            containerColor = Honey,
            shadowColor = Honey,
            cornerRadius = 22.dp,
            shadowElevation = 8.dp,
            modifier = Modifier
                .height(48.dp)
                .semantics { liveRegion = LiveRegionMode.Polite },
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.sprite_3),
                    contentDescription = stringResource(R.string.cd_flower),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.counter_format, bloomed, total),
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                    color = TextPrimary
                )
            }
        }
    }
}

@Composable
private fun PlayTile(songNumber: Int, total: Int, onPlay: () -> Unit) {
    val transition = rememberInfiniteTransition(label = "playTile")
    val tilt by transition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "playTileTilt"
    )
    val songLabel = stringResource(R.string.song_label_format, songNumber, total)
    ClayCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .semantics { stateDescription = songLabel },
        containerColor = Tomato,
        containerBrush = Brush.linearGradient(listOf(Tomato, TomatoEnd)),
        shadowColor = Tomato,
        cornerRadius = 32.dp,
        shadowElevation = 16.dp,
        borderColor = SurfaceWhite.copy(alpha = 0.55f),
        highlightAlpha = 0.30f
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .graphicsLayer { rotationZ = tilt },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.sprite_1),
                    contentDescription = stringResource(R.string.cd_mole),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(92.dp)
                )
                Image(
                    painter = painterResource(R.drawable.sprite_5),
                    contentDescription = stringResource(R.string.cd_mallet),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.BottomEnd)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            PrimaryButton(
                text = stringResource(R.string.play),
                onClick = onPlay,
                startColor = SurfaceWhite,
                endColor = SurfaceWhite,
                labelColor = Tomato,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun GardenGrid(total: Int, bloomed: Set<Int>, modifier: Modifier = Modifier) {
    val songIds = remember(total) { (1..total).toList() }
    val bloomedLabel = stringResource(R.string.state_bloomed)
    val lockedLabel = stringResource(R.string.state_locked)
    LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(items = songIds, key = { it }) { songId ->
            FlowerCell(
                bloomed = songId in bloomed,
                bloomedDescription = bloomedLabel,
                lockedDescription = lockedLabel,
                modifier = Modifier.height(60.dp)
            )
        }
    }
}

@Composable
private fun SideTileRow(
    onFreePlay: () -> Unit,
    onWardrobe: () -> Unit,
    onNoteColors: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        SideTile(
            label = stringResource(R.string.free_play),
            containerColor = Sky,
            onClick = onFreePlay,
            modifier = Modifier.weight(1f)
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_5),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(34.dp)
            )
        }
        SideTile(
            label = stringResource(R.string.hats),
            containerColor = Honey,
            labelColor = TextPrimary,
            onClick = onWardrobe,
            modifier = Modifier.weight(1f)
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_2),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(34.dp)
            )
        }
        SideTile(
            label = stringResource(R.string.colors),
            containerColor = Leaf,
            onClick = onNoteColors,
            modifier = Modifier.weight(1f)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                BarColors.forEach { dot ->
                    Box(
                        modifier = Modifier
                            .size(9.dp)
                            .clip(CircleShape)
                            .background(dot)
                    )
                }
            }
        }
    }
}

@Composable
private fun SideTile(
    label: String,
    containerColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    labelColor: Color = SurfaceWhite,
    content: @Composable () -> Unit
) {
    ClayCard(
        modifier = modifier
            .height(92.dp)
            .clickable(onClick = onClick),
        containerColor = containerColor,
        shadowColor = containerColor,
        cornerRadius = 26.dp,
        shadowElevation = 10.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            content()
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 11.sp,
                    letterSpacing = 1.sp
                ),
                color = labelColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun GrownUpsTile(onClick: () -> Unit) {
    ClayCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(onClick = onClick),
        containerColor = CreamSunken,
        shadowColor = CreamSunken,
        cornerRadius = 20.dp,
        shadowElevation = 6.dp,
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LockGlyph(
                color = TextSecondary,
                contentDescription = stringResource(R.string.cd_lock),
                size = 20.dp
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(R.string.grown_ups_hold),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
        }
    }
}
