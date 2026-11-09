package com.mallet.echotiles.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

const val PRESET_NAME = "CANDY_SWEET"

private val EchoColorScheme = lightColorScheme(
    primary = Tomato,
    onPrimary = TextOnAccent,
    primaryContainer = Honey,
    onPrimaryContainer = TextPrimary,
    secondary = Honey,
    onSecondary = TextPrimary,
    secondaryContainer = CreamAlt,
    onSecondaryContainer = TextPrimary,
    tertiary = Sky,
    onTertiary = TextOnAccent,
    tertiaryContainer = Leaf,
    onTertiaryContainer = TextOnAccent,
    background = CreamBackground,
    onBackground = TextPrimary,
    surface = SurfaceWhite,
    onSurface = TextPrimary,
    surfaceVariant = CreamSunken,
    onSurfaceVariant = TextSecondary,
    outline = LockedTile,
    outlineVariant = CreamSunken,
    error = Tomato,
    onError = TextOnAccent
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EchoColorScheme,
        typography = AppTypography,
        content = content
    )
}
