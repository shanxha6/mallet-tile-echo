package com.mallet.echotiles.presentation.wardrobe

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.mallet.echotiles.core.theme.CreamAlt
import com.mallet.echotiles.core.theme.CreamBackground
import com.mallet.echotiles.core.theme.Honey
import com.mallet.echotiles.core.theme.LockedTile
import com.mallet.echotiles.core.theme.SurfaceWhite
import com.mallet.echotiles.core.theme.TextPrimary
import com.mallet.echotiles.core.theme.TextSecondary
import com.mallet.echotiles.core.ui.ClayCard
import com.mallet.echotiles.core.ui.HatBadge
import com.mallet.echotiles.core.ui.LockGlyph
import com.mallet.echotiles.core.ui.MoleStage
import com.mallet.echotiles.core.ui.RoundIconButton
import com.mallet.echotiles.core.ui.ScreenBackground
import androidx.compose.foundation.Image

@Composable
fun WardrobeRoute(onMenu: () -> Unit) {
    val factory = remember { AppViewModelFactory() }
    val viewModel: WardrobeViewModel = viewModel(
        key = "wardrobe",
        factory = factory
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    WardrobeScreen(
        state = state,
        onHatSelected = viewModel::onHatSelected,
        onMenu = onMenu
    )
}

@Composable
fun WardrobeScreen(
    state: WardrobeUiState,
    onHatSelected: (Int) -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                    text = stringResource(R.string.hats),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = TextPrimary,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                )
            }

            MoleStage(
                frame = 0,
                nodTick = 0,
                contentDescription = stringResource(R.string.cd_mole),
                moleSize = 140.dp,
                hatColor = state.selectedArgb?.let { Color(it) },
                modifier = Modifier.height(160.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items = state.hats, key = { it.id }) { tile ->
                    HatTileCard(tile = tile, onClick = { onHatSelected(tile.id) })
                }
            }
        }
    }
}

@Composable
private fun HatTileCard(tile: HatTile, onClick: () -> Unit) {
    val selectedLabel = stringResource(R.string.state_selected)
    val notSelectedLabel = stringResource(R.string.state_not_selected)
    val lockedLabel = stringResource(R.string.state_locked)
    ClayCard(
        modifier = Modifier
            .height(120.dp)
            .clickable(enabled = tile.unlocked, onClick = onClick)
            .semantics {
                stateDescription = when {
                    !tile.unlocked -> lockedLabel
                    tile.selected -> selectedLabel
                    else -> notSelectedLabel
                }
            },
        containerColor = if (tile.unlocked) SurfaceWhite else LockedTile,
        shadowColor = if (tile.selected) Honey else TextSecondary,
        cornerRadius = 26.dp,
        shadowElevation = if (tile.selected) 12.dp else 5.dp,
        borderColor = if (tile.selected) Honey else null,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (tile.unlocked) {
                if (tile.id == 0) {
                    Image(
                        painter = painterResource(R.drawable.sprite_1),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(52.dp)
                    )
                } else {
                    HatBadge(color = Color(tile.argb), width = 52.dp)
                }
            } else {
                LockGlyph(
                    color = TextSecondary,
                    contentDescription = stringResource(R.string.cd_lock),
                    size = 24.dp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Honey.copy(alpha = 0.8f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.hat_locked_format, tile.songsRemaining),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            letterSpacing = 0.5.sp
                        ),
                        color = TextPrimary
                    )
                }
            }
        }
    }
}
