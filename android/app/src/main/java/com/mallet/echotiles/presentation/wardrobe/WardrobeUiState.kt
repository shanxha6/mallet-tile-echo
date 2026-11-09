package com.mallet.echotiles.presentation.wardrobe

data class HatTile(
    val id: Int,
    val argb: Long,
    val requiredSongs: Int,
    val unlocked: Boolean,
    val selected: Boolean,
    val songsRemaining: Int
)

data class WardrobeUiState(
    val hats: List<HatTile> = emptyList(),
    val selectedArgb: Long? = null,
    val bloomedCount: Int = 0
)
