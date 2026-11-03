package org.aalbertini.ham.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.materialkolor.DynamicMaterialTheme
import com.materialkolor.PaletteStyle
import org.aalbertini.ham.features.settings.domain.model.AppSettings
import org.aalbertini.ham.features.settings.domain.model.PaletteVariant
import org.aalbertini.ham.features.settings.domain.model.ThemeMode

/**
 * Wallpaper-based scheme (Android 12+). `null` where the platform has none.
 */
@Composable
expect fun platformDynamicColorScheme(isDark: Boolean): ColorScheme?

/** Whether [platformDynamicColorScheme] can return a scheme here (hides the setting otherwise). */
expect val supportsDynamicColor: Boolean

@Composable
fun AppSettings.isDarkTheme(): Boolean = when (themeMode) {
    ThemeMode.SYSTEM -> isSystemInDarkTheme()
    ThemeMode.LIGHT -> false
    ThemeMode.DARK -> true
}

/**
 * App theme: a full Material 3 scheme (every surface-container role included) generated
 * from the seed color with Material Color Utilities, animated when the seed or mode changes.
 */
@Composable
fun HamTheme(
    settings: AppSettings,
    content: @Composable () -> Unit,
) {
    val isDark = settings.isDarkTheme()
    val wallpaperScheme = if (settings.useDynamicColor) platformDynamicColorScheme(isDark) else null
    if (wallpaperScheme != null) {
        MaterialTheme(
            colorScheme = wallpaperScheme,
            typography = HamTypography,
            shapes = HamShapes,
            content = content,
        )
    } else {
        DynamicMaterialTheme(
            seedColor = settings.seed.color,
            isDark = isDark,
            style = settings.variant.toPaletteStyle(),
            contrastLevel = settings.contrast.value,
            typography = HamTypography,
            shapes = HamShapes,
            animate = true,
            content = content,
        )
    }
}

private fun PaletteVariant.toPaletteStyle(): PaletteStyle = when (this) {
    PaletteVariant.TONAL_SPOT -> PaletteStyle.TonalSpot
    PaletteVariant.VIBRANT -> PaletteStyle.Vibrant
    PaletteVariant.EXPRESSIVE -> PaletteStyle.Expressive
    PaletteVariant.NEUTRAL -> PaletteStyle.Neutral
    PaletteVariant.FIDELITY -> PaletteStyle.Fidelity
}
