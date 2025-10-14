package org.aalbertini.ham.core.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import org.aalbertini.ham.core.ui.resources.UiConstants
import org.aalbertini.ham.core.ui.theme.CustomShapes

/**
 * Generic section content card wrapper.
 *
 * Provides consistent styling for section content areas:
 * - Border and corner radius
 * - Background colors
 * - Padding
 * - Optional expand/collapse animation
 *
 * Thread-safe and null-safe by design.
 *
 * @param modifier Optional modifier
 * @param backgroundColor Background color
 * @param borderColor Border color
 * @param borderWidth Border width
 * @param shape Card shape
 * @param expanded Whether content is expanded (affects animation)
 * @param content Card content
 */
@Composable
fun GenericSectionCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    borderColor: Color = MaterialTheme.colorScheme.outlineVariant,
    borderWidth: Dp = UiConstants.Card.borderWidth,
    shape: Shape = CustomShapes.SectionContentShape,
    expanded: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (borderWidth > Dp.Hairline) {
                    Modifier.border(
                        width = borderWidth,
                        color = borderColor,
                        shape = shape
                    )
                } else {
                    Modifier
                }
            )
            .animateContentSizeFast(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = shape
    ) {
        SectionAnimatedVisibility(visible = expanded) {
            Column(
                modifier = Modifier.padding(
                    horizontal = UiConstants.SectionHeader.horizontalPadding,
                    vertical = UiConstants.SectionHeader.verticalPadding
                ),
                content = content
            )
        }
    }
}

/**
 * Generic list item card.
 *
 * Provides consistent styling for list items with:
 * - Leading content (e.g., icon, avatar)
 * - Title and subtitle
 * - Trailing actions
 *
 * Follows Material Design list item patterns.
 * Thread-safe and null-safe.
 *
 * @param modifier Optional modifier
 * @param backgroundColor Background color
 * @param borderColor Border color
 * @param borderWidth Border width
 * @param shape Card shape
 * @param leadingContent Optional leading content
 * @param trailingActions Optional trailing actions
 * @param content Main content area
 */
@Composable
fun GenericListItemCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
    borderColor: Color = Color.Transparent,
    borderWidth: Dp = UiConstants.Card.borderWidth,
    shape: Shape = RoundedCornerShape(UiConstants.Card.cornerRadiusSmall),
    leadingContent: (@Composable () -> Unit)? = null,
    trailingActions: @Composable RowScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = borderWidth,
                color = borderColor,
                shape = shape
            ),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = shape
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(UiConstants.Padding.itemInCard),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(UiConstants.Spacing.betweenElements)
        ) {
            leadingContent?.invoke()

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(UiConstants.Spacing.betweenElements / 2),
                content = content
            )
        }
        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.spacedBy(UiConstants.Spacing.betweenElements / 2),
            verticalAlignment = Alignment.CenterVertically,
            content = trailingActions
        )
    }
}

/**
 * Generic action button wrapper with tooltip.
 *
 * Provides consistent behavior for action buttons:
 * - Tooltip support
 * - Icon button styling
 * - Null-safe tooltip handling
 *
 * @param onClick Click handler
 * @param icon Icon to display
 * @param contentDescription Accessibility description
 * @param tooltipText Optional tooltip text
 * @param modifier Optional modifier
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericActionButton(
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    contentDescription: String,
    tooltipText: String? = null,
    modifier: Modifier = Modifier
) {
    if (tooltipText != null) {
        TooltipBox(
            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
            tooltip = {
                PlainTooltip {
                    Text(tooltipText)
                }
            },
            state = rememberTooltipState(),
            modifier = modifier
        ) {
            IconButton(onClick = onClick) {
                icon()
            }
        }
    } else {
        IconButton(
            onClick = onClick,
            modifier = modifier
        ) {
            icon()
        }
    }
}
