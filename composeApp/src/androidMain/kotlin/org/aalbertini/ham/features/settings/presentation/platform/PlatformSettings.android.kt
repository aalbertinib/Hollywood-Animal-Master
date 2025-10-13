package org.aalbertini.ham.features.settings.presentation.platform

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Android platform settings implementation.
 * Currently no platform-specific settings for Android.
 */
actual fun getPlatformSettings(
    onResetWindowSize: (() -> Unit)?
): List<PlatformSettingItem> {
    // Android doesn't have platform-specific settings
    return emptyList()
}

/**
 * Mobile dialog padding: 0.dp (fullscreen)
 */
actual fun getSettingsDialogPadding(): Dp = 0.dp
