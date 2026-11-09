package com.mallet.echotiles.presentation.wardrobe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mallet.echotiles.data.sample.SampleData
import com.mallet.echotiles.domain.model.PlayerProgress
import com.mallet.echotiles.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class WardrobeViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    val state: StateFlow<WardrobeUiState> = progressRepository.progress
        .map { progress -> buildState(progress) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = buildState(progressRepository.progress.value)
        )

    fun onHatSelected(hatId: Int) {
        val hat = SampleData.HATS.firstOrNull { it.id == hatId } ?: return
        if (progressRepository.progress.value.bloomedCount < hat.requiredSongs) return
        progressRepository.selectHat(hatId)
    }

    private fun buildState(progress: PlayerProgress): WardrobeUiState {
        val bloomed = progress.bloomedCount
        val tiles = SampleData.HATS.map { hat ->
            val unlocked = bloomed >= hat.requiredSongs
            HatTile(
                id = hat.id,
                argb = hat.argb,
                requiredSongs = hat.requiredSongs,
                unlocked = unlocked,
                selected = progress.selectedHat == hat.id,
                songsRemaining = (hat.requiredSongs - bloomed).coerceAtLeast(0)
            )
        }
        val selected = tiles.firstOrNull { it.selected && it.id > 0 }
        return WardrobeUiState(
            hats = tiles,
            selectedArgb = selected?.argb,
            bloomedCount = bloomed
        )
    }
}
