package com.mallet.echotiles.presentation.gameover

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mallet.echotiles.R
import com.mallet.echotiles.core.di.AppViewModelFactory
import com.mallet.echotiles.core.theme.BarColors
import com.mallet.echotiles.core.theme.CreamBackground
import com.mallet.echotiles.core.theme.Honey
import com.mallet.echotiles.core.theme.Leaf
import com.mallet.echotiles.core.theme.Sky
import com.mallet.echotiles.core.theme.SurfaceWhite
import com.mallet.echotiles.core.theme.TextOnAccent
import com.mallet.echotiles.core.theme.TextPrimary
import com.mallet.echotiles.core.theme.Tomato
import com.mallet.echotiles.core.ui.ClayCard
import com.mallet.echotiles.core.ui.PrimaryButton
import com.mallet.echotiles.core.ui.ScreenBackground
import com.mallet.echotiles.core.ui.SecondaryButton
import com.mallet.echotiles.core.ui.StatItem
import com.mallet.echotiles.core.ui.StatRow
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GameOverRoute(
    songIndex: Int,
    hatUnlocked: Boolean,
    session: Int,
    onPlayAgain: (Int) -> Unit,
    onMenu: () -> Unit
) {
    val factory = remember(songIndex, hatUnlocked) {
        AppViewModelFactory(songIndex = songIndex, hatUnlocked = hatUnlocked)
    }
    val viewModel: GameOverViewModel = viewModel(
        key = "gameover-$session",
        factory = factory
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    DisposableEffect(Unit) {
        onDispose { viewModel.stop() }
    }
    GameOverScreen(
        state = state,
        onPlayAgain = { onPlayAgain(state.nextSongIndex) },
        onMenu = onMenu
    )
}

@Composable
fun GameOverScreen(
    state: GameOverUiState,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    ScreenBackground(
        modifier = modifier,
        overlay = listOf(
            Sky.copy(alpha = 0.88f),
            Leaf.copy(alpha = 0.86f),
            CreamBackground.copy(alpha = 0.94f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp)
                .padding(top = 16.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = stringResource(R.string.song_done),
                style = MaterialTheme.typography.displayLarge.copy(
                    shadow = Shadow(
                        color = TextPrimary.copy(alpha = 0.40f),
                        offset = Offset(0f, 3f),
                        blurRadius = 8f
                    )
                ),
                color = TextOnAccent,
                textAlign = TextAlign.Center,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
            )

            BloomingFlower(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )

            MelodyTiles(
                phrase = state.phrase,
                litIndex = state.litIndex
            )

            Spacer(modifier = Modifier.height(12.dp))

            StatRow(
                items = listOf(
                    StatItem(state.flowers, stringResource(R.string.stat_flowers), Leaf),
                    StatItem(state.songs, stringResource(R.string.stat_songs), Tomato),
                    StatItem(state.hats, stringResource(R.string.stat_hats), Sky)
                )
            )

            if (state.hatUnlocked) {
                Spacer(modifier = Modifier.height(12.dp))
                HatUnlockedBanner()
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = stringResource(R.string.one_more_flower),
                style = MaterialTheme.typography.labelSmall,
                color = TextOnAccent,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            PrimaryButton(
                text = stringResource(R.string.play_again),
                onClick = onPlayAgain,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            SecondaryButton(
                text = stringResource(R.string.menu),
                onClick = onMenu,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun BloomingFlower(modifier: Modifier = Modifier) {
    val bloom = remember { Animatable(0.3f) }
    LaunchedEffect(bloom) {
        bloom.animateTo(
            targetValue = 1f,
            animationSpec = spring(dampingRatio = 0.42f, stiffness = 320f)
        )
    }
    val transition = rememberInfiniteTransition(label = "petals")
    val spin by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 14000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "petalSpin"
    )
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val discSize = minOf(maxWidth, maxHeight, 210.dp)
        Box(
            modifier = Modifier.size(discSize),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .size(discSize)
                    .graphicsLayer { rotationZ = spin }
            ) {
                val radius = size.minDimension / 2f - 6.dp.toPx()
                val center = Offset(size.width / 2f, size.height / 2f)
                repeat(8) { index ->
                    val angle = (index * 45f) * (Math.PI / 180f).toFloat()
                    drawCircle(
                        color = BarColors[index % BarColors.size].copy(alpha = 0.75f),
                        radius = 7.dp.toPx(),
                        center = Offset(
                            center.x + radius * cos(angle),
                            center.y + radius * sin(angle)
                        )
                    )
                }
            }
            Box(
                modifier = Modifier
                    .size(discSize * 0.82f)
                    .shadow(
                        elevation = 14.dp,
                        shape = CircleShape,
                        ambientColor = Leaf,
                        spotColor = Leaf
                    )
                    .clip(CircleShape)
                    .background(SurfaceWhite)
                    .background(
                        Brush.verticalGradient(
                            0f to Color.White.copy(alpha = 0.6f),
                            0.5f to Color.Transparent
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.sprite_3),
                    contentDescription = stringResource(R.string.cd_flower),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(discSize * 0.66f)
                        .graphicsLayer {
                            scaleX = bloom.value
                            scaleY = bloom.value
                            alpha = bloom.value.coerceIn(0f, 1f)
                        }
                )
            }
        }
    }
}

@Composable
private fun MelodyTiles(phrase: List<Int>, litIndex: Int?) {
    if (phrase.isEmpty()) return
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val gap = 8.dp
        val tileSize = ((maxWidth - gap * (phrase.size - 1)) / phrase.size)
            .coerceAtMost(44.dp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(gap, Alignment.CenterHorizontally)
        ) {
            phrase.forEachIndexed { index, note ->
                val scale by animateFloatAsState(
                    targetValue = if (litIndex == index) 1.25f else 1f,
                    animationSpec = tween(durationMillis = 180),
                    label = "melodyTile"
                )
                Box(
                    modifier = Modifier
                        .size(tileSize)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                        .clip(CircleShape)
                        .background(BarColors[note % BarColors.size])
                )
            }
        }
    }
}

@Composable
private fun HatUnlockedBanner() {
    ClayCard(
        modifier = Modifier.height(52.dp),
        containerColor = Honey,
        shadowColor = Honey,
        cornerRadius = 26.dp,
        shadowElevation = 10.dp,
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_2),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(R.string.hat_unlocked),
                style = MaterialTheme.typography.labelSmall,
                color = TextPrimary
            )
        }
    }
}
