package com.example.flexipay.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Updated Dark Color Scheme using new palette
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlueLight, // Lighter blue for dark theme primary
    secondary = SecondaryTealLight, // Lighter teal for dark theme secondary
    tertiary = AccentOrange,
    background = NeutralGrayDark, 
    surface = SurfaceDark, // Use specific dark surface
    surfaceVariant = SurfaceVariantDark, // Use specific dark variant
    outline = OutlineDark, // Add outline color
    outlineVariant = Color(0xFF606060), // Slightly lighter outline variant
    onPrimary = TextPrimaryDark, // Dark text on light primary button
    onSecondary = TextPrimaryDark, // Dark text on light secondary button
    onTertiary = TextPrimaryDark,
    onBackground = TextPrimaryLight, // Light text on dark background
    onSurface = TextPrimaryLight, // Light text on dark surface
    onSurfaceVariant = TextSecondaryLight // Slightly dimmer light text for variants
)

// Updated Light Color Scheme using new palette
private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    secondary = SecondaryTeal,
    tertiary = AccentOrange,
    background = NeutralGray, // Use light gray background
    surface = SurfaceLight, // Use white surface
    surfaceVariant = NeutralGray, // Use light gray variant
    outline = OutlineLight, // Add outline color
    outlineVariant = Color(0xFFC0C0C0), // Slightly darker outline variant
    onPrimary = TextPrimaryLight, // Light text on primary button
    onSecondary = TextPrimaryDark, // Dark text on secondary button
    onTertiary = TextPrimaryDark,
    onBackground = TextPrimaryDark, // Dark text on light background
    onSurface = TextPrimaryDark, // Dark text on light surface
    onSurfaceVariant = TextSecondaryDark // Dimmer dark text for variants
)

@Composable
fun FlexiPayTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme // Use !darkTheme for light status bar icons on light theme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
} 