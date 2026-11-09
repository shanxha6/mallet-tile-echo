package com.mallet.echotiles.domain.repository

import com.mallet.echotiles.domain.model.PlayerProgress
import kotlinx.coroutines.flow.StateFlow

interface ProgressRepository {
    val progress: StateFlow<PlayerProgress>
    fun bloom(songId: Int)
    fun selectHat(hatId: Int)
    fun reset()
}
