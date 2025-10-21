package org.aalbertini.ham.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.aalbertini.ham.core.ui.resources.UiConstants

/**
 * Generic section header component that provides consistent styling and behavior
 * for all section headers in the application.
 * 
 * Follows SOLID principles:
 * - Single Responsibility: Handles only header layout and styling
 * - Open/Closed: Extensible through composition (content parameter)
 * - Dependency Inversion: Depends on abstractions (composable lambdas)
 * 
 * @param title The header title text
 * @param icon Optional leading icon for the title
 * @param expanded Whether the section is expanded (affects corner radius animation)
 * @param modifier Optional modifier for the header
 * @param titleStyle Optional text style for the title
 * @param backgroundColor Optional background color (defaults to primaryContainer)
 * @param tonalElevation Optional tonal elevation
 * @param actions Optional trailing actions (buttons, icons, etc.)
 */
@Composable
fun GenericSectionHeader(
    title: String,
    icon: EmojiIcon? = null,
    expanded: Boolean = true,
    modifier: Modifier = Modifier,
    titleStyle: TextStyle = MaterialTheme.typography.titleMedium,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
    tonalElevation: Dp = UiConstants.SectionHeader.tonalElevation,
    actions: @Composable RowScope.() -> Unit = {}
) {
    val (bottomStartRadius, bottomEndRadius) = animatedSectionHeaderCorners(expanded)
    
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = backgroundColor,
        tonalElevation = tonalElevation,
        shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomStart = bottomStartRadius,
            bottomEnd = bottomEndRadius
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = UiConstants.SectionHeader.horizontalPadding,
                    vertical = UiConstants.SectionHeader.verticalPadding
                )
                .height(UiConstants.SectionHeader.minHeight),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextIcon(
                text = title,
                icon = icon,
                style = titleStyle,
                modifier = Modifier.weight(1f, fill = false),
                softWrap = true
            )
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                actions()
            }
        }
    }
}
