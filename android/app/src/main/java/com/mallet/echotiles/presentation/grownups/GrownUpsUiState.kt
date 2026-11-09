package com.mallet.echotiles.presentation.grownups

data class GrownUpsUiState(
    val volumePercent: Int = 80,
    val reminderMinutes: Int = 15,
    val bloomedCount: Int = 0,
    val showResetDialog: Boolean = false
)
