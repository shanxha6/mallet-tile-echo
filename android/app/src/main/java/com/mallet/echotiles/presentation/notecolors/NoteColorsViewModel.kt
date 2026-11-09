package com.mallet.echotiles.presentation.notecolors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mallet.echotiles.data.audio.ToneEngine
import com.mallet.echotiles.domain.repository.SettingsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoteColorsViewModel(
    private val settingsRepository: SettingsRepository,
    private val toneEngine: ToneEngine
) : ViewModel() {

    private val _litBar = MutableStateFlow<Int?>(null)
    val litBar: StateFlow<Int?> = _litBar.asStateFlow()

    private var releaseJob: Job? = null

    fun onPlayNote(barIndex: Int) {
        val settings = settingsRepository.settings.value
        toneEngine.playBar(barIndex, settings.instrument, settings.volumePercent)
        _litBar.value = barIndex
        releaseJob?.cancel()
        releaseJob = viewModelScope.launch {
            delay(LIT_RELEASE_MS)
            _litBar.value = null
        }
    }

    fun stop() {
        releaseJob?.cancel()
        toneEngine.stopAll()
        _litBar.value = null
    }

    override fun onCleared() {
        super.onCleared()
        releaseJob?.cancel()
        toneEngine.stopAll()
    }

    private companion object {
        const val LIT_RELEASE_MS = 260L
    }
}
