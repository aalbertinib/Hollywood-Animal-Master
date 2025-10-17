package org.aalbertini.ham.features.settings.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.aalbertini.ham.core.ui.resources.Dimensions
import org.aalbertini.ham.core.ui.resources.Strings
import org.aalbertini.ham.features.settings.domain.model.ThemePreset
import org.jetbrains.compose.resources.stringResource

/**
 * Reusable theme preset selection section.
 * Displays a collapsible list of available theme presets.
 *
 * @param selectedPreset Currently selected theme preset
 * @param isExpanded Whether the section is expanded
 * @param onToggleExpanded Callback to toggle expansion state
 * @param onPresetSelected Callback when a preset is selected
 */
@Composable
fun ThemePresetSection(
    selectedPreset: ThemePreset,
    isExpanded: Boolean,
    onToggleExpanded: () -> Unit,
    onPresetSelected: (ThemePreset) -> Unit
) {
    CollapsibleSection(
        title = stringResource(Strings.themePresetsTitle),
        subtitle = selectedPreset.displayName,
        icon = Icons.Default.Palette,
        isExpanded = isExpanded,
        onToggle = onToggleExpanded
    ) {
        Column {
            Spacer(modifier = Modifier.height(Dimensions.Spacing.medium))

            Text(
                text = stringResource(Strings.chooseColorTheme),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = Dimensions.Padding.medium)
            )

            Spacer(modifier = Modifier.height(Dimensions.Spacing.medium))

            // Theme List
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(Dimensions.Spacing.small)
            ) {
                ThemePreset.entries.toList().forEach { preset ->
                    ThemePresetItem(
                        preset = preset,
                        isSelected = selectedPreset == preset,
                        onClick = { onPresetSelected(preset) }
                    )
                }
            }
        }
    }
}
