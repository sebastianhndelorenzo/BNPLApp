package com.example.flexipay.ui.theme

import androidx.compose.ui.graphics.Color

// Primary Color Palette (Example: Professional Blue)
val PrimaryBlue = Color(0xFF0073B1) // A professional blue tone
val PrimaryBlueLight = Color(0xFF5E9FD1)
val PrimaryBlueDark = Color(0xFF004A81)

// Secondary Color Palette (Example: Complementary Teal/Green)
val SecondaryTeal = Color(0xFF4DB6AC)
val SecondaryTealLight = Color(0xFF82E9DE)
val SecondaryTealDark = Color(0xFF00867D)

// Tertiary Color Palette (Optional Accent)
val AccentOrange = Color(0xFFFFA726)

// Neutral Colors
val NeutralGray = Color(0xFFF5F5F5) // Light background
val SurfaceLight = Color(0xFFFFFFFF) // White surface for cards etc. in light mode
val NeutralGrayDark = Color(0xFF212121) // Dark background
val SurfaceDark = Color(0xFF303030) // Slightly lighter dark surface
val SurfaceVariantDark = Color(0xFF424242)
val OutlineLight = Color(0xFFD0D0D0) // Subtle border/outline for light theme
val OutlineDark = Color(0xFF505050) // Subtle border/outline for dark theme

val TextPrimaryDark = Color(0xFF1F1F1F)
val TextPrimaryLight = Color(0xFFE5E5E5)
val TextSecondaryDark = Color(0x99000000)
val TextSecondaryLight = Color(0xB3FFFFFF)

// Status Colors
val StatusGreen = Color(0xFF388E3C) // Slightly darker green
val StatusGreenContainerLight = Color(0xFFC8E6C9)
val StatusGreenContainerDark = Color(0xFF003300) // Dark container for green text

// Redefine App colors based on the new palette
val AppGradientStart = PrimaryBlue
val AppGradientEnd = SecondaryTealDark // Subtle gradient
val AppBlue = PrimaryBlue // Keep for consistency if used elsewhere
val AppGreen = StatusGreen
val AppWhite = Color.White
val AppBlack = Color.Black 