package org.aalbertini.ham

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import org.aalbertini.ham.features.settings.data.preferences.SettingsPreferences
import org.aalbertini.ham.features.movie_distribution.presentation.screen.MovieWeeklyDistributionCalculatorScreen
import org.aalbertini.ham.core.ui.theme.HAMTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
    alwaysOnTop: Boolean = false,
    onAlwaysOnTopChange: ((Boolean) -> Unit)? = null,
    onResetWindowSize: (() -> Unit)? = null
) {
    val settingsPreferences = remember { SettingsPreferences() }
    val themePreset by settingsPreferences.themePreset.collectAsState()
    
    // Use saved dark mode preference, or fall back to system default
    val systemDarkMode = isSystemInDarkTheme()
    val savedDarkMode = remember { settingsPreferences.loadDarkModeOrNull() }
    var isDarkMode by remember { mutableStateOf(savedDarkMode ?: systemDarkMode) }
    var themeToggleOffset by remember { mutableStateOf<Offset?>(null) }
    
    HAMTheme(
        darkTheme = isDarkMode,
        themePreset = themePreset
    ) {
        MovieWeeklyDistributionCalculatorScreen(
            isDarkMode = isDarkMode,
            themeToggleOffset = themeToggleOffset,
            currentThemePreset = themePreset,
            onThemeToggle = { offset ->
                themeToggleOffset = offset
                isDarkMode = !isDarkMode
                settingsPreferences.saveDarkMode(isDarkMode)
            },
            onThemePresetChange = { preset ->
                settingsPreferences.saveThemePreset(preset)
            },
            alwaysOnTop = alwaysOnTop,
            onAlwaysOnTopChange = onAlwaysOnTopChange,
            onResetWindowSize = onResetWindowSize
        )
    }
}
