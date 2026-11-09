package com.mallet.echotiles.presentation.app

import androidx.lifecycle.ViewModel
import com.mallet.echotiles.core.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val backStack = mutableListOf<Screen>()
    private var session = 0

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val _canGoBack = MutableStateFlow(false)
    val canGoBack: StateFlow<Boolean> = _canGoBack.asStateFlow()

    fun onSplashFinished() {
        backStack.clear()
        _screen.value = Screen.Menu
        syncBack()
    }

    fun openGame(songIndex: Int) {
        session += 1
        push(Screen.Game(songIndex = songIndex, session = session))
    }

    fun openFreePlay() = push(Screen.FreePlay)

    fun openWardrobe() = push(Screen.Wardrobe)

    fun openNoteColors() = push(Screen.NoteColors)

    fun openGrownUps() = push(Screen.GrownUps)

    fun openGameOver(songIndex: Int, hatUnlocked: Boolean) {
        session += 1
        replace(
            Screen.GameOver(
                songIndex = songIndex,
                hatUnlocked = hatUnlocked,
                session = session
            )
        )
    }

    fun playNextSong(songIndex: Int) {
        session += 1
        replace(Screen.Game(songIndex = songIndex, session = session))
    }

    fun backToMenu() {
        backStack.clear()
        _screen.value = Screen.Menu
        syncBack()
    }

    fun back() {
        val previous = backStack.removeLastOrNull() ?: return
        _screen.value = previous
        syncBack()
    }

    private fun push(target: Screen) {
        backStack.add(_screen.value)
        _screen.value = target
        syncBack()
    }

    private fun replace(target: Screen) {
        _screen.value = target
        syncBack()
    }

    private fun syncBack() {
        _canGoBack.value = backStack.isNotEmpty()
    }
}
