package com.mallet.echotiles.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mallet.echotiles.core.config.GameConfig
import com.mallet.echotiles.data.audio.ToneEngine
import com.mallet.echotiles.data.sample.SampleData
import com.mallet.echotiles.domain.repository.ProgressRepository
import com.mallet.echotiles.domain.repository.SettingsRepository
import com.mallet.echotiles.domain.usecase.GetSongUseCase
import com.mallet.echotiles.domain.usecase.UnlockHatUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameOverViewModel(
    private val songIndex: Int,
    hatUnlocked: Boolean,
    getSong: GetSongUseCase,
    private val unlockHat: UnlockHatUseCase,
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository,
    private val toneEngine: ToneEngine
) : ViewModel() {

    private val phrase = getSong(songIndex).notes

    private val _state = MutableStateFlow(buildInitialState(hatUnlocked))
    val state: StateFlow<GameOverUiState> = _state.asStateFlow()

    private var playbackJob: Job? = null

    init {
        playMelody()
    }

    fun playMelody() {
        playbackJob?.cancel()
        playbackJob = viewModelScope.launch {
            delay(PLAYBACK_START_MS)
            phrase.forEachIndexed { position, note ->
                _state.update { it.copy(litIndex = position) }
                val settings = settingsRepository.settings.value
                toneEngine.playBar(note, settings.instrument, settings.volumePercent)
                delay(GameConfig.NOTE_ON_MS)
                _state.update { it.copy(litIndex = null) }
                delay(GameConfig.NOTE_GAP_MS)
            }
        }
    }

    fun stop() {
        playbackJob?.cancel()
        toneEngine.stopAll()
        _state.update { it.copy(litIndex = null) }
    }

    override fun onCleared() {
        super.onCleared()
        playbackJob?.cancel()
        toneEngine.stopAll()
    }

    private fun buildInitialState(hatUnlocked: Boolean): GameOverUiState {
        val progress = progressRepository.progress.value
        return GameOverUiState(
            songNumber = songIndex + 1,
            totalSongs = GameConfig.TOTAL_SONGS,
            phrase = phrase,
            flowers = progress.bloomedCount,
            songs = progress.bloomedCount,
            hats = unlockHat.unlockedHatCount(progress.bloomedCount),
            hatUnlocked = hatUnlocked,
            nextSongIndex = nextSongIndex()
        )
    }

    private fun nextSongIndex(): Int {
        val bloomed = progressRepository.progress.value.bloomedSongs
        val next = SampleData.SONGS.indexOfFirst { it.id !in bloomed }
        return if (next >= 0) next else (songIndex + 1) % GameConfig.TOTAL_SONGS
    }

    private companion object {
        const val PLAYBACK_START_MS = 700L
    }
}
