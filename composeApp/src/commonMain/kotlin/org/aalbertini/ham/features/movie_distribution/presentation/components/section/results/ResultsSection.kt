package org.aalbertini.ham.features.movie_distribution.presentation.components.section.results

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.aalbertini.ham.core.ui.components.section.ModernSectionWithHeader
import org.aalbertini.ham.core.ui.resources.Strings
import org.aalbertini.ham.features.movie_distribution.domain.calculator.MovieDistributionConstants
import org.jetbrains.compose.resources.stringResource

/**
 * Modern results section with week cards and override inputs.
 * Header is inside the card.
 * 
 * Features:
 * - Clean card-based layout with header inside
 * - Responsive grid using FlowRow
 * - Week cards with override inputs
 * - Available screenings override per week
 * - Week multiplier override per week
 * - Animated content transitions
 * - Empty state message
 * - Copy and expand/collapse actions
 * 
 * @param results List of calculated weekly screenings
 * @param expanded Whether the section is expanded
 * @param hasResults Whether there are results to display
 * @param availableScreeningsValue Total available screenings
 * @param availableScreeningsOverrideInputs Map of week-specific screenings overrides
 * @param weekMultiplierOverrideInputs Map of week-specific multiplier overrides
 * @param currentMovieResultId Current movie ID for state tracking
 * @param onAvailableScreeningsOverrideChange Callback for screenings override changes
 * @param onWeekMultiplierOverrideChange Callback for multiplier override changes
 * @param onCopyClick Callback when copy button is clicked
 * @param onToggleExpand Callback when expand/collapse is toggled
 * @param modifier Optional modifier
 */
@OptIn(ExperimentalLayoutApi::class, androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun ResultsSection(
    results: List<Long>,
    expanded: Boolean,
    hasResults: Boolean,
    availableScreeningsValue: Double,
    availableScreeningsOverrideInputs: Map<Int, String>,
    weekMultiplierOverrideInputs: Map<Int, String>,
    currentMovieResultId: String?,
    onAvailableScreeningsOverrideChange: (weekIndex: Int, value: String) -> Unit,
    onWeekMultiplierOverrideChange: (weekIndex: Int, value: String) -> Unit,
    onCopyClick: () -> Unit,
    onToggleExpand: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    
    ModernSectionWithHeader(
        title = stringResource(Strings.resultsTitle),
        modifier = modifier,
        headerActions = {
            // Copy button
            if (hasResults) {
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = { PlainTooltip { Text(stringResource(Strings.actionCopyResults)) } },
                    state = rememberTooltipState()
                ) {
                    FilledTonalIconButton(
                        onClick = onCopyClick,
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    ) {
                        Icon(
                            Icons.Filled.ContentCopy,
                            contentDescription = stringResource(Strings.actionCopyResults)
                        )
                    }
                }
            }
            
            // Expand/collapse button
            if (hasResults) {
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = { PlainTooltip { Text(if (expanded) stringResource(Strings.actionCollapse) else stringResource(Strings.actionExpand)) } },
                    state = rememberTooltipState()
                ) {
                    FilledTonalIconButton(
                        onClick = onToggleExpand,
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    ) {
                        Icon(
                            if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                            contentDescription = if (expanded) stringResource(Strings.actionCollapse) else stringResource(Strings.actionExpand)
                        )
                    }
                }
            }
        }
    ) {
        if (expanded) {
            if (results.isEmpty()) {
                EmptyResultsMessage()
            } else {
                ModernResultsGrid(
                    results = results,
                    availableScreeningsValue = availableScreeningsValue,
                    availableScreeningsOverrideInputs = availableScreeningsOverrideInputs,
                    weekMultiplierOverrideInputs = weekMultiplierOverrideInputs,
                    onAvailableScreeningsOverrideChange = onAvailableScreeningsOverrideChange,
                    onWeekMultiplierOverrideChange = onWeekMultiplierOverrideChange,
                    focusManager = focusManager
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ModernResultsGrid(
    results: List<Long>,
    availableScreeningsValue: Double,
    availableScreeningsOverrideInputs: Map<Int, String>,
    weekMultiplierOverrideInputs: Map<Int, String>,
    onAvailableScreeningsOverrideChange: (Int, String) -> Unit,
    onWeekMultiplierOverrideChange: (Int, String) -> Unit,
    focusManager: androidx.compose.ui.focus.FocusManager
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Group results by month (4 weeks)
        results.chunked(4).forEachIndexed { monthIndex, monthWeeks ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Month header
                if (monthIndex > 0) {
                    MonthSeparator(monthNumber = monthIndex + 1)
                }
                
                // Weeks in this month
                ResultsFlowGrid(
                    items = monthWeeks,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalSpacing = 12.dp,
                    verticalSpacing = 12.dp,
                    maxItemsInRow = 2,
                    keyFactory = { weekInMonth, _ -> monthIndex * 4 + weekInMonth }
                ) { weekInMonth, screenings ->
                    val weekIndex = monthIndex * 4 + weekInMonth
                    // Compute default reduction percent relative to previous week's default multiplier
                    val currentDefault = MovieDistributionConstants.Multipliers.DEFAULT_WEEK_MULTIPLIERS.getOrNull(weekIndex) ?: 1.0
                    val previousDefault = if (weekIndex > 0) {
                        MovieDistributionConstants.Multipliers.DEFAULT_WEEK_MULTIPLIERS.getOrElse(weekIndex - 1) { 1.0 }
                    } else 1.0
                    val defaultReductionPercent = if (previousDefault > 0.0) {
                        ((1.0 - (currentDefault / previousDefault)) * 100).toInt().coerceIn(0, 100)
                    } else 0
                    ModernWeekResultCard(
                        weekNumber = weekIndex + 1,
                        screenings = screenings,
                        availableScreeningsValue = availableScreeningsValue,
                        availableScreeningsOverride = availableScreeningsOverrideInputs[weekIndex] ?: "",
                        weekMultiplierOverride = weekMultiplierOverrideInputs[weekIndex] ?: "",
                        weekDefaultReductionPercent = defaultReductionPercent,
                        onAvailableScreeningsOverrideChange = { value ->
                            onAvailableScreeningsOverrideChange(weekIndex, value)
                        },
                        onWeekMultiplierOverrideChange = { value ->
                            onWeekMultiplierOverrideChange(weekIndex, value)
                        },
                        focusManager = focusManager,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun MonthSeparator(monthNumber: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.outlineVariant
        )
        
        Text(
            text = "Month $monthNumber",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold
        )
        
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}

@Composable
private fun EmptyResultsMessage() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(
                Strings.messageEnterValidParameters,
                MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MIN.toString(),
                MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MAX.toInt().toString(),
                MovieDistributionConstants.Validation.SCREENINGS_MIN.toInt().toString()
            ),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
