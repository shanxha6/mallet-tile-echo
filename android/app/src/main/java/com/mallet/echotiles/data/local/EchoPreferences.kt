package com.mallet.echotiles.data.local

import android.content.Context
import android.content.SharedPreferences
import com.mallet.echotiles.domain.model.GrownUpSettings
import com.mallet.echotiles.domain.model.Instrument
import com.mallet.echotiles.domain.model.PlayerProgress

class EchoPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    fun loadProgress(): PlayerProgress {
        val raw = prefs.getString(KEY_BLOOMED, "").orEmpty()
        val bloomed = raw.split(SEPARATOR)
            .mapNotNull { it.trim().toIntOrNull() }
            .toSet()
        return PlayerProgress(
            bloomedSongs = bloomed,
            selectedHat = prefs.getInt(KEY_HAT, 0)
        )
    }

    fun saveProgress(progress: PlayerProgress) {
        prefs.edit()
            .putString(KEY_BLOOMED, progress.bloomedSongs.sorted().joinToString(SEPARATOR))
            .putInt(KEY_HAT, progress.selectedHat)
            .apply()
    }

    fun loadSettings(): GrownUpSettings {
        val instrumentName = prefs.getString(KEY_INSTRUMENT, Instrument.Xylophone.name)
        val instrument = Instrument.entries.firstOrNull { it.name == instrumentName }
            ?: Instrument.Xylophone
        return GrownUpSettings(
            volumePercent = prefs.getInt(KEY_VOLUME, 80),
            reminderMinutes = prefs.getInt(KEY_REMINDER, 15),
            soundCheckShown = prefs.getBoolean(KEY_SOUND_CHECK, false),
            instrument = instrument
        )
    }

    fun saveSettings(settings: GrownUpSettings) {
        prefs.edit()
            .putInt(KEY_VOLUME, settings.volumePercent)
            .putInt(KEY_REMINDER, settings.reminderMinutes)
            .putBoolean(KEY_SOUND_CHECK, settings.soundCheckShown)
            .putString(KEY_INSTRUMENT, settings.instrument.name)
            .apply()
    }

    private companion object {
        const val FILE_NAME = "mallet_tile_echo_prefs"
        const val SEPARATOR = ","
        const val KEY_BLOOMED = "bloomed_songs"
        const val KEY_HAT = "selected_hat"
        const val KEY_VOLUME = "volume_percent"
        const val KEY_REMINDER = "reminder_minutes"
        const val KEY_SOUND_CHECK = "sound_check_shown"
        const val KEY_INSTRUMENT = "instrument"
    }
}
