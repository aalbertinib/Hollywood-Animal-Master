package org.aalbertini.ham.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Reusable AnimatedVisibility composable with consistent section expand/collapse animations.
 * Uses fade and vertical expand/shrink animations with standardized duration.
 * 
 * @param visible Controls the visibility of the content
 * @param content The composable content to show/hide with animation
 */
@Composable
fun SectionAnimatedVisibility(
    visible: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    val durationMs = UiConstants.Animation.sectionExpandCollapseDurationMs
    
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(durationMs)) + expandVertically(animationSpec = tween(durationMs)),
        exit = fadeOut(animationSpec = tween(durationMs)) + shrinkVertically(animationSpec = tween(durationMs)),
        content = content
    )
}

/**
 * Reusable AnimatedVisibility composable for error messages with consistent animation behavior.
 * Uses fade and vertical expand/shrink animations with standardized duration.
 * 
 * @param visible Controls the visibility of the error message
 * @param content The composable content (typically error text) to show/hide with animation
 */
@Composable
fun ErrorMessageAnimatedVisibility(
    visible: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    val durationMs = UiConstants.Animation.errorMessageDurationMs
    
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(durationMs)) + expandVertically(animationSpec = tween(durationMs)),
        exit = fadeOut(animationSpec = tween(durationMs)) + shrinkVertically(animationSpec = tween(durationMs)),
        content = content
    )
}

/**
 * Reusable animated icon composable with crossfade animation when the icon changes.
 * Uses a smooth crossfade transition with standardized duration.
 * 
 * @param imageVector The current icon to display
 * @param contentDescription Accessibility description for the icon
 * @param modifier Optional modifier for the icon
 * @param tint Optional tint color for the icon (defaults to LocalContentColor)
 */
@Composable
fun AnimatedIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) {
    val durationMs = UiConstants.Animation.sectionExpandCollapseDurationMs
    
    Crossfade(
        targetState = imageVector,
        animationSpec = tween(durationMs),
        modifier = modifier
    ) { icon ->
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint
        )
    }
}
