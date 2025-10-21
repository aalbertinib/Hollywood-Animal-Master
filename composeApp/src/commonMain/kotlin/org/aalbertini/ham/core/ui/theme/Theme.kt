package org.aalbertini.ham.core.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.aalbertini.ham.features.settings.domain.model.ThemeColorSchemes
import org.aalbertini.ham.features.settings.domain.model.ThemePreset

/**
 * Hollywood Animal Master modern theme configuration.
 * 
 * Implements Material Design 3 with modern dashboard aesthetics:
 * - Smooth color transitions with spring animations
 * - Enhanced elevation and depth
 * - Modern card-based layouts
 * - Multiple theme presets with dark/light support
 * 
 * Features:
 * - All existing ThemePresets preserved
 * - Animated color scheme transitions
 * - Modern Material3 components
 * - Dashboard-inspired design language
 * 
 * Usage:
 * ```
 * HAMTheme {
 *     // Your app content
 * }
 * 
 * // Force dark theme
 * HAMTheme(darkTheme = true) {
 *     // Your app content
 * }
 * 
 * // Use specific theme preset
 * HAMTheme(themePreset = ThemePreset.D02_CADMIUM_GREEN) {
 *     // Your app content
 * }
 * ```
 */

/**
 * Main theme composable for Hollywood Animal Master with modern styling.
 * 
 * @param darkTheme Whether to use dark theme. Defaults to system preference.
 * @param themePreset The theme preset to use. Defaults to D01_PURPLE.
 * @param content The content to display within the theme.
 */
@Composable
fun HAMTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themePreset: ThemePreset = ThemePreset.D01_PURPLE,
    content: @Composable () -> Unit
) {
    val targetColorScheme = if (darkTheme) {
        ThemeColorSchemes.getDarkColorScheme(themePreset)
    } else {
        ThemeColorSchemes.getLightColorScheme(themePreset)
    }
    
    // Animate color scheme transitions for smooth theme changes
    val animatedColorScheme = ColorScheme(
        primary = animateColorAsState(
            targetValue = targetColorScheme.primary,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        onPrimary = animateColorAsState(
            targetValue = targetColorScheme.onPrimary,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        primaryContainer = animateColorAsState(
            targetValue = targetColorScheme.primaryContainer,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        onPrimaryContainer = animateColorAsState(
            targetValue = targetColorScheme.onPrimaryContainer,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        inversePrimary = animateColorAsState(
            targetValue = targetColorScheme.inversePrimary,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        secondary = animateColorAsState(
            targetValue = targetColorScheme.secondary,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        onSecondary = animateColorAsState(
            targetValue = targetColorScheme.onSecondary,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        secondaryContainer = animateColorAsState(
            targetValue = targetColorScheme.secondaryContainer,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        onSecondaryContainer = animateColorAsState(
            targetValue = targetColorScheme.onSecondaryContainer,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        tertiary = animateColorAsState(
            targetValue = targetColorScheme.tertiary,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        onTertiary = animateColorAsState(
            targetValue = targetColorScheme.onTertiary,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        tertiaryContainer = animateColorAsState(
            targetValue = targetColorScheme.tertiaryContainer,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        onTertiaryContainer = animateColorAsState(
            targetValue = targetColorScheme.onTertiaryContainer,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        background = animateColorAsState(
            targetValue = targetColorScheme.background,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        onBackground = animateColorAsState(
            targetValue = targetColorScheme.onBackground,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        surface = animateColorAsState(
            targetValue = targetColorScheme.surface,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        onSurface = animateColorAsState(
            targetValue = targetColorScheme.onSurface,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        surfaceVariant = animateColorAsState(
            targetValue = targetColorScheme.surfaceVariant,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        onSurfaceVariant = animateColorAsState(
            targetValue = targetColorScheme.onSurfaceVariant,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        surfaceTint = animateColorAsState(
            targetValue = targetColorScheme.surfaceTint,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        inverseSurface = animateColorAsState(
            targetValue = targetColorScheme.inverseSurface,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        inverseOnSurface = animateColorAsState(
            targetValue = targetColorScheme.inverseOnSurface,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        error = animateColorAsState(
            targetValue = targetColorScheme.error,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        onError = animateColorAsState(
            targetValue = targetColorScheme.onError,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        errorContainer = animateColorAsState(
            targetValue = targetColorScheme.errorContainer,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        onErrorContainer = animateColorAsState(
            targetValue = targetColorScheme.onErrorContainer,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        outline = animateColorAsState(
            targetValue = targetColorScheme.outline,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        outlineVariant = animateColorAsState(
            targetValue = targetColorScheme.outlineVariant,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        scrim = animateColorAsState(
            targetValue = targetColorScheme.scrim,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        surfaceBright = animateColorAsState(
            targetValue = targetColorScheme.surfaceBright,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        surfaceDim = animateColorAsState(
            targetValue = targetColorScheme.surfaceDim,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        surfaceContainer = animateColorAsState(
            targetValue = targetColorScheme.surfaceContainer,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        surfaceContainerHigh = animateColorAsState(
            targetValue = targetColorScheme.surfaceContainerHigh,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        surfaceContainerHighest = animateColorAsState(
            targetValue = targetColorScheme.surfaceContainerHighest,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        surfaceContainerLow = animateColorAsState(
            targetValue = targetColorScheme.surfaceContainerLow,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        surfaceContainerLowest = animateColorAsState(
            targetValue = targetColorScheme.surfaceContainerLowest,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
    )
    
    MaterialTheme(
        colorScheme = animatedColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
