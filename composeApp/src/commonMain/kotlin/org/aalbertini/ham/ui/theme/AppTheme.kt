package org.aalbertini.ham.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF6750A4),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF625B71),
    onSecondary = Color(0xFFFFFFFF),
    tertiary = Color(0xFF7D5260),
    onTertiary = Color(0xFFFFFFFF),
    background = Color(0xFFFFFBFE),
    onBackground = Color(0xFF1C1B1F),
    surface = Color(0xFFFFFBFE),
    onSurface = Color(0xFF1C1B1F),
    error = Color(0xFFB3261E),
    onError = Color(0xFFFFFFFF),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFD0BCFF),
    onPrimary = Color(0xFF381E72),
    secondary = Color(0xFFCCC2DC),
    onSecondary = Color(0xFF332D41),
    tertiary = Color(0xFFEFB8C8),
    onTertiary = Color(0xFF492532),
    background = Color(0xFF1C1B1F),
    onBackground = Color(0xFFE6E1E5),
    surface = Color(0xFF1C1B1F),
    onSurface = Color(0xFFE6E1E5),
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
)

@Composable
fun AppTheme(useDarkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val targetColors = if (useDarkTheme) DarkColors else LightColors
    
    // Animate all color transitions with a smooth spring animation
    val animatedColorScheme = ColorScheme(
        primary = animateColorAsState(
            targetValue = targetColors.primary,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        onPrimary = animateColorAsState(
            targetValue = targetColors.onPrimary,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        primaryContainer = animateColorAsState(
            targetValue = targetColors.primaryContainer,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        onPrimaryContainer = animateColorAsState(
            targetValue = targetColors.onPrimaryContainer,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        inversePrimary = animateColorAsState(
            targetValue = targetColors.inversePrimary,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        secondary = animateColorAsState(
            targetValue = targetColors.secondary,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        onSecondary = animateColorAsState(
            targetValue = targetColors.onSecondary,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        secondaryContainer = animateColorAsState(
            targetValue = targetColors.secondaryContainer,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        onSecondaryContainer = animateColorAsState(
            targetValue = targetColors.onSecondaryContainer,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        tertiary = animateColorAsState(
            targetValue = targetColors.tertiary,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        onTertiary = animateColorAsState(
            targetValue = targetColors.onTertiary,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        tertiaryContainer = animateColorAsState(
            targetValue = targetColors.tertiaryContainer,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        onTertiaryContainer = animateColorAsState(
            targetValue = targetColors.onTertiaryContainer,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        background = animateColorAsState(
            targetValue = targetColors.background,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        onBackground = animateColorAsState(
            targetValue = targetColors.onBackground,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        surface = animateColorAsState(
            targetValue = targetColors.surface,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        onSurface = animateColorAsState(
            targetValue = targetColors.onSurface,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        surfaceVariant = animateColorAsState(
            targetValue = targetColors.surfaceVariant,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        onSurfaceVariant = animateColorAsState(
            targetValue = targetColors.onSurfaceVariant,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        surfaceTint = animateColorAsState(
            targetValue = targetColors.surfaceTint,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        inverseSurface = animateColorAsState(
            targetValue = targetColors.inverseSurface,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        inverseOnSurface = animateColorAsState(
            targetValue = targetColors.inverseOnSurface,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        error = animateColorAsState(
            targetValue = targetColors.error,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        onError = animateColorAsState(
            targetValue = targetColors.onError,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        errorContainer = animateColorAsState(
            targetValue = targetColors.errorContainer,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        onErrorContainer = animateColorAsState(
            targetValue = targetColors.onErrorContainer,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        outline = animateColorAsState(
            targetValue = targetColors.outline,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        outlineVariant = animateColorAsState(
            targetValue = targetColors.outlineVariant,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        scrim = animateColorAsState(
            targetValue = targetColors.scrim,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        surfaceBright = animateColorAsState(
            targetValue = targetColors.surfaceBright,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        surfaceDim = animateColorAsState(
            targetValue = targetColors.surfaceDim,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        surfaceContainer = animateColorAsState(
            targetValue = targetColors.surfaceContainer,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        surfaceContainerHigh = animateColorAsState(
            targetValue = targetColors.surfaceContainerHigh,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        surfaceContainerHighest = animateColorAsState(
            targetValue = targetColors.surfaceContainerHighest,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        surfaceContainerLow = animateColorAsState(
            targetValue = targetColors.surfaceContainerLow,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
        surfaceContainerLowest = animateColorAsState(
            targetValue = targetColors.surfaceContainerLowest,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        ).value,
    )
    
    MaterialTheme(
        colorScheme = animatedColorScheme,
        typography = Typography(),
        content = content
    )
}
