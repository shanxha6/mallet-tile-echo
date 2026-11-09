package com.mallet.echotiles.domain.usecase

import com.mallet.echotiles.domain.repository.ProgressRepository

class BloomSongUseCase(private val progressRepository: ProgressRepository) {

    operator fun invoke(songId: Int): Int {
        progressRepository.bloom(songId)
        return progressRepository.progress.value.bloomedCount
    }
}
