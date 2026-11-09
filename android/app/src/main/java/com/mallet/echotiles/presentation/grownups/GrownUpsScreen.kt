package com.mallet.echotiles.presentation.grownups

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mallet.echotiles.R
import com.mallet.echotiles.core.config.GameConfig
import com.mallet.echotiles.core.theme.CreamAlt
import com.mallet.echotiles.core.theme.CreamBackground
import com.mallet.echotiles.core.theme.CreamSunken
import com.mallet.echotiles.core.theme.Honey
import com.mallet.echotiles.core.theme.Sky
import com.mallet.echotiles.core.theme.SurfaceWhite
import com.mallet.echotiles.core.theme.TextPrimary
import com.mallet.echotiles.core.theme.TextSecondary
import com.mallet.echotiles.core.theme.Tomato
import com.mallet.echotiles.core.di.AppViewModelFactory
import com.mallet.echotiles.core.ui.ClayCard
import com.mallet.echotiles.core.ui.RoundIconButton
import com.mallet.echotiles.core.ui.ScreenBackground
import com.mallet.echotiles.core.ui.SecondaryButton
import com.mallet.echotiles.presentation.dialog.ResetProgressDialog

private val ReminderOptions = listOf(10, 15, 20, 30)

private val TipResources = listOf(
    R.string.tip_one,
    R.string.tip_two,
    R.string.tip_three,
    R.string.tip_four
)

@Composable
fun GrownUpsRoute(onMenu: () -> Unit) {
    val factory = remember { AppViewModelFactory() }
    val viewModel: GrownUpsViewModel = viewModel(
        key = "grownups",
        factory = factory
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    GrownUpsScreen(
        state = state,
        onVolumeChanged = viewModel::onVolumeChanged,
        onVolumePreview = viewModel::onVolumePreview,
        onReminderSelected = viewModel::onReminderSelected,
        onResetRequested = viewModel::onResetRequested,
        onResetDismissed = viewModel::onResetDismissed,
        onResetConfirmed = viewModel::onResetConfirmed,
        onMenu = onMenu
    )
}

@Composable
fun GrownUpsScreen(
    state: GrownUpsUiState,
    onVolumeChanged: (Int) -> Unit,
    onVolumePreview: () -> Unit,
    onReminderSelected: (Int) -> Unit,
    onResetRequested: () -> Unit,
    onResetDismissed: () -> Unit,
    onResetConfirmed: () -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    ScreenBackground(
        modifier = modifier,
        overlay = listOf(
            CreamBackground.copy(alpha = 0.96f),
            CreamAlt.copy(alpha = 0.94f)
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
                    text = stringResource(R.string.grown_ups),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = TextPrimary,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                )
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    VolumeBlock(
                        volumePercent = state.volumePercent,
                        onVolumeChanged = onVolumeChanged,
                        onVolumePreview = onVolumePreview
                    )
                }
                item {
                    ReminderBlock(
                        selectedMinutes = state.reminderMinutes,
                        onReminderSelected = onReminderSelected
                    )
                }
                item {
                    SectionTitle(text = stringResource(R.string.listening_tips))
                }
                items(items = TipResources) { tipRes ->
                    ClayCard(
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = SurfaceWhite,
                        shadowColor = TextSecondary,
                        cornerRadius = 24.dp,
                        shadowElevation = 6.dp
                    ) {
                        Text(
                            text = stringResource(tipRes),
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 17.sp),
                            color = TextPrimary,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                item {
                    OutlinedButton(
                        onClick = onResetRequested,
                        shape = RoundedCornerShape(26.dp),
                        border = BorderStroke(2.dp, Tomato),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = SurfaceWhite.copy(alpha = 0.9f),
                            contentColor = Tomato
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.reset_progress),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
        }

        if (state.showResetDialog) {
            ResetProgressDialog(
                onConfirm = onResetConfirmed,
                onDismiss = onResetDismissed
            )
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = TextSecondary,
        modifier = Modifier.padding(start = 4.dp)
    )
}

@Composable
private fun VolumeBlock(
    volumePercent: Int,
    onVolumeChanged: (Int) -> Unit,
    onVolumePreview: () -> Unit
) {
    ClayCard(
        modifier = Modifier.fillMaxWidth(),
        containerColor = SurfaceWhite,
        shadowColor = TextSecondary,
        cornerRadius = 24.dp,
        shadowElevation = 6.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.volume_limit),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(Honey)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .semantics { liveRegion = LiveRegionMode.Polite }
                ) {
                    Text(
                        text = stringResource(R.string.volume_format, volumePercent),
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
                        color = TextPrimary
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Slider(
                value = volumePercent.toFloat(),
                onValueChange = { onVolumeChanged(it.toInt()) },
                valueRange = GameConfig.MIN_VOLUME_PERCENT.toFloat()..
                    GameConfig.MAX_VOLUME_PERCENT.toFloat(),
                colors = SliderDefaults.colors(
                    thumbColor = Tomato,
                    activeTrackColor = Tomato,
                    inactiveTrackColor = CreamSunken
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            SecondaryButton(
                text = stringResource(R.string.preview_tone),
                onClick = onVolumePreview,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ReminderBlock(
    selectedMinutes: Int,
    onReminderSelected: (Int) -> Unit
) {
    val selectedLabel = stringResource(R.string.state_selected)
    val notSelectedLabel = stringResource(R.string.state_not_selected)
    ClayCard(
        modifier = Modifier.fillMaxWidth(),
        containerColor = SurfaceWhite,
        shadowColor = TextSecondary,
        cornerRadius = 24.dp,
        shadowElevation = 6.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.session_reminder),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ReminderOptions.forEach { minutes ->
                    val active = minutes == selectedMinutes
                    ClayCard(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clickable { onReminderSelected(minutes) }
                            .semantics {
                                stateDescription = if (active) selectedLabel else notSelectedLabel
                            },
                        containerColor = if (active) Sky else CreamSunken,
                        shadowColor = if (active) Sky else CreamSunken,
                        cornerRadius = 24.dp,
                        shadowElevation = if (active) 8.dp else 2.dp,
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.minutes_format, minutes),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                letterSpacing = 0.5.sp
                            ),
                            color = if (active) SurfaceWhite else TextSecondary
                        )
                    }
                }
            }
        }
    }
}
