package org.aalbertini.ham.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.aalbertini.ham.model.ThemePreset
import org.aalbertini.ham.resources.Dimensions

/**
 * Settings dialog for app configuration.
 * 
 * Features:
 * - Theme preset selection
 * - Light/Dark mode toggle
 * - Preview of theme colors
 * 
 * @param currentThemePreset Currently selected theme preset
 * @param isDarkMode Current dark mode state
 * @param onThemePresetChange Callback when theme preset is changed
 * @param onDarkModeChange Callback when dark mode is toggled
 * @param onDismiss Callback when dialog is dismissed
 */
@Composable
fun SettingsDialog(
    currentThemePreset: ThemePreset,
    isDarkMode: Boolean,
    onThemePresetChange: (ThemePreset) -> Unit,
    onDarkModeChange: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedPreset by remember(currentThemePreset) { mutableStateOf(currentThemePreset) }
    var darkModeEnabled by remember(isDarkMode) { mutableStateOf(isDarkMode) }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.Padding.large),
            shape = RoundedCornerShape(Dimensions.cornerRadiusXLarge),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimensions.Padding.extraLarge)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(Dimensions.Spacing.medium))
                        Text(
                            text = "Settings",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close"
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(Dimensions.Spacing.large))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(Dimensions.Spacing.large))
                
                // Dark Mode Toggle Section
                Text(
                    text = "Appearance",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(Dimensions.Spacing.medium))
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(Dimensions.cornerRadiusMedium))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable {
                            darkModeEnabled = !darkModeEnabled
                            onDarkModeChange(darkModeEnabled)
                        }
                        .padding(Dimensions.Padding.large),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (darkModeEnabled) Icons.Default.DarkMode else Icons.Default.LightMode,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(Dimensions.Spacing.medium))
                        Column {
                            Text(
                                text = if (darkModeEnabled) "Dark Mode" else "Light Mode",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = if (darkModeEnabled) "Switch to light theme" else "Switch to dark theme",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    if (darkModeEnabled) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Enabled",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(Dimensions.Spacing.extraLarge))
                
                // Theme Selection Section
                Text(
                    text = "Theme Presets",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(Dimensions.Spacing.medium))
                
                Text(
                    text = "Choose a color theme for your app",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(Dimensions.Spacing.medium))
                
                // Theme List
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(Dimensions.Spacing.small)
                ) {
                    items(ThemePreset.entries.toList()) { preset ->
                        ThemePresetItem(
                            preset = preset,
                            isSelected = selectedPreset == preset,
                            onClick = {
                                selectedPreset = preset
                                onThemePresetChange(preset)
                            }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(Dimensions.Spacing.large))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(Dimensions.Spacing.medium))
                
                // Footer with action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Done")
                    }
                }
            }
        }
    }
}

/**
 * Individual theme preset item in the list
 */
@Composable
private fun ThemePresetItem(
    preset: ThemePreset,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Dimensions.cornerRadiusMedium))
            .clickable(onClick = onClick),
        color = if (isSelected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        },
        tonalElevation = if (isSelected) 2.dp else 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.Padding.large),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = preset.displayName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = preset.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    }
                )
            }
            
            RadioButton(
                selected = isSelected,
                onClick = onClick
            )
        }
    }
}
