package org.aalbertini.ham.features.settings.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import org.aalbertini.ham.features.settings.data.SettingsRepository
import org.aalbertini.ham.features.settings.domain.model.AppSettings

class SettingsViewModel(
    private val repository: SettingsRepository,
) : ViewModel() {
    val settings: StateFlow<AppSettings> = repository.settings

    fun update(transform: (AppSettings) -> AppSettings) = repository.update(transform)
}
