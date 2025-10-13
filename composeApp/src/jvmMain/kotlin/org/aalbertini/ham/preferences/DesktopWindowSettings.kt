package org.aalbertini.ham.preferences

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.aalbertini.ham.core.data.storage.StorageProvider
import java.util.concurrent.atomic.AtomicReference

/**
 * Data class representing desktop settings stored in JSON format
 */
@Serializable
data class DesktopSettingsData(
    val windowWidth: Float,
    val windowHeight: Float
)

/**
 * Manages desktop window settings for JVM platform.
 * Handles window size persistence across app sessions.
 * 
 * Thread-safe implementation using atomic operations.
 * Settings are saved to a single JSON file: ham_desktop_settings.json
 */
class DesktopWindowSettings(
    private val storage: StorageProvider = StorageProvider()
) {
    private companion object {
        const val SETTINGS_KEY = "ham_desktop_settings"
    }
    
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }
    
    // Store the last saved size to avoid unnecessary writes
    private val lastSavedSize = AtomicReference<DpSize?>(null)
    
    /**
     * Saves the window size if it differs from the last saved size
     * @param size The window size to save
     */
    fun saveWindowSize(size: DpSize) {
        val current = lastSavedSize.get()
        
        // Only save if size changed from last saved state
        if (current == null || current.width != size.width || current.height != size.height) {
            try {
                val settingsData = DesktopSettingsData(
                    windowWidth = size.width.value,
                    windowHeight = size.height.value
                )
                val jsonString = json.encodeToString(settingsData)
                storage.saveData(SETTINGS_KEY, jsonString)
                lastSavedSize.set(size)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    /**
     * Loads the saved window size
     * @param defaultSize The default size to use if no saved size exists
     * @return The saved window size, or default if not found
     */
    fun loadWindowSize(defaultSize: DpSize): DpSize {
        return try {
            val jsonString = storage.loadData(SETTINGS_KEY)
            if (jsonString != null) {
                val settingsData = json.decodeFromString<DesktopSettingsData>(jsonString)
                val size = DpSize(settingsData.windowWidth.dp, settingsData.windowHeight.dp)
                lastSavedSize.set(size)
                size
            } else {
                lastSavedSize.set(defaultSize)
                defaultSize
            }
        } catch (e: Exception) {
            e.printStackTrace()
            lastSavedSize.set(defaultSize)
            defaultSize
        }
    }
    
    /**
     * Clears saved window size settings
     */
    fun clearWindowSettings() {
        storage.saveData(SETTINGS_KEY, "")
        lastSavedSize.set(null)
    }
}
