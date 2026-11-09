package com.mallet.echotiles.presentation.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mallet.echotiles.R
import com.mallet.echotiles.core.di.AppViewModelFactory
import com.mallet.echotiles.core.theme.BarColors
import com.mallet.echotiles.core.theme.Honey
import com.mallet.echotiles.core.theme.SurfaceWhite
import com.mallet.echotiles.core.theme.TextOnAccent
import com.mallet.echotiles.core.theme.TextPrimary
import com.mallet.echotiles.core.theme.Tomato
import com.mallet.echotiles.core.ui.ScreenBackground

@Composable
fun SplashRoute(onFinished: () -> Unit) {
    val viewModel: SplashViewModel = viewModel(
        key = "splash",
        factory = AppViewModelFactory()
    )
    val finished by viewModel.finished.collectAsStateWithLifecycle()
    LaunchedEffect(finished) {
        if (finished) onFinished()
    }
    SplashScreen()
}

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    ScreenBackground(
        modifier = modifier,
        overlay = listOf(
            Honey.copy(alpha = 0.92f),
            Tomato.copy(alpha = 0.96f)
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            NoteBlob(size = 18.dp, color = BarColors[0], startX = (-120).dp, durationMillis = 3200)
            NoteBlob(size = 14.dp, color = BarColors[2], startX = (-60).dp, durationMillis = 4000)
            NoteBlob(size = 22.dp, color = BarColors[3], startX = 0.dp, durationMillis = 3600)
            NoteBlob(size = 12.dp, color = BarColors[4], startX = 60.dp, durationMillis = 4600)
            NoteBlob(size = 20.dp, color = BarColors[5], startX = 120.dp, durationMillis = 3800)
            NoteBlob(size = 16.dp, color = SurfaceWhite, startX = 150.dp, durationMillis = 4200)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 28.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            BrandDisc()
            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = stringResource(R.string.app_title),
                style = MaterialTheme.typography.displayLarge.copy(
                    shadow = Shadow(
                        color = TextPrimary.copy(alpha = 0.45f),
                        offset = Offset(0f, 3f),
                        blurRadius = 8f
                    )
                ),
                color = TextOnAccent,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.tap_the_song_back),
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFFFFF1D6),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(36.dp))
            LinearProgressIndicator(
                color = SurfaceWhite,
                trackColor = SurfaceWhite.copy(alpha = 0.28f),
                modifier = Modifier
                    .width(190.dp)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = stringResource(R.string.loading),
                style = MaterialTheme.typography.labelSmall,
                color = SurfaceWhite.copy(alpha = 0.85f)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .safeDrawingPadding()
                .padding(bottom = 24.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            NoddingMole()
        }
    }
}

@Composable
private fun BrandDisc() {
    val transition = rememberInfiniteTransition(label = "disc")
    val breathe by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "discBreathe"
    )
    Box(
        modifier = Modifier
            .size(220.dp)
            .graphicsLayer {
                scaleX = breathe
                scaleY = breathe
            }
            .shadow(
                elevation = 14.dp,
                shape = CircleShape,
                ambientColor = Tomato,
                spotColor = Tomato
            )
            .clip(CircleShape)
            .background(SurfaceWhite)
            .background(
                Brush.verticalGradient(
                    0f to Color.White.copy(alpha = 0.55f),
                    0.5f to Color.Transparent
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = stringResource(R.string.cd_logo),
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(184.dp)
        )
    }
}

@Composable
private fun NoddingMole() {
    val transition = rememberInfiniteTransition(label = "mole")
    val tilt by transition.animateFloat(
        initialValue = -7f,
        targetValue = 7f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "moleTilt"
    )
    Image(
        painter = painterResource(R.drawable.sprite_1),
        contentDescription = stringResource(R.string.cd_mole),
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .size(96.dp)
            .graphicsLayer { rotationZ = tilt }
    )
}

@Composable
private fun NoteBlob(
    size: Dp,
    color: Color,
    startX: Dp,
    durationMillis: Int
) {
    val transition = rememberInfiniteTransition(label = "blob")
    val spec: InfiniteRepeatableSpec<Float> = infiniteRepeatable(
        animation = tween(durationMillis = durationMillis, easing = LinearEasing),
        repeatMode = RepeatMode.Restart
    )
    val travel by transition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = spec,
        label = "blobTravel"
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp, bottom = 40.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .offset(x = startX)
                .graphicsLayer {
                    translationY = -(1f - travel) * 900f
                    alpha = 0.30f + 0.25f * travel
                }
                .size(size)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.55f))
        )
    }
}
