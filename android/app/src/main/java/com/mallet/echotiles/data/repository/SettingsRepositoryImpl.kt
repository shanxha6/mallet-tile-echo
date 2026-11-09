package com.mallet.echotiles.data.repository

import com.mallet.echotiles.core.config.GameConfig
import com.mallet.echotiles.data.local.EchoPreferences
import com.mallet.echotiles.domain.model.GrownUpSettings
import com.mallet.echotiles.domain.model.Instrument
import com.mallet.echotiles.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepositoryImpl(
    private val preferences: EchoPreferences
) : SettingsRepository {

    private val _settings = MutableStateFlow(preferences.loadSettings())
    override val settings: StateFlow<GrownUpSettings> = _settings.asStateFlow()

    override fun setVolume(percent: Int) {
        val clamped = percent.coerceIn(GameConfig.MIN_VOLUME_PERCENT, GameConfig.MAX_VOLUME_PERCENT)
        store(_settings.value.copy(volumePercent = clamped))
    }

    override fun setReminderMinutes(minutes: Int) {
        store(_settings.value.copy(reminderMinutes = minutes))
    }

    override fun markSoundCheckShown() {
        store(_settings.value.copy(soundCheckShown = true))
    }

    override fun setInstrument(instrument: Instrument) {
        store(_settings.value.copy(instrument = instrument))
    }

    private fun store(value: GrownUpSettings) {
        _settings.value = value
        preferences.saveSettings(value)
    }
}
