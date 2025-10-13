package org.aalbertini.ham.core.ui.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun rememberWindowSizeClass(): WindowSizeClass {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    
    return remember(windowInfo.containerSize) {
        val widthDp = with(density) { windowInfo.containerSize.width.toDp() }
        
        // Determine based on width (iPhone vs iPad)
        when {
            widthDp < 600.dp -> WindowSizeClass.COMPACT
            widthDp < 900.dp -> WindowSizeClass.MEDIUM
            else -> WindowSizeClass.EXPANDED
        }
    }
}
