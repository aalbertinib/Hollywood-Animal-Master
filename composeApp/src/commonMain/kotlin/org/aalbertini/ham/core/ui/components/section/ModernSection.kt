package org.aalbertini.ham.core.ui.components.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Modern section component combining header and content card.
 * 
 * Matches dashboard design with:
 * - Clean header without background
 * - Content in elevated card
 * - Proper spacing between elements
 * 
 * @param title Section title
 * @param modifier Optional modifier
 * @param subtitle Optional subtitle or count
 * @param headerActions Optional header actions
 * @param content Section content
 */
@Composable
fun ModernSection(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    headerActions: @Composable RowScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ModernSectionHeader(
            title = title,
            subtitle = subtitle,
            actions = headerActions
        )
        
        ModernContentCard(
            content = content
        )
    }
}

/**
 * Compact modern section with less padding.
 * 
 * @param title Section title
 * @param modifier Optional modifier
 * @param subtitle Optional subtitle or count
 * @param headerActions Optional header actions
 * @param content Section content
 */
@Composable
fun ModernSectionCompact(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    headerActions: @Composable RowScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ModernSectionHeader(
            title = title,
            subtitle = subtitle,
            actions = headerActions
        )
        
        ModernContentCardCompact(
            content = content
        )
    }
}
