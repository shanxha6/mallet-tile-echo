package com.mallet.echotiles.presentation.game

import com.mallet.echotiles.domain.model.GamePhase

data class GameUiState(
    val songNumber: Int = 1,
    val totalSongs: Int = 1,
    val phrase: List<Int> = emptyList(),
    val filled: Int = 0,
    val litBar: Int? = null,
    val demoIndex: Int? = null,
    val phase: GamePhase = GamePhase.Intro,
    val inputLocked: Boolean = true,
    val mistakes: Int = 0,
    val guided: Boolean = false,
    val moleFrame: Int = 0,
    val nodTick: Int = 0,
    val hatArgb: Long? = null
)
