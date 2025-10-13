package org.aalbertini.ham.features.settings.presentation.platform

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp

/**
 * Represents a platform-specific setting item
 * 
 * @param title The display title of the setting
 * @param description A brief description of what the setting does
 * @param icon The icon to display for this setting
 * @param action The action to perform when the setting is clicked
 */
data class PlatformSettingItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val action: () -> Unit
)

/**
 * Returns platform-specific settings to display in the settings dialog.
 * Different platforms can provide their own settings (e.g., window size reset on desktop).
 * 
 * @param onResetWindowSize Callback for resetting window size (desktop only)
 * @return List of platform-specific setting items
 */
expect fun getPlatformSettings(
    onResetWindowSize: (() -> Unit)?
): List<PlatformSettingItem>

/**
 * Returns the padding to use for the settings dialog on the current platform.
 * Desktop: 68.dp padding
 * Mobile: 0.dp (fullscreen)
 */
expect fun getSettingsDialogPadding(): Dp
