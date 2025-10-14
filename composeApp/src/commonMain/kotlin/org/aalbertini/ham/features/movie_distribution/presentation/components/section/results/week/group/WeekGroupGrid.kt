@file:OptIn(ExperimentalLayoutApi::class)

package org.aalbertini.ham.features.movie_distribution.presentation.components.section.results.week.group

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.unit.Dp
import org.aalbertini.ham.features.movie_distribution.presentation.components.section.results.week.card.WeekResultCard

/**
 * Grid component that displays a group of week result cards.
 * 
 * Features:
 * - FlowRow layout for responsive arrangement
 * - Configurable column count and spacing
 * - Isolated recomposition per week using key()
 * 
 * @param startWeek First week number in this group (1-indexed)
 * @param endWeek Last week number in this group (1-indexed)
 * @param results List of all weekly screening results
 * @param availableScreeningsValue Total available screenings
 * @param availableScreeningsOverrideInputs Map of week-specific screenings overrides
 * @param weekMultiplierOverrideInputs Map of week-specific multiplier overrides
 * @param currentMovieResultId Current movie ID for state tracking
 * @param onAvailableScreeningsOverrideChange Callback for screenings override changes
 * @param onWeekMultiplierOverrideChange Callback for multiplier override changes
 * @param focusManager Focus manager for input fields
 * @param columns Number of columns in the grid
 * @param spacing Spacing between cards
 */
@Composable
internal fun WeekGroupGrid(
    startWeek: Int,
    endWeek: Int,
    results: List<Long>,
    availableScreeningsValue: Double,
    availableScreeningsOverrideInputs: Map<Int, String>,
    weekMultiplierOverrideInputs: Map<Int, String>,
    currentMovieResultId: String?,
    onAvailableScreeningsOverrideChange: (Int, String) -> Unit,
    onWeekMultiplierOverrideChange: (Int, String) -> Unit,
    focusManager: FocusManager,
    columns: Int,
    spacing: Dp,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalArrangement = Arrangement.spacedBy(spacing),
        maxItemsInEachRow = columns
    ) {
        for (weekNumber in startWeek..endWeek) {
            val weekIndex = weekNumber - 1
            
            // Key each week card to isolate recomposition
            key(weekIndex) {
                WeekResultCard(
                    weekNumber = weekNumber,
                    weekIndex = weekIndex,
                    resultValue = results[weekIndex],
                    availableScreeningsValue = availableScreeningsValue,
                    availableScreeningsOverrideInputs = availableScreeningsOverrideInputs,
                    weekMultiplierOverrideInputs = weekMultiplierOverrideInputs,
                    currentMovieResultId = currentMovieResultId,
                    onAvailableScreeningsOverrideChange = onAvailableScreeningsOverrideChange,
                    onWeekMultiplierOverrideChange = onWeekMultiplierOverrideChange,
                    focusManager = focusManager,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
