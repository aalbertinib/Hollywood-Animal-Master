package org.aalbertini.ham.features.settings.data.preferences

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.aalbertini.ham.core.data.storage.StorageProvider
import org.aalbertini.ham.features.settings.domain.model.ThemePreset

/**
 * Manages app settings preferences (theme, etc.).
 * Separate from movie data saves to avoid conflicts.
 * 
 * Thread-safe implementation using StateFlow with atomic updates.
 */
class SettingsPreferences(
    private val storage: StorageProvider = StorageProvider()
) {
    private companion object {
        const val SETTINGS_PREFIX = "app_settings_"
        const val KEY_THEME_PRESET = "${SETTINGS_PREFIX}theme_preset"
        const val KEY_IS_DARK_MODE = "${SETTINGS_PREFIX}is_dark_mode"
    }
    
    private val _themePreset = MutableStateFlow(loadThemePreset())
    val themePreset: StateFlow<ThemePreset> = _themePreset.asStateFlow()
    
    private val _isDarkMode = MutableStateFlow(loadDarkMode())
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()
    
    /**
     * Saves the theme preset preference
     * @param preset The theme preset to save
     */
    fun saveThemePreset(preset: ThemePreset) {
        storage.saveData(KEY_THEME_PRESET, preset.name)
        _themePreset.update { preset }
    }
    
    /**
     * Loads the saved theme preset
     * @return The saved theme preset, defaults to D01_PURPLE
     */
    private fun loadThemePreset(): ThemePreset {
        val saved = storage.loadData(KEY_THEME_PRESET)
        return ThemePreset.fromString(saved) ?: ThemePreset.D01_PURPLE
    }
    
    /**
     * Saves the dark mode preference
     * @param isDark true for dark mode, false for light mode
     */
    fun saveDarkMode(isDark: Boolean) {
        storage.saveData(KEY_IS_DARK_MODE, isDark.toString())
        _isDarkMode.update { isDark }
    }
    
    /**
     * Loads the saved dark mode preference
     * @return The saved dark mode setting, returns null if not set (use system default)
     */
    fun loadDarkModeOrNull(): Boolean? {
        val saved = storage.loadData(KEY_IS_DARK_MODE)
        return saved?.toBooleanStrictOrNull()
    }
    
    /**
     * Loads the dark mode preference with fallback
     * @return true for dark mode, defaults to true if not set
     */
    private fun loadDarkMode(): Boolean {
        return loadDarkModeOrNull() ?: true
    }
    
    /**
     * Clears all settings (for testing or reset functionality)
     */
    fun clearAllSettings() {
        storage.saveData(KEY_THEME_PRESET, "")
        storage.saveData(KEY_IS_DARK_MODE, "")
        _themePreset.update { ThemePreset.D01_PURPLE }
        _isDarkMode.update { true }
    }
}
