package com.mallet.echotiles.domain.repository

import com.mallet.echotiles.domain.model.GrownUpSettings
import com.mallet.echotiles.domain.model.Instrument
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val settings: StateFlow<GrownUpSettings>
    fun setVolume(percent: Int)
    fun setReminderMinutes(minutes: Int)
    fun markSoundCheckShown()
    fun setInstrument(instrument: Instrument)
}
