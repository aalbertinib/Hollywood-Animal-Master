package org.aalbertini.ham.core.ui.theme

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

/**
 * Animated background that transitions between colors using circular reveal animation.
 * 
 * Optimized to prevent unnecessary recompositions:
 * - Uses remember for stable animation spec
 * - Stable content lambda to avoid recomposition
 * - Efficient color selection
 * 
 * @param targetState The target state to determine background color (true = dark, false = light)
 * @param backgroundColor Current background color from theme
 * @param revealFrom Optional offset to start the reveal animation from
 * @param modifier Modifier to apply to the background
 */
@Composable
fun AnimatedBackground(
    targetState: Boolean,
    backgroundColor: Color,
    revealFrom: Offset? = null,
    modifier: Modifier = Modifier
) {
    // Remember animation spec to avoid recreation on recomposition
    val animationSpec = remember { tween<Float>(durationMillis = 700) }
    
    // Remember content lambda to prevent recomposition of CircularReveal
    val content: @Composable (Boolean) -> Unit = remember(backgroundColor) {
        { _ ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
            )
        }
    }
    
    CircularReveal(
        targetState = targetState,
        modifier = modifier.fillMaxSize(),
        animationSpec = animationSpec,
        revealFrom = revealFrom,
        content = content
    )
}

/**
 * Animated background that transitions between colors using circular reveal animation.
 * 
 * @param targetState The target state to determine background color (true = dark, false = light)
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
    // Remember animation spec to avoid recreation on recomposition
    val animationSpec = remember { tween<Float>(durationMillis = 700) }
    
    // Remember content lambda to prevent recomposition of CircularReveal
    val content: @Composable (Boolean) -> Unit = remember(lightColor, darkColor) {
        { isDark ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (isDark) darkColor else lightColor)
            )
        }
    }
    
    CircularReveal(
        targetState = targetState,
        modifier = modifier.fillMaxSize(),
        animationSpec = animationSpec,
        revealFrom = revealFrom,
        content = content
    )
}
