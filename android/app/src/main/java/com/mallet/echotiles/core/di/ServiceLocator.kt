package com.mallet.echotiles.core.di

import android.content.Context
import com.mallet.echotiles.data.audio.ToneEngine
import com.mallet.echotiles.data.local.EchoPreferences
import com.mallet.echotiles.data.repository.ProgressRepositoryImpl
import com.mallet.echotiles.data.repository.SettingsRepositoryImpl
import com.mallet.echotiles.data.sample.SampleData
import com.mallet.echotiles.domain.repository.ProgressRepository
import com.mallet.echotiles.domain.repository.SettingsRepository
import com.mallet.echotiles.domain.usecase.BloomSongUseCase
import com.mallet.echotiles.domain.usecase.CheckEchoTapUseCase
import com.mallet.echotiles.domain.usecase.GetSongUseCase
import com.mallet.echotiles.domain.usecase.UnlockHatUseCase

object ServiceLocator {

    private var progress: ProgressRepository? = null
    private var settings: SettingsRepository? = null
    private var tone: ToneEngine? = null
    private var getSong: GetSongUseCase? = null
    private var checkEchoTap: CheckEchoTapUseCase? = null
    private var bloomSong: BloomSongUseCase? = null
    private var unlockHat: UnlockHatUseCase? = null

    fun init(context: Context) {
        if (progress != null) return
        val preferences = EchoPreferences(context.applicationContext)
        val progressRepositoryImpl = ProgressRepositoryImpl(preferences)
        progress = progressRepositoryImpl
        settings = SettingsRepositoryImpl(preferences)
        tone = ToneEngine()
        getSong = GetSongUseCase(SampleData.SONGS)
        checkEchoTap = CheckEchoTapUseCase()
        bloomSong = BloomSongUseCase(progressRepositoryImpl)
        unlockHat = UnlockHatUseCase()
    }

    val progressRepository: ProgressRepository get() = requireNotNull(progress)
    val settingsRepository: SettingsRepository get() = requireNotNull(settings)
    val toneEngine: ToneEngine get() = requireNotNull(tone)
    val getSongUseCase: GetSongUseCase get() = requireNotNull(getSong)
    val checkEchoTapUseCase: CheckEchoTapUseCase get() = requireNotNull(checkEchoTap)
    val bloomSongUseCase: BloomSongUseCase get() = requireNotNull(bloomSong)
    val unlockHatUseCase: UnlockHatUseCase get() = requireNotNull(unlockHat)
}
