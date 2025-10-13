package org.aalbertini.ham.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.aalbertini.ham.core.ui.resources.UiConstants

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

/**
 * Reusable Modifier extension for animating content size changes with consistent animation behavior.
 * Uses a fast, smooth animation with standardized duration.
 * 
 * @return Modifier with animated content size behavior
 */
fun Modifier.animateContentSizeFast(): Modifier {
    return this.animateContentSize(
        animationSpec = tween(
            durationMillis = UiConstants.Animation.contentSizeDurationMs
        )
    )
}

/**
 * Animated corner radius values for section headers that coordinate with content expand/collapse.
 * 
 * When expanding: corners animate immediately from rounded to square (top corners remain rounded).
 * When collapsing: corners wait for content to collapse, then animate from square to rounded.
 * 
 * This creates a smooth, sequential animation that feels natural and polished.
 * 
 * @param expanded Whether the section is expanded
 * @param cornerRadius The corner radius value when collapsed (typically UiConstants.Card.cornerRadiusLarge)
 * @return Pair of Dp values for (bottomStart, bottomEnd) animated corner radii
 */
@Composable
fun animatedSectionHeaderCorners(
    expanded: Boolean,
    cornerRadius: Dp = UiConstants.Card.cornerRadiusLarge
): Pair<Dp, Dp> {
    val animationSpec = tween<Dp>(
        durationMillis = UiConstants.Animation.headerCornerRadiusDurationMs,
        delayMillis = if (expanded) 0 else UiConstants.Animation.headerCornerRadiusDelayMs
    )
    
    val targetRadius = if (expanded) 0.dp else cornerRadius
    
    val bottomStartRadius by animateDpAsState(
        targetValue = targetRadius,
        animationSpec = animationSpec,
        label = "bottomStartRadius"
    )
    
    val bottomEndRadius by animateDpAsState(
        targetValue = targetRadius,
        animationSpec = animationSpec,
        label = "bottomEndRadius"
    )
    
    return Pair(bottomStartRadius, bottomEndRadius)
}
