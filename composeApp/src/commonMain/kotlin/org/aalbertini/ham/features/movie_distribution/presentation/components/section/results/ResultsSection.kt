package org.aalbertini.ham.features.movie_distribution.presentation.components.section.results

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import org.aalbertini.ham.core.ui.components.GenericSectionCard
import org.aalbertini.ham.core.ui.resources.Strings
import org.aalbertini.ham.features.movie_distribution.domain.calculator.MovieDistributionConstants
import org.jetbrains.compose.resources.stringResource

/**
 * Main results section component that displays weekly distribution results.
 * 
 * Features:
 * - Animated visibility and content transitions
 * - Empty state message when no results available
 * - Responsive grid layout for results
 * 
 * @param results List of calculated weekly screenings
 * @param expanded Whether the section is expanded
 * @param availableScreeningsValue Total available screenings
 * @param availableScreeningsOverrideInputs Map of week-specific screenings overrides
 * @param weekMultiplierOverrideInputs Map of week-specific multiplier overrides
 * @param currentMovieResultId Current movie ID for state tracking
 * @param onAvailableScreeningsOverrideChange Callback for screenings override changes
 * @param onWeekMultiplierOverrideChange Callback for multiplier override changes
 * @param modifier Optional modifier
 */
@Composable
fun ResultsSection(
    results: List<Long>,
    expanded: Boolean,
    availableScreeningsValue: Double,
    availableScreeningsOverrideInputs: Map<Int, String>,
    weekMultiplierOverrideInputs: Map<Int, String>,
    currentMovieResultId: String?,
    onAvailableScreeningsOverrideChange: (weekIndex: Int, value: String) -> Unit,
    onWeekMultiplierOverrideChange: (weekIndex: Int, value: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    
    GenericSectionCard(
        modifier = modifier,
        expanded = expanded
    ) {
        AnimatedContent(
            targetState = results,
            label = "results",
            transitionSpec = { fadeIn() togetherWith fadeOut() }
        ) { list ->
            if (list.isEmpty()) {
                EmptyResultsMessage()
            } else {
                ResultsGridContent(
                    results = list,
                    availableScreeningsValue = availableScreeningsValue,
                    availableScreeningsOverrideInputs = availableScreeningsOverrideInputs,
                    weekMultiplierOverrideInputs = weekMultiplierOverrideInputs,
                    currentMovieResultId = currentMovieResultId,
                    onAvailableScreeningsOverrideChange = onAvailableScreeningsOverrideChange,
                    onWeekMultiplierOverrideChange = onWeekMultiplierOverrideChange,
                    focusManager = focusManager
                )
            }
        }
    }
}

@Composable
private fun EmptyResultsMessage() {
    Text(
        text = stringResource(
            Strings.messageEnterValidParameters,
            MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MIN.toString(),
            MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MAX.toInt().toString(),
            MovieDistributionConstants.Validation.SCREENINGS_MIN.toInt().toString()
        ),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        softWrap = true
    )
}
