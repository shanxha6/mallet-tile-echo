package com.mallet.echotiles.data.repository

import com.mallet.echotiles.data.local.EchoPreferences
import com.mallet.echotiles.domain.model.PlayerProgress
import com.mallet.echotiles.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProgressRepositoryImpl(
    private val preferences: EchoPreferences
) : ProgressRepository {

    private val _progress = MutableStateFlow(preferences.loadProgress())
    override val progress: StateFlow<PlayerProgress> = _progress.asStateFlow()

    override fun bloom(songId: Int) {
        val current = _progress.value
        if (songId in current.bloomedSongs) return
        val updated = current.copy(bloomedSongs = current.bloomedSongs + songId)
        _progress.value = updated
        preferences.saveProgress(updated)
    }

    override fun selectHat(hatId: Int) {
        val updated = _progress.value.copy(selectedHat = hatId)
        _progress.value = updated
        preferences.saveProgress(updated)
    }

    override fun reset() {
        val updated = PlayerProgress()
        _progress.value = updated
        preferences.saveProgress(updated)
    }
}
