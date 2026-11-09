package com.mallet.echotiles.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mallet.echotiles.core.di.AppViewModelFactory
import com.mallet.echotiles.core.navigation.Screen
import com.mallet.echotiles.presentation.freeplay.FreePlayRoute
import com.mallet.echotiles.presentation.game.GameRoute
import com.mallet.echotiles.presentation.gameover.GameOverRoute
import com.mallet.echotiles.presentation.grownups.GrownUpsRoute
import com.mallet.echotiles.presentation.menu.MenuRoute
import com.mallet.echotiles.presentation.notecolors.NoteColorsRoute
import com.mallet.echotiles.presentation.splash.SplashRoute
import com.mallet.echotiles.presentation.wardrobe.WardrobeRoute

@Composable
fun AppRoot() {
    val factory = remember { AppViewModelFactory() }
    val appViewModel: AppViewModel = viewModel(key = "app", factory = factory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val canGoBack by appViewModel.canGoBack.collectAsStateWithLifecycle()

    BackHandler(enabled = canGoBack) {
        appViewModel.back()
    }

    AnimatedContent(
        targetState = screen,
        transitionSpec = {
            fadeIn(animationSpec = tween(durationMillis = 400)) togetherWith
                fadeOut(animationSpec = tween(durationMillis = 300))
        },
        modifier = Modifier.fillMaxSize(),
        label = "screen"
    ) { current ->
        when (current) {
            is Screen.Splash -> SplashRoute(
                onFinished = appViewModel::onSplashFinished
            )

            is Screen.Menu -> MenuRoute(
                onPlay = appViewModel::openGame,
                onFreePlay = appViewModel::openFreePlay,
                onWardrobe = appViewModel::openWardrobe,
                onNoteColors = appViewModel::openNoteColors,
                onGrownUps = appViewModel::openGrownUps
            )

            is Screen.Game -> GameRoute(
                songIndex = current.songIndex,
                session = current.session,
                onFinished = appViewModel::openGameOver,
                onMenu = appViewModel::backToMenu
            )

            is Screen.GameOver -> GameOverRoute(
                songIndex = current.songIndex,
                hatUnlocked = current.hatUnlocked,
                session = current.session,
                onPlayAgain = appViewModel::playNextSong,
                onMenu = appViewModel::backToMenu
            )

            is Screen.FreePlay -> FreePlayRoute(
                onMenu = appViewModel::backToMenu
            )

            is Screen.Wardrobe -> WardrobeRoute(
                onMenu = appViewModel::backToMenu
            )

            is Screen.NoteColors -> NoteColorsRoute(
                onMenu = appViewModel::backToMenu
            )

            is Screen.GrownUps -> GrownUpsRoute(
                onMenu = appViewModel::backToMenu
            )
        }
    }
}
