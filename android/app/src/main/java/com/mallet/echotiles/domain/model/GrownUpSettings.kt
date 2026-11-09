package com.mallet.echotiles.domain.model

data class GrownUpSettings(
    val volumePercent: Int = 80,
    val reminderMinutes: Int = 15,
    val soundCheckShown: Boolean = false,
    val instrument: Instrument = Instrument.Xylophone
)
