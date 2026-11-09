package com.mallet.echotiles.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mallet.echotiles.core.config.GameConfig
import com.mallet.echotiles.data.audio.ToneEngine
import com.mallet.echotiles.data.sample.SampleData
import com.mallet.echotiles.domain.repository.ProgressRepository
import com.mallet.echotiles.domain.repository.SettingsRepository
import com.mallet.echotiles.domain.usecase.UnlockHatUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MenuViewModel(
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository,
    private val unlockHat: UnlockHatUseCase,
    private val toneEngine: ToneEngine
) : ViewModel() {

    val state: StateFlow<MenuUiState> =
        combine(progressRepository.progress, settingsRepository.settings) { progress, settings ->
            MenuUiState(
                bloomedSongs = progress.bloomedSongs,
                totalSongs = GameConfig.TOTAL_SONGS,
                unlockedHats = unlockHat.unlockedHatCount(progress.bloomedCount),
                nextSongIndex = nextSongIndex(progress.bloomedSongs),
                showSoundCheck = !settings.soundCheckShown
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = MenuUiState()
        )

    fun onSoundCheckTone() {
        val settings = settingsRepository.settings.value
        toneEngine.playBar(0, settings.instrument, settings.volumePercent)
    }

    fun onSoundCheckDismissed() {
        settingsRepository.markSoundCheckShown()
    }

    private fun nextSongIndex(bloomed: Set<Int>): Int {
        val next = SampleData.SONGS.indexOfFirst { it.id !in bloomed }
        return if (next >= 0) next else 0
    }
}
