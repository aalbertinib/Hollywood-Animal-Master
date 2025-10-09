package org.aalbertini.ham.preferences

import org.aalbertini.ham.storage.StorageProvider

/**
 * Manages theme preferences
 */
class ThemePreferences(
    private val storage: StorageProvider = StorageProvider()
) {
    private val themeKey = "app_theme"
    
    /**
     * Saves the theme preference
     * @param isDarkMode true for dark mode, false for light mode
     */
    fun saveTheme(isDarkMode: Boolean) {
        storage.saveData(themeKey, if (isDarkMode) "dark" else "light")
    }
    
    /**
     * Loads the theme preference
     * @return true for dark mode, false for light mode, defaults to dark
     */
    fun loadTheme(): Boolean {
        val theme = storage.loadData(themeKey)
        return theme != "light" // Default to dark mode
    }
}
