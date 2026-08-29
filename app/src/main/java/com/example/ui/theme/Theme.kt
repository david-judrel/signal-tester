package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkTechColorScheme =
  darkColorScheme(
    primary = NeonCyan,
    onPrimary = DarkBackgroundDeep,
    primaryContainer = DarkSurfaceElevated,
    onPrimaryContainer = NeonCyan,
    secondary = NeonBlue,
    onSecondary = DarkBackgroundDeep,
    secondaryContainer = DarkSurfaceSubtle,
    onSecondaryContainer = NeonBlue,
    tertiary = NeonAmber,
    onTertiary = DarkBackgroundDeep,
    error = NeonRed,
    onError = TextPrimary,
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = GlassBorder,
    outlineVariant = GlassHighlight,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = DarkTechColorScheme,
    typography = Typography,
    content = content
  )
}

