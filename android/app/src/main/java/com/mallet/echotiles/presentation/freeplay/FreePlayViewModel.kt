package com.mallet.echotiles.presentation.freeplay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mallet.echotiles.data.audio.ToneEngine
import com.mallet.echotiles.domain.model.Instrument
import com.mallet.echotiles.domain.repository.SettingsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FreePlayViewModel(
    private val settingsRepository: SettingsRepository,
    private val toneEngine: ToneEngine
) : ViewModel() {

    private val _state = MutableStateFlow(
        FreePlayUiState(instrument = settingsRepository.settings.value.instrument)
    )
    val state: StateFlow<FreePlayUiState> = _state.asStateFlow()

    private var releaseJob: Job? = null

    fun onInstrumentSelected(instrument: Instrument) {
        settingsRepository.setInstrument(instrument)
        _state.update { it.copy(instrument = instrument) }
    }

    fun onBarTap(barIndex: Int) {
        val settings = settingsRepository.settings.value
        toneEngine.playBar(barIndex, settings.instrument, settings.volumePercent)
        _state.update { it.copy(litBar = barIndex, playedNotes = it.playedNotes + 1) }
        releaseJob?.cancel()
        releaseJob = viewModelScope.launch {
            delay(LIT_RELEASE_MS)
            _state.update { it.copy(litBar = null) }
        }
    }

    fun stop() {
        releaseJob?.cancel()
        toneEngine.stopAll()
        _state.update { it.copy(litBar = null) }
    }

    override fun onCleared() {
        super.onCleared()
        releaseJob?.cancel()
        toneEngine.stopAll()
    }

    private companion object {
        const val LIT_RELEASE_MS = 220L
    }
}
