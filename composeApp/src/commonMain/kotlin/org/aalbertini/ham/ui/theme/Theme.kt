package org.aalbertini.ham.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.aalbertini.ham.model.ThemeColorSchemes
import org.aalbertini.ham.model.ThemePreset

/**
 * Hollywood Animals Master theme configuration.
 * 
 * Implements Material Design 3 color scheme with multiple theme presets.
 * Supports both light and dark themes following platform conventions.
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
 * HAMTheme(themePreset = ThemePreset.OCEAN_BREEZE) {
 *     // Your app content
 * }
 * ```
 */

/**
 * Main theme composable for Hollywood Animals Master.
 * 
 * @param darkTheme Whether to use dark theme. Defaults to system preference.
 * @param themePreset The theme preset to use. Defaults to HOLLYWOOD_CLASSIC.
 * @param content The content to display within the theme.
 */
@Composable
fun HAMTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themePreset: ThemePreset = ThemePreset.MIDNIGHT_PURPLE,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        ThemeColorSchemes.getDarkColorScheme(themePreset)
    } else {
        ThemeColorSchemes.getLightColorScheme(themePreset)
    }
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
