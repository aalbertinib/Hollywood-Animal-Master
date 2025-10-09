package org.aalbertini.ham.ui.theme

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

/**
 * Animated background that transitions between colors using circular reveal animation
 * 
 * @param targetState The target state to determine background color
 * @param lightColor Background color for light theme
 * @param darkColor Background color for dark theme
 * @param revealFrom Optional offset to start the reveal animation from
 * @param modifier Modifier to apply to the background
 */
@Composable
fun AnimatedBackground(
    targetState: Boolean,
    lightColor: Color,
    darkColor: Color,
    revealFrom: Offset? = null,
    modifier: Modifier = Modifier
) {
    CircularReveal(
        targetState = targetState,
        modifier = modifier.fillMaxSize(),
        animationSpec = tween(durationMillis = 700),
        revealFrom = revealFrom
    ) { isDark ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isDark) darkColor else lightColor)
        )
    }
}
