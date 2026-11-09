package com.mallet.echotiles.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mallet.echotiles.core.config.GameConfig
import com.mallet.echotiles.data.audio.ToneEngine
import com.mallet.echotiles.data.sample.SampleData
import com.mallet.echotiles.domain.model.GamePhase
import com.mallet.echotiles.domain.model.Song
import com.mallet.echotiles.domain.repository.ProgressRepository
import com.mallet.echotiles.domain.repository.SettingsRepository
import com.mallet.echotiles.domain.usecase.BloomSongUseCase
import com.mallet.echotiles.domain.usecase.CheckEchoTapUseCase
import com.mallet.echotiles.domain.usecase.GetSongUseCase
import com.mallet.echotiles.domain.usecase.UnlockHatUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.min

sealed interface GameEvent {
    data class Finished(val songIndex: Int, val hatUnlocked: Boolean) : GameEvent
}

class GameViewModel(
    private val songIndex: Int,
    private val getSong: GetSongUseCase,
    private val checkEchoTap: CheckEchoTapUseCase,
    private val bloomSong: BloomSongUseCase,
    private val unlockHat: UnlockHatUseCase,
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository,
    private val toneEngine: ToneEngine
) : ViewModel() {

    private val song: Song = getSong(songIndex)

    private val _state = MutableStateFlow(
        GameUiState(
            songNumber = songIndex + 1,
            totalSongs = GameConfig.TOTAL_SONGS,
            phrase = song.notes,
            hatArgb = selectedHatArgb()
        )
    )
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val eventChannel = Channel<GameEvent>(Channel.BUFFERED)
    val events: Flow<GameEvent> = eventChannel.receiveAsFlow()

    private var loopJob: Job? = null
    private var releaseJob: Job? = null

    init {
        startRound()
    }

    fun replayPhrase() {
        if (_state.value.phase == GamePhase.Finished) return
        runDemoJob(introDelay = 0L)
    }

    fun onBarTap(barIndex: Int) {
        val current = _state.value
        if (current.inputLocked) return
        val expected = current.phrase.getOrNull(current.filled) ?: return
        val accepted = current.guided || checkEchoTap(current.phrase, current.filled, barIndex)
        if (!accepted) {
            playNote(barIndex)
            retryRound()
            return
        }
        val sounded = if (current.guided) expected else barIndex
        playNote(sounded)
        val filled = current.filled + 1
        _state.update { it.copy(filled = filled, litBar = sounded, nodTick = it.nodTick + 1) }
        clearLitSoon()
        if (filled >= current.phrase.size) {
            finishRound()
        }
    }

    fun pause() {
        loopJob?.cancel()
        releaseJob?.cancel()
        toneEngine.stopAll()
        _state.update { it.copy(inputLocked = true, litBar = null, demoIndex = null) }
    }

    override fun onCleared() {
        super.onCleared()
        loopJob?.cancel()
        releaseJob?.cancel()
        toneEngine.stopAll()
    }

    private fun startRound() {
        runDemoJob(introDelay = GameConfig.DEMO_START_DELAY_MS)
    }

    private fun runDemoJob(introDelay: Long) {
        loopJob?.cancel()
        loopJob = viewModelScope.launch {
            _state.update {
                it.copy(
                    phase = GamePhase.Intro,
                    inputLocked = true,
                    filled = 0,
                    litBar = null,
                    demoIndex = null,
                    moleFrame = 0
                )
            }
            if (introDelay > 0L) delay(introDelay)
            playDemo()
        }
    }

    private suspend fun playDemo() {
        val phrase = _state.value.phrase
        val factor = slowFactor()
        _state.update { it.copy(phase = GamePhase.Demo, inputLocked = true, filled = 0) }
        phrase.forEachIndexed { position, note ->
            _state.update {
                it.copy(litBar = note, demoIndex = position, nodTick = it.nodTick + 1)
            }
            playNote(note)
            delay((GameConfig.NOTE_ON_MS * factor).toLong())
            _state.update { it.copy(litBar = null) }
            delay((GameConfig.NOTE_GAP_MS * factor).toLong())
        }
        _state.update {
            it.copy(phase = GamePhase.Echo, inputLocked = false, demoIndex = null, litBar = null)
        }
    }

    private fun retryRound() {
        loopJob?.cancel()
        loopJob = viewModelScope.launch {
            _state.update {
                val mistakes = it.mistakes + 1
                it.copy(
                    phase = GamePhase.Retry,
                    inputLocked = true,
                    filled = 0,
                    litBar = null,
                    demoIndex = null,
                    mistakes = mistakes,
                    guided = mistakes >= GameConfig.GUIDE_AFTER_MISTAKES
                )
            }
            delay(GameConfig.RETRY_PAUSE_MS)
            playDemo()
        }
    }

    private fun finishRound() {
        loopJob?.cancel()
        loopJob = viewModelScope.launch {
            _state.update {
                it.copy(phase = GamePhase.Finished, inputLocked = true, litBar = null)
            }
            repeat(GameConfig.CLAP_CYCLES * 2) {
                _state.update { current ->
                    current.copy(moleFrame = if (current.moleFrame == 0) 1 else 0)
                }
                delay(GameConfig.CLAP_FRAME_MS)
            }
            _state.update { it.copy(moleFrame = 0) }
            val bloomedCount = bloomSong(song.id)
            eventChannel.send(
                GameEvent.Finished(
                    songIndex = songIndex,
                    hatUnlocked = unlockHat(bloomedCount)
                )
            )
        }
    }

    private fun clearLitSoon() {
        releaseJob?.cancel()
        releaseJob = viewModelScope.launch {
            delay(LIT_RELEASE_MS)
            _state.update { it.copy(litBar = null) }
        }
    }

    private fun slowFactor(): Float = min(
        1f + GameConfig.SLOW_FACTOR_STEP * _state.value.mistakes,
        GameConfig.SLOW_FACTOR_MAX
    )

    private fun playNote(barIndex: Int) {
        val settings = settingsRepository.settings.value
        toneEngine.playBar(barIndex, settings.instrument, settings.volumePercent)
    }

    private fun selectedHatArgb(): Long? {
        val hatId = progressRepository.progress.value.selectedHat
        if (hatId <= 0) return null
        return SampleData.HATS.firstOrNull { it.id == hatId }?.argb
    }

    private companion object {
        const val LIT_RELEASE_MS = 220L
    }
}
