package org.aalbertini.ham

import org.aalbertini.ham.features.settings.data.SettingsRepository
import org.aalbertini.ham.features.settings.domain.model.AppSettings
import org.aalbertini.ham.features.settings.domain.model.SeedPalette
import org.aalbertini.ham.features.settings.domain.model.ThemeMode
import kotlin.test.Test
import kotlin.test.assertEquals

class SettingsRepositoryTest {

    @Test
    fun v1DarkModeFlag_isMigrated() {
        val dark = SettingsRepository(InMemoryStore(mapOf("app_settings_is_dark_mode" to "true")))
        assertEquals(ThemeMode.DARK, dark.settings.value.themeMode)
        val light = SettingsRepository(InMemoryStore(mapOf("app_settings_is_dark_mode" to "false")))
        assertEquals(ThemeMode.LIGHT, light.settings.value.themeMode)
    }

    @Test
    fun noSettings_usesDefaults() {
        assertEquals(AppSettings(), SettingsRepository(InMemoryStore()).settings.value)
    }

    @Test
    fun update_persistsAcrossInstances() {
        val store = InMemoryStore()
        SettingsRepository(store).update { it.copy(seed = SeedPalette.TECHNICOLOR, themeMode = ThemeMode.DARK) }
        val reloaded = SettingsRepository(store).settings.value
        assertEquals(SeedPalette.TECHNICOLOR, reloaded.seed)
        assertEquals(ThemeMode.DARK, reloaded.themeMode)
    }

    @Test
    fun unreadableV2Settings_fallBackToTheV1Flag() {
        val store = InMemoryStore(mapOf("app_settings_v2" to "{broken", "app_settings_is_dark_mode" to "true"))
        assertEquals(ThemeMode.DARK, SettingsRepository(store).settings.value.themeMode)
    }
}
