package com.hdulam.walkingwithghosts.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

// The game always uses its own dark paranormal palette, so dynamic color and light mode are not used.
private val GhostColorScheme = darkColorScheme(
    primary = NeonGreen,
    onPrimary = VoidBlack,
    primaryContainer = NeonGreenDim,
    onPrimaryContainer = MistWhite,
    secondary = SpectralPurple,
    onSecondary = VoidBlack,
    secondaryContainer = SpectralPurpleDim,
    onSecondaryContainer = MistWhite,
    tertiary = EctoplasmCyan,
    onTertiary = VoidBlack,
    background = VoidBlack,
    onBackground = MistWhite,
    surface = NightBlue,
    onSurface = MistWhite,
    surfaceVariant = NightBlueElevated,
    onSurfaceVariant = MistGrey,
    error = BloodRed,
    onError = VoidBlack,
    outline = SpectralPurpleDim
)

@Composable
fun WalkingWithGhostsTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = GhostColorScheme,
        typography = Typography,
        content = content
    )
}
