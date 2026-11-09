package com.mallet.echotiles.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.mallet.echotiles.R
import com.mallet.echotiles.core.theme.CreamBackground

@Composable
fun ScreenBackground(
    overlay: List<Color>,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground)
    ) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Brush.verticalGradient(overlay))
        )
        content()
    }
}
