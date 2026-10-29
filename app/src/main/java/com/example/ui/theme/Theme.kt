package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
    darkColorScheme(
        primary = EmeraldLight,
        onPrimary = CharcoalDark,
        primaryContainer = EmeraldDark,
        onPrimaryContainer = PureWhite,
        secondary = GoldAccent,
        onSecondary = CharcoalDark,
        background = CharcoalDark,
        onBackground = PureWhite,
        surface = SurfaceDark,
        onSurface = PureWhite
    )

private val LightColorScheme =
    lightColorScheme(
        primary = EmeraldGreen,
        onPrimary = PureWhite,
        primaryContainer = Color(0xFFE6F4EA), // Soft Islamic Green Tint
        onPrimaryContainer = EmeraldDark,
        secondary = GoldAccent,
        onSecondary = PureWhite,
        background = PureWhite,
        onBackground = TextDark,
        surface = PureWhite,
        onSurface = TextDark
    )

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
