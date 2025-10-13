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
        val heightDp = with(density) { windowInfo.containerSize.height.toDp() }
        
        // Determine based on width and aspect ratio
        when {
            widthDp < 600.dp -> WindowSizeClass.COMPACT
            widthDp < 840.dp -> WindowSizeClass.MEDIUM
            widthDp < 1000.dp && heightDp > widthDp -> WindowSizeClass.MEDIUM // Portrait
            else -> WindowSizeClass.EXPANDED
        }
    }
}
