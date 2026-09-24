package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = RoyalMaroon,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFBE8EC),
    onPrimaryContainer = DarkMaroon,
    secondary = DeepGold,
    onSecondary = Color.White,
    secondaryContainer = LightGold,
    onSecondaryContainer = Color(0xFF423100),
    tertiary = RoyalGold,
    onTertiary = Charcoal,
    tertiaryContainer = Color(0xFFFFF6D8),
    onTertiaryContainer = Color(0xFF3B2B00),
    background = SandCream,
    onBackground = Charcoal,
    surface = Color.White,
    onSurface = Charcoal,
    surfaceVariant = SandSurface,
    onSurfaceVariant = SoftCharcoal,
    outline = BorderGold,
    outlineVariant = LightGray
)

private val DarkColorScheme = darkColorScheme(
    primary = RoyalGold,
    onPrimary = Charcoal,
    primaryContainer = DarkMaroon,
    onPrimaryContainer = LightGold,
    secondary = LightGold,
    onSecondary = Charcoal,
    secondaryContainer = Color(0xFF423100),
    onSecondaryContainer = LightGold,
    tertiary = LightMaroon,
    onTertiary = Color.White,
    background = Color(0xFF191314),
    onBackground = SandCream,
    surface = Color(0xFF231B1C),
    onSurface = SandCream,
    surfaceVariant = Color(0xFF332729),
    onSurfaceVariant = Color(0xFFD7CCC8),
    outline = DeepGold,
    outlineVariant = Color(0xFF4E3D40)
)

@Composable
fun DhartiRajasthanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
