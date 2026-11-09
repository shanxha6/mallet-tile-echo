package com.mallet.echotiles.presentation.menu

import com.mallet.echotiles.core.config.GameConfig

data class MenuUiState(
    val bloomedSongs: Set<Int> = emptySet(),
    val totalSongs: Int = GameConfig.TOTAL_SONGS,
    val unlockedHats: Int = 0,
    val nextSongIndex: Int = 0,
    val showSoundCheck: Boolean = false
) {
    val bloomedCount: Int get() = bloomedSongs.size
}
