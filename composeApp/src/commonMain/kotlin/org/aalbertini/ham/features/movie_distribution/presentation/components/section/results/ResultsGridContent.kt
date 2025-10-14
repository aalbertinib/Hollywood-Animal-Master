@file:OptIn(ExperimentalLayoutApi::class)

package org.aalbertini.ham.features.movie_distribution.presentation.components.section.results

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.unit.dp
import org.aalbertini.ham.core.ui.resources.UiConstants
import org.aalbertini.ham.features.movie_distribution.domain.calculator.MovieDistributionConstants
import org.aalbertini.ham.features.movie_distribution.presentation.components.section.results.week.group.WeekGroupGrid
import org.aalbertini.ham.features.movie_distribution.presentation.components.section.results.week.group.WeekGroupSeparator

/**
 * Grid content component that manages responsive layout and week grouping.
 * 
 * Features:
 * - Dynamic column calculation based on screen width (1-2 columns max)
 * - Groups weeks into sets of 4
 * - Adds visual separators between groups
 * - Optimal spacing and card sizing
 * 
 * @param results List of weekly screening results
 * @param availableScreeningsValue Total available screenings
 * @param availableScreeningsOverrideInputs Map of week-specific screenings overrides
 * @param weekMultiplierOverrideInputs Map of week-specific multiplier overrides
 * @param currentMovieResultId Current movie ID for state tracking
 * @param onAvailableScreeningsOverrideChange Callback for screenings override changes
 * @param onWeekMultiplierOverrideChange Callback for multiplier override changes
 * @param focusManager Focus manager for input fields
 */
@Composable
internal fun ResultsGridContent(
    results: List<Long>,
    availableScreeningsValue: Double,
    availableScreeningsOverrideInputs: Map<Int, String>,
    weekMultiplierOverrideInputs: Map<Int, String>,
    currentMovieResultId: String?,
    onAvailableScreeningsOverrideChange: (Int, String) -> Unit,
    onWeekMultiplierOverrideChange: (Int, String) -> Unit,
    focusManager: FocusManager
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val minCardWidth = 200.dp
        val spacing = 12.dp
        val horizontalPadding = UiConstants.SectionHeader.horizontalPadding * 2
        
        // Calculate optimal number of columns based on available width (max 2 columns)
        val columns = remember(maxWidth) {
            val availableWidth = maxWidth - horizontalPadding
            ((availableWidth + spacing) / (minCardWidth + spacing)).toInt().coerceIn(1, 2)
        }
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = UiConstants.SectionHeader.verticalPadding
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val totalWeeks = MovieDistributionConstants.WeeklyCalculation.NUMBER_OF_WEEKS
            val weeksPerGroup = 4
            val numberOfGroups = (totalWeeks + weeksPerGroup - 1) / weeksPerGroup
            
            for (groupIndex in 0 until numberOfGroups) {
                val startWeek = groupIndex * weeksPerGroup + 1
                val endWeek = minOf((groupIndex + 1) * weeksPerGroup, totalWeeks)
                
                // Add separator before each group except the first
                if (groupIndex > 0) {
                    WeekGroupSeparator(groupNumber = groupIndex + 1)
                }
                
                // Week group grid
                WeekGroupGrid(
                    startWeek = startWeek,
                    endWeek = endWeek,
                    results = results,
                    availableScreeningsValue = availableScreeningsValue,
                    availableScreeningsOverrideInputs = availableScreeningsOverrideInputs,
                    weekMultiplierOverrideInputs = weekMultiplierOverrideInputs,
                    currentMovieResultId = currentMovieResultId,
                    onAvailableScreeningsOverrideChange = onAvailableScreeningsOverrideChange,
                    onWeekMultiplierOverrideChange = onWeekMultiplierOverrideChange,
                    focusManager = focusManager,
                    columns = columns,
                    spacing = spacing,
                    modifier = Modifier.padding(horizontal = UiConstants.SectionHeader.horizontalPadding)
                )
            }
        }
    }
}
