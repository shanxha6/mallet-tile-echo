package com.mallet.echotiles.domain.model

data class PlayerProgress(
    val bloomedSongs: Set<Int> = emptySet(),
    val selectedHat: Int = 0
) {
    val bloomedCount: Int get() = bloomedSongs.size
}
