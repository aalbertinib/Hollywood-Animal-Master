package org.aalbertini.ham.ui.settings

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * iOS platform settings implementation.
 * Currently no platform-specific settings for iOS.
 */
actual fun getPlatformSettings(
    onResetWindowSize: (() -> Unit)?
): List<PlatformSettingItem> {
    // iOS doesn't have platform-specific settings
    return emptyList()
}

/**
 * Mobile dialog padding: 0.dp (fullscreen)
 */
actual fun getSettingsDialogPadding(): Dp = 0.dp
