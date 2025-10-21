package org.aalbertini.ham.core.ui.theme

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.hypot

/**
 * Data class to hold animation item state
 */
private data class CircularRevealAnimationItem<T>(
    val key: T,
    val content: @Composable () -> Unit
)

/**
 * Custom shape that creates a circular clip with an animated progress.
 * 
 * Thread-safe and immutable - all properties are val and passed via constructor.
 * The shape is recreated on each animation frame with new progress value.
 */
private class CircularRevealShape(
    private val progress: Float,
    private val offset: Offset? = null
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val centerX = offset?.x ?: (size.width / 2f)
        val centerY = offset?.y ?: (size.height / 2f)
        val radius = longestDistanceToACorner(size, offset) * progress
        
        return Outline.Generic(
            Path().apply {
                addOval(
                    Rect(
                        left = centerX - radius,
                        top = centerY - radius,
                        right = centerX + radius,
                        bottom = centerY + radius
                    )
                )
            }
        )
    }

    /**
     * Calculates the longest distance from the offset point to any corner of the view
     * This ensures the animation completely fills the screen regardless of offset position
     */
    private fun longestDistanceToACorner(size: Size, offset: Offset?): Float {
        if (offset == null) {
            return hypot(size.width / 2f, size.height / 2f)
        }

        val topLeft = hypot(offset.x, offset.y)
        val topRight = hypot(size.width - offset.x, offset.y)
        val bottomLeft = hypot(offset.x, size.height - offset.y)
        val bottomRight = hypot(size.width - offset.x, size.height - offset.y)

        return topLeft
            .coerceAtLeast(topRight)
            .coerceAtLeast(bottomLeft)
            .coerceAtLeast(bottomRight)
    }
}

/**
 * Modifier extension to apply circular reveal animation.
 * 
 * Creates a new CircularRevealShape on each call with the current progress.
 */
fun Modifier.circularReveal(
    progress: Float,
    offset: Offset? = null
) = clip(CircularRevealShape(progress, offset))

/**
 * Composable that animates content transitions with a circular reveal effect.
 * 
 * Optimized for performance:
 * - Uses remember for stable state management
 * - Minimizes recompositions by using key() for list items
 * - Efficient state change detection
 * - Cleans up intermediate items after animation completes
 * 
 * @param targetState The target state to transition to
 * @param modifier Modifier to apply to the container
 * @param animationSpec Animation specification for the reveal effect
 * @param revealFrom Optional offset to start the reveal animation from
 * @param content Composable content to display for each state
 */
@Composable
fun <T> CircularReveal(
    targetState: T,
    modifier: Modifier = Modifier,
    animationSpec: FiniteAnimationSpec<Float> = tween(600),
    revealFrom: Offset? = null,
    content: @Composable (T) -> Unit
) {
    // Stable state holders to prevent unnecessary recompositions
    val items = remember { mutableStateListOf<CircularRevealAnimationItem<T>>() }
    val transitionState = remember { MutableTransitionState(targetState) }
    val targetChanged = (targetState != transitionState.targetState)
    
    // Update target state only when changed
    transitionState.targetState = targetState
    val transition = rememberTransition(transitionState, label = "circularRevealTransition")

    if (targetChanged || items.isEmpty()) {
        // Only manipulate the list when the state is changed, or in the first run
        val keys = items.map { it.key }.run {
            if (!contains(targetState)) {
                toMutableList().also { it.add(targetState) }
            } else {
                this
            }
        }

        items.clear()
        keys.mapIndexedTo(items) { index, key ->
            CircularRevealAnimationItem(key) {
                val progress by transition.animateFloat(
                    transitionSpec = { animationSpec },
                    label = "progress"
                ) { state ->
                    if (index == keys.size - 1) {
                        if (state == key) 1f else 0f
                    } else {
                        1f
                    }
                }

                Box(
                    modifier = Modifier.circularReveal(
                        progress = progress,
                        offset = revealFrom
                    )
                ) {
                    content(key)
                }
            }
        }
    } else if (transitionState.currentState == transitionState.targetState) {
        // Remove all the intermediate items from the list once the animation is finished
        items.removeAll { it.key != transitionState.targetState }
    }

    Box(modifier = modifier) {
        items.forEach {
            key(it.key) {
                it.content()
            }
        }
    }
}
