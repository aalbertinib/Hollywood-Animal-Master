@file:OptIn(ExperimentalLayoutApi::class)

package org.aalbertini.ham.features.movie_distribution.presentation.components.section.results

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

/**
 * Reusable FlowRow-based grid for results cards.
 * - Centralizes spacing and max-items-per-row behavior
 * - Supports keyed children to isolate recomposition
 */
@Composable
internal fun <T> ResultsFlowGrid(
    items: List<T>,
    modifier: Modifier = Modifier,
    horizontalSpacing: Dp,
    verticalSpacing: Dp = horizontalSpacing,
    maxItemsInRow: Int = 2,
    keyFactory: ((index: Int, item: T) -> Any)? = null,
    itemContent: @Composable (index: Int, item: T) -> Unit
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(horizontalSpacing),
        verticalArrangement = Arrangement.spacedBy(verticalSpacing),
        maxItemsInEachRow = maxItemsInRow
    ) {
        items.forEachIndexed { index, item ->
            val keyValue = keyFactory?.invoke(index, item)
            if (keyValue != null) {
                key(keyValue) {
                    itemContent(index, item)
                }
            } else {
                itemContent(index, item)
            }
        }
    }
}
