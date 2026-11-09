package com.mallet.echotiles.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mallet.echotiles.presentation.app.AppViewModel
import com.mallet.echotiles.presentation.freeplay.FreePlayViewModel
import com.mallet.echotiles.presentation.game.GameViewModel
import com.mallet.echotiles.presentation.gameover.GameOverViewModel
import com.mallet.echotiles.presentation.grownups.GrownUpsViewModel
import com.mallet.echotiles.presentation.menu.MenuViewModel
import com.mallet.echotiles.presentation.notecolors.NoteColorsViewModel
import com.mallet.echotiles.presentation.splash.SplashViewModel
import com.mallet.echotiles.presentation.wardrobe.WardrobeViewModel

class AppViewModelFactory(
    private val songIndex: Int = 0,
    private val hatUnlocked: Boolean = false
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val created: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) -> AppViewModel()

            modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel()

            modelClass.isAssignableFrom(MenuViewModel::class.java) -> MenuViewModel(
                progressRepository = ServiceLocator.progressRepository,
                settingsRepository = ServiceLocator.settingsRepository,
                unlockHat = ServiceLocator.unlockHatUseCase,
                toneEngine = ServiceLocator.toneEngine
            )

            modelClass.isAssignableFrom(GameViewModel::class.java) -> GameViewModel(
                songIndex = songIndex,
                getSong = ServiceLocator.getSongUseCase,
                checkEchoTap = ServiceLocator.checkEchoTapUseCase,
                bloomSong = ServiceLocator.bloomSongUseCase,
                unlockHat = ServiceLocator.unlockHatUseCase,
                progressRepository = ServiceLocator.progressRepository,
                settingsRepository = ServiceLocator.settingsRepository,
                toneEngine = ServiceLocator.toneEngine
            )

            modelClass.isAssignableFrom(GameOverViewModel::class.java) -> GameOverViewModel(
                songIndex = songIndex,
                hatUnlocked = hatUnlocked,
                getSong = ServiceLocator.getSongUseCase,
                unlockHat = ServiceLocator.unlockHatUseCase,
                progressRepository = ServiceLocator.progressRepository,
                settingsRepository = ServiceLocator.settingsRepository,
                toneEngine = ServiceLocator.toneEngine
            )

            modelClass.isAssignableFrom(FreePlayViewModel::class.java) -> FreePlayViewModel(
                settingsRepository = ServiceLocator.settingsRepository,
                toneEngine = ServiceLocator.toneEngine
            )

            modelClass.isAssignableFrom(WardrobeViewModel::class.java) -> WardrobeViewModel(
                progressRepository = ServiceLocator.progressRepository
            )

            modelClass.isAssignableFrom(NoteColorsViewModel::class.java) -> NoteColorsViewModel(
                settingsRepository = ServiceLocator.settingsRepository,
                toneEngine = ServiceLocator.toneEngine
            )

            modelClass.isAssignableFrom(GrownUpsViewModel::class.java) -> GrownUpsViewModel(
                settingsRepository = ServiceLocator.settingsRepository,
                progressRepository = ServiceLocator.progressRepository,
                toneEngine = ServiceLocator.toneEngine
            )

            else -> throw IllegalArgumentException(modelClass.name)
        }
        return created as T
    }
}
