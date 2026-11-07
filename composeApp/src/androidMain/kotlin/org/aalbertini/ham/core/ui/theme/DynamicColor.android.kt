package org.aalbertini.ham.core.ui.theme

import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun platformDynamicColorScheme(isDark: Boolean): ColorScheme? {
    if (!supportsDynamicColor) return null
    val context = LocalContext.current
    return if (isDark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
}

actual val supportsDynamicColor: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
