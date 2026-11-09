package com.mallet.echotiles.core.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.mallet.echotiles.R
import com.mallet.echotiles.core.theme.CreamSunken
import com.mallet.echotiles.core.theme.Leaf
import com.mallet.echotiles.core.theme.SurfaceWhite

@Composable
fun FlowerCell(
    bloomed: Boolean,
    bloomedDescription: String,
    lockedDescription: String,
    modifier: Modifier = Modifier
) {
    val scale = remember { Animatable(if (bloomed) 1f else 0.9f) }
    LaunchedEffect(bloomed) {
        if (bloomed) {
            scale.snapTo(0.6f)
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(dampingRatio = 0.42f, stiffness = 320f)
            )
        } else {
            scale.snapTo(0.9f)
        }
    }
    ClayCard(
        modifier = modifier.semantics {
            stateDescription = if (bloomed) bloomedDescription else lockedDescription
        },
        containerColor = if (bloomed) SurfaceWhite else CreamSunken,
        shadowColor = if (bloomed) Leaf else CreamSunken,
        cornerRadius = 22.dp,
        shadowElevation = if (bloomed) 8.dp else 2.dp,
        highlightAlpha = if (bloomed) 0.45f else 0.18f,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(
                    if (bloomed) R.drawable.sprite_3 else R.drawable.sprite_4
                ),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(if (bloomed) 40.dp else 32.dp)
                    .graphicsLayer {
                        scaleX = scale.value
                        scaleY = scale.value
                        alpha = if (bloomed) 1f else 0.55f
                    }
            )
        }
    }
}
