package org.aalbertini.ham.features.settings.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.serialization.json.Json
import org.aalbertini.ham.core.data.storage.KeyValueStore
import org.aalbertini.ham.core.data.storage.StorageProvider
import org.aalbertini.ham.features.settings.domain.model.AppSettings
import org.aalbertini.ham.features.settings.domain.model.ThemeMode

/**
 * Persists [AppSettings] as one JSON document, separate from movie saves.
 * The V1 dark-mode flag is read once so existing users keep their light/dark choice.
 */
class SettingsRepository(
    private val storage: KeyValueStore = StorageProvider()
) {
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    private val _settings = MutableStateFlow(load())
    val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    fun update(transform: (AppSettings) -> AppSettings) {
        val updated = _settings.updateAndGet(transform)
        storage.saveData(KEY_SETTINGS, json.encodeToString(updated))
    }

    private fun load(): AppSettings {
        storage.loadData(KEY_SETTINGS)
            ?.takeIf { it.isNotBlank() }
            ?.let { saved -> runCatching { json.decodeFromString<AppSettings>(saved) }.getOrNull() }
            ?.let { return it }
        val legacyDark = storage.loadData(KEY_LEGACY_DARK_MODE)?.toBooleanStrictOrNull()
        return when (legacyDark) {
            true -> AppSettings(themeMode = ThemeMode.DARK)
            false -> AppSettings(themeMode = ThemeMode.LIGHT)
            null -> AppSettings()
        }
    }

    private companion object {
        const val KEY_SETTINGS = "app_settings_v2"
        const val KEY_LEGACY_DARK_MODE = "app_settings_is_dark_mode"
    }
}
