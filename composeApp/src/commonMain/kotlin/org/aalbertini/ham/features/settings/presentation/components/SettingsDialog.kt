package org.aalbertini.ham.features.settings.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.window.DialogProperties
import org.aalbertini.ham.features.settings.domain.model.ThemePreset
import org.aalbertini.ham.core.ui.resources.Dimensions
import org.aalbertini.ham.core.ui.resources.UiConstants
import org.aalbertini.ham.features.settings.presentation.platform.getPlatformSettings
import org.aalbertini.ham.features.settings.presentation.platform.getSettingsDialogPadding

/**
 * Settings dialog for app configuration.
 *
 * Features:
 * - Theme preset selection
 * - Light/Dark mode toggle
 * - Platform-specific settings (automatically shown based on platform)
 * - Responsive layout: Dialog on desktop, fullscreen on mobile
 *
 * Behavior:
 * - Desktop (large window): Dialog with rounded corners and padding (68dp), dismissible by clicking outside
 *   - Width: Half of window width, with minimum of 600dp (ensures readability)
 * - Desktop (small window < 600dp width or < 800dp height): Switches to fullscreen mode
 *   - No padding, rounded corners, optimized for limited space
 * - Mobile: Fullscreen view (0dp padding, no rounded corners), not dismissible by clicking outside
 * - Both: Dismissible with back button/escape key, close button in top-right
 *
 * @param currentThemePreset Currently selected theme preset
 * @param isDarkMode Current dark mode state
 * @param onThemePresetChange Callback when theme preset is changed
 * @param onDarkModeChange Callback when dark mode is toggled
 * @param onResetWindowSize Callback for platform-specific actions (e.g., window size reset on desktop)
 * @param onDismiss Callback when dialog is dismissed
 */
@Composable
fun SettingsDialog(
    currentThemePreset: ThemePreset,
    isDarkMode: Boolean,
    onThemePresetChange: (ThemePreset) -> Unit,
    onDarkModeChange: (Boolean) -> Unit,
    onResetWindowSize: (() -> Unit)? = null,
    onDismiss: () -> Unit
) {
    var selectedPreset by remember(currentThemePreset) {
        mutableStateOf(currentThemePreset)
    }
    var darkModeEnabled by remember(isDarkMode) { mutableStateOf(isDarkMode) }
    val scrollState = rememberScrollState()
    val platformDialogPadding = getSettingsDialogPadding()
    val isMobile = platformDialogPadding == 0.dp

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = !isMobile,  // Mobile: can't dismiss by clicking outside (fullscreen)
            usePlatformDefaultWidth = false  // Custom width control
        )
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            // Check if window is smaller than default dimensions (desktop only)
            val isWindowTooSmall by remember(
                maxWidth,
                maxHeight,
                isMobile
            ) {
                derivedStateOf {
                    !isMobile && (maxWidth < UiConstants.Dialog.defaultDialogWidth
                            || maxHeight < UiConstants.Dialog.defaultDialogHeight)
                }
            }

            // Calculate actual padding: remove padding if window is too small on desktop
            val dialogPadding by remember(
                isWindowTooSmall,
                platformDialogPadding,
                isMobile
            ) {
                derivedStateOf {
                    if (isMobile || isWindowTooSmall) 0.dp else platformDialogPadding
                }
            }

            // Calculate dialog width
            val dialogWidth by remember(
                maxWidth,
                isMobile,
                isWindowTooSmall
            ) {
                derivedStateOf {
                    if (isMobile || isWindowTooSmall) {
                        maxWidth  // Mobile or small window: fullscreen width
                    } else {
                        // Desktop: half of window width, but at least the default dialog width
                        maxOf(UiConstants.Dialog.defaultDialogWidth, maxWidth / 2)
                    }
                }
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .then(
                            if (!isMobile && !isWindowTooSmall) Modifier.width(dialogWidth)
                            else Modifier
                        )
                        .padding(dialogPadding),
                    shape = RoundedCornerShape(
                        if (dialogPadding > 0.dp) Dimensions.cornerRadiusXLarge else Dimensions.cornerRadiusSmall
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        // Header (fixed)
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.surface,
                            shadowElevation = 2.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(Dimensions.Padding.extraLarge),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Palette,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(end = Dimensions.Spacing.medium)
                                    )
                                    Text(
                                        text = "Settings",
                                        style = MaterialTheme.typography.headlineMedium,
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
                        }

                        // Scrollable content
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .verticalScroll(scrollState)
                                .padding(Dimensions.Padding.extraLarge)
                        ) {
                            // Dark Mode Toggle Section
                            Text(
                                text = "Appearance",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
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
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(Dimensions.Spacing.medium))

                            Text(
                                text = "Choose a color theme for your app",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(Dimensions.Spacing.medium))

                            // Theme List (using Column instead of LazyColumn for scrollable parent)
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(Dimensions.Spacing.small)
                            ) {
                                ThemePreset.entries.toList().forEach { preset ->
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

                            Spacer(modifier = Modifier.height(Dimensions.Spacing.extraLarge))

                            // Platform-specific settings
                            val platformSettings = remember(onResetWindowSize) {
                                getPlatformSettings(onResetWindowSize)
                            }

                            if (platformSettings.isNotEmpty()) {
                                HorizontalDivider()
                                Spacer(modifier = Modifier.height(Dimensions.Spacing.extraLarge))

                                Text(
                                    text = "Platform Settings",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                Spacer(modifier = Modifier.height(Dimensions.Spacing.medium))

                                Text(
                                    text = "Settings specific to your current platform",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(Dimensions.Spacing.medium))

                                // Display each platform-specific setting
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(Dimensions.Spacing.medium)
                                ) {
                                    platformSettings.forEach { setting ->
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable(onClick = setting.action),
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.primaryContainer
                                            ),
                                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                            shape = RoundedCornerShape(Dimensions.cornerRadiusMedium)
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(Dimensions.Padding.large),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.weight(1f)
                                                ) {
                                                    Icon(
                                                        imageVector = setting.icon,
                                                        contentDescription = null,
                                                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                                        modifier = Modifier.padding(end = Dimensions.Spacing.medium)
                                                    )
                                                    Column {
                                                        Text(
                                                            text = setting.title,
                                                            style = MaterialTheme.typography.titleMedium,
                                                            fontWeight = FontWeight.Bold,
                                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                                        )
                                                        Spacer(modifier = Modifier.height(4.dp))
                                                        Text(
                                                            text = setting.description,
                                                            style = MaterialTheme.typography.bodyMedium,
                                                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                                                                alpha = 0.8f
                                                            )
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(Dimensions.Spacing.large))
                            }
                        }

//                // Footer (fixed)
//                HorizontalDivider()
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(Dimensions.Padding.large),
//                    horizontalArrangement = Arrangement.End
//                ) {
//                    TextButton(onClick = onDismiss) {
//                        Text(
//                            text = "Done",
//                            style = MaterialTheme.typography.labelLarge
//                        )
//                    }
//                }
                    }  // Column
                }  // Card
            }  // Box
        }  // BoxWithConstraints
    }  // Dialog
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
