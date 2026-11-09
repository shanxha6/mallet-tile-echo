package com.mallet.echotiles.core.navigation

sealed interface Screen {
    data object Splash : Screen
    data object Menu : Screen
    data class Game(val songIndex: Int, val session: Int) : Screen
    data class GameOver(val songIndex: Int, val hatUnlocked: Boolean, val session: Int) : Screen
    data object FreePlay : Screen
    data object Wardrobe : Screen
    data object NoteColors : Screen
    data object GrownUps : Screen
}
