package com.mallet.echotiles.presentation.freeplay

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.mallet.echotiles.core.theme.TextSecondary
import com.mallet.echotiles.core.ui.ClayCard
import com.mallet.echotiles.core.ui.RoundIconButton
import com.mallet.echotiles.core.ui.ScreenBackground
import com.mallet.echotiles.core.ui.XylophoneBoard
import com.mallet.echotiles.core.ui.barDescriptions
import com.mallet.echotiles.domain.model.Instrument

@Composable
fun FreePlayRoute(onMenu: () -> Unit) {
    val factory = remember { AppViewModelFactory() }
    val viewModel: FreePlayViewModel = viewModel(
        key = "freeplay",
        factory = factory
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    DisposableEffect(Unit) {
        onDispose { viewModel.stop() }
    }
    FreePlayScreen(
        state = state,
        onBarTap = viewModel::onBarTap,
        onInstrumentSelected = viewModel::onInstrumentSelected,
        onMenu = onMenu
    )
}

@Composable
fun FreePlayScreen(
    state: FreePlayUiState,
    onBarTap: (Int) -> Unit,
    onInstrumentSelected: (Instrument) -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    ScreenBackground(
        modifier = modifier,
        overlay = listOf(
            CreamBackground.copy(alpha = 0.95f),
            CreamAlt.copy(alpha = 0.92f)
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
                    text = stringResource(R.string.free_play),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = TextPrimary,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InstrumentChip(
                    label = stringResource(R.string.instrument_xylophone),
                    active = state.instrument == Instrument.Xylophone,
                    onClick = { onInstrumentSelected(Instrument.Xylophone) },
                    modifier = Modifier.weight(1f)
                )
                InstrumentChip(
                    label = stringResource(R.string.instrument_bells),
                    active = state.instrument == Instrument.Bells,
                    onClick = { onInstrumentSelected(Instrument.Bells) },
                    modifier = Modifier.weight(1f)
                )
                InstrumentChip(
                    label = stringResource(R.string.instrument_marimba),
                    active = state.instrument == Instrument.Marimba,
                    onClick = { onInstrumentSelected(Instrument.Marimba) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(modifier = Modifier.weight(1f)) {
                XylophoneBoard(
                    barColors = BarColors,
                    litIndex = state.litBar,
                    enabled = true,
                    onBarTap = onBarTap,
                    barDescriptions = barDescriptions(),
                    ringingDescription = stringResource(R.string.state_ringing),
                    quietDescription = stringResource(R.string.state_quiet)
                )
            }
        }
    }
}

@Composable
private fun InstrumentChip(
    label: String,
    active: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selected = stringResource(R.string.state_selected)
    val notSelected = stringResource(R.string.state_not_selected)
    ClayCard(
        modifier = modifier
            .height(48.dp)
            .clickable(onClick = onClick)
            .semantics { stateDescription = if (active) selected else notSelected },
        containerColor = if (active) Sky else SurfaceWhite.copy(alpha = 0.9f),
        shadowColor = if (active) Sky else TextSecondary,
        cornerRadius = 24.dp,
        shadowElevation = if (active) 10.dp else 4.dp,
        borderColor = if (active) SurfaceWhite.copy(alpha = 0.85f) else null,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 11.sp,
                letterSpacing = 1.sp
            ),
            color = if (active) SurfaceWhite else TextSecondary
        )
    }
}
