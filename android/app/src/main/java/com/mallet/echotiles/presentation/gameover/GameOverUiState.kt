package com.mallet.echotiles.presentation.gameover

data class GameOverUiState(
    val songNumber: Int = 1,
    val totalSongs: Int = 1,
    val phrase: List<Int> = emptyList(),
    val litIndex: Int? = null,
    val flowers: Int = 0,
    val songs: Int = 0,
    val hats: Int = 0,
    val hatUnlocked: Boolean = false,
    val nextSongIndex: Int = 0
)
