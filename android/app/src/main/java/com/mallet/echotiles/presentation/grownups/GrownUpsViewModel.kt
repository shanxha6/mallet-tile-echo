package com.mallet.echotiles.presentation.grownups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mallet.echotiles.data.audio.ToneEngine
import com.mallet.echotiles.domain.repository.ProgressRepository
import com.mallet.echotiles.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class GrownUpsViewModel(
    private val settingsRepository: SettingsRepository,
    private val progressRepository: ProgressRepository,
    private val toneEngine: ToneEngine
) : ViewModel() {

    private val _showResetDialog = MutableStateFlow(false)

    val state: StateFlow<GrownUpsUiState> = combine(
        settingsRepository.settings,
        progressRepository.progress,
        _showResetDialog
    ) { settings, progress, showReset ->
        GrownUpsUiState(
            volumePercent = settings.volumePercent,
            reminderMinutes = settings.reminderMinutes,
            bloomedCount = progress.bloomedCount,
            showResetDialog = showReset
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = GrownUpsUiState(
            volumePercent = settingsRepository.settings.value.volumePercent,
            reminderMinutes = settingsRepository.settings.value.reminderMinutes,
            bloomedCount = progressRepository.progress.value.bloomedCount
        )
    )

    fun onVolumeChanged(percent: Int) {
        settingsRepository.setVolume(percent)
    }

    fun onVolumePreview() {
        val settings = settingsRepository.settings.value
        toneEngine.playBar(4, settings.instrument, settings.volumePercent)
    }

    fun onReminderSelected(minutes: Int) {
        settingsRepository.setReminderMinutes(minutes)
    }

    fun onResetRequested() {
        _showResetDialog.value = true
    }

    fun onResetDismissed() {
        _showResetDialog.value = false
    }

    fun onResetConfirmed() {
        progressRepository.reset()
        _showResetDialog.value = false
    }

    override fun onCleared() {
        super.onCleared()
        toneEngine.stopAll()
    }
}
