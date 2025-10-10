package org.aalbertini.ham.ui.settings

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * JavaScript platform settings implementation.
 * Currently no platform-specific settings for JS.
 */
actual fun getPlatformSettings(
    onResetWindowSize: (() -> Unit)?
): List<PlatformSettingItem> {
    // JS doesn't have platform-specific settings
    return emptyList()
}

/**
 * Web dialog padding: 16.dp
 */
actual fun getSettingsDialogPadding(): Dp = 16.dp
