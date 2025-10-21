package org.aalbertini.ham.core.ui.components.card

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.aalbertini.ham.core.ui.theme.CustomShapes

/**
 * Modern elevated card component with enhanced styling.
 * 
 * Features:
 * - Smooth elevation with shadow
 * - Modern rounded corners
 * - Optional border
 * - Animated content size changes
 * - Material3 color scheme integration
 * 
 * @param modifier Optional modifier
 * @param shape Card shape
 * @param containerColor Background color
 * @param contentColor Content color
 * @param elevation Elevation level
 * @param border Optional border
 * @param content Card content
 */
@Composable
fun ModernCard(
    modifier: Modifier = Modifier,
    shape: Shape = CustomShapes.ElevatedCardShape,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    elevation: Dp = 2.dp,
    border: BorderStroke? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevation,
            pressedElevation = elevation + 2.dp,
            hoveredElevation = elevation + 1.dp
        ),
        border = border
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            content = content
        )
    }
}

/**
 * Modern outlined card with subtle border and no elevation.
 * 
 * @param modifier Optional modifier
 * @param shape Card shape
 * @param containerColor Background color
 * @param contentColor Content color
 * @param borderColor Border color
 * @param borderWidth Border width
 * @param content Card content
 */
@Composable
fun ModernOutlinedCard(
    modifier: Modifier = Modifier,
    shape: Shape = CustomShapes.ElevatedCardShape,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    borderColor: Color = MaterialTheme.colorScheme.outlineVariant,
    borderWidth: Dp = 1.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    ModernCard(
        modifier = modifier,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        elevation = 0.dp,
        border = BorderStroke(borderWidth, borderColor),
        content = content
    )
}

/**
 * Modern surface card with minimal elevation.
 * 
 * @param modifier Optional modifier
 * @param shape Card shape
 * @param content Card content
 */
@Composable
fun ModernSurfaceCard(
    modifier: Modifier = Modifier,
    shape: Shape = CustomShapes.ListItemShape,
    content: @Composable ColumnScope.() -> Unit
) {
    ModernCard(
        modifier = modifier,
        shape = shape,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        elevation = 1.dp,
        content = content
    )
}
