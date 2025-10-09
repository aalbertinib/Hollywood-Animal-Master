package org.aalbertini.ham

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.aalbertini.ham.preferences.ThemePreferences
import org.aalbertini.ham.ui.screen.MovieWeeklyDistributionCalculatorScreen
import org.aalbertini.ham.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
    alwaysOnTop: Boolean = false,
    onAlwaysOnTopChange: ((Boolean) -> Unit)? = null
) {
    val themePreferences = remember { ThemePreferences() }
    var isDarkMode by remember { mutableStateOf(themePreferences.loadTheme()) }
    
    AppTheme(useDarkTheme = isDarkMode) {
        MovieWeeklyDistributionCalculatorScreen(
            isDarkMode = isDarkMode,
            onThemeToggle = {
                isDarkMode = !isDarkMode
                themePreferences.saveTheme(isDarkMode)
            },
            alwaysOnTop = alwaysOnTop,
            onAlwaysOnTopChange = onAlwaysOnTopChange
        )
    }
}
