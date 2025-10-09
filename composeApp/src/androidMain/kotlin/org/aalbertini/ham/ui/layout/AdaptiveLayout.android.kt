package org.aalbertini.ham.ui.layout

import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
actual fun rememberWindowSizeClass(): WindowSizeClass {
    val activity = LocalContext.current as ComponentActivity
    val windowSizeClass = calculateWindowSizeClass(activity)
    return when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> WindowSizeClass.COMPACT
        WindowWidthSizeClass.Medium -> WindowSizeClass.MEDIUM
        else -> WindowSizeClass.EXPANDED
    }
}
