package org.aalbertini.ham.ui.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * JVM (Desktop) platform settings implementation.
 * Provides desktop-specific settings like window size reset.
 */
actual fun getPlatformSettings(
    onResetWindowSize: (() -> Unit)?
): List<PlatformSettingItem> {
    val settings = mutableListOf<PlatformSettingItem>()
    
    // Add window size reset option if callback is provided
    onResetWindowSize?.let { resetAction ->
        settings.add(
            PlatformSettingItem(
                title = "Reset Window Size",
                description = "Restore default window dimensions",
                icon = Icons.Default.Refresh,
                action = resetAction
            )
        )
    }
    
    return settings
}

/**
 * Desktop dialog padding: 68.dp on all sides
 */
actual fun getSettingsDialogPadding(): Dp = 68.dp
