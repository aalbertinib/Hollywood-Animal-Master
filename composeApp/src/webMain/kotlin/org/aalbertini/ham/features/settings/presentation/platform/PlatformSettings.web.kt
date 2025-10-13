package org.aalbertini.ham.features.settings.presentation.platform

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Web platform settings implementation.
 * Currently no platform-specific settings for Web.
 */
actual fun getPlatformSettings(
    onResetWindowSize: (() -> Unit)?
): List<PlatformSettingItem> {
    // Web doesn't have platform-specific settings
    return emptyList()
}

/**
 * Web dialog padding: 16.dp
 */
actual fun getSettingsDialogPadding(): Dp = 16.dp
