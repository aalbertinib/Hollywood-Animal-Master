package org.aalbertini.ham.core.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

/** No wallpaper-based color on this platform: the seed palette is always used. */
@Composable
actual fun platformDynamicColorScheme(isDark: Boolean): ColorScheme? = null

actual val supportsDynamicColor: Boolean = false
