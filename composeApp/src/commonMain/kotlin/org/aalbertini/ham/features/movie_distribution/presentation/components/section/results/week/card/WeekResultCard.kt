package org.aalbertini.ham.features.movie_distribution.presentation.components.section.results.week.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.unit.dp
import org.aalbertini.ham.features.movie_distribution.domain.calculator.MovieDistributionConstants
import org.aalbertini.ham.features.movie_distribution.domain.formatting.formatNumberThousands
import kotlin.math.abs

/**
 * Individual week result card component with state management and override inputs.
 *
 * Features:
 * - Local state management for input handling
 * - Focus tracking and validation
 * - Synchronized state with ViewModel
 * - Alternating header colors for visual distinction
 * - Supports both screenings and multiplier overrides
 *
 * @param weekNumber Week number to display (1-indexed)
 * @param weekIndex Week index in results array (0-indexed)
 * @param resultValue Calculated screening result
 * @param availableScreeningsValue Total available screenings
 * @param availableScreeningsOverrideInputs Map of week-specific screenings overrides
 * @param weekMultiplierOverrideInputs Map of week-specific multiplier overrides
 * @param currentMovieResultId Current movie ID for state tracking
 * @param onAvailableScreeningsOverrideChange Callback for screenings override changes
 * @param onWeekMultiplierOverrideChange Callback for multiplier override changes
 * @param focusManager Focus manager for input fields
 * @param modifier Optional modifier
 */
@Composable
internal fun WeekResultCard(
    weekNumber: Int,
    weekIndex: Int,
    resultValue: Long,
    availableScreeningsValue: Double,
    availableScreeningsOverrideInputs: Map<Int, String>,
    weekMultiplierOverrideInputs: Map<Int, String>,
    currentMovieResultId: String?,
    onAvailableScreeningsOverrideChange: (Int, String) -> Unit,
    onWeekMultiplierOverrideChange: (Int, String) -> Unit,
    focusManager: FocusManager,
    modifier: Modifier = Modifier
) {
    // Screenings override local state
    var localScreeningsInput by remember(weekIndex) {
        mutableStateOf(availableScreeningsOverrideInputs[weekIndex] ?: "")
    }
    var screeningsFocused by remember(weekIndex) { mutableStateOf(false) }

    // Multiplier override local state
    var localMultiplierInput by remember(weekIndex) {
        mutableStateOf(weekMultiplierOverrideInputs[weekIndex] ?: "")
    }
    var multiplierFocused by remember(weekIndex) { mutableStateOf(false) }

    // Get default multiplier for this week
    val defaultMultiplier = remember(weekIndex) {
        MovieDistributionConstants.Multipliers.DEFAULT_WEEK_MULTIPLIERS.getOrElse(weekIndex) { 1.0 }
    }

    // Track if screenings input is validated
    val screeningsValidated by remember {
        derivedStateOf {
            val vmValue = availableScreeningsOverrideInputs[weekIndex] ?: ""
            if (localScreeningsInput.isEmpty() && vmValue.isEmpty()) {
                true
            } else {
                val localNum = localScreeningsInput.toDoubleOrNull()
                val vmNum = vmValue.toDoubleOrNull()
                localNum != null && vmNum != null && abs(localNum - vmNum) < 0.0001
            }
        }
    }
    // Track if multiplier input is validated
    val multiplierValidated by remember {
        derivedStateOf {
            val vmValue = weekMultiplierOverrideInputs[weekIndex] ?: ""
            if (localMultiplierInput.isEmpty() && vmValue.isEmpty()) {
                true
            } else {
                val localNum = localMultiplierInput.toDoubleOrNull()
                val vmNum = vmValue.toDoubleOrNull()
                localNum != null && vmNum != null && abs(localNum - vmNum) < 0.0001
            }
        }
    }
    // Check if screenings input is valid
    val screeningsInputValid by remember {
        derivedStateOf {
            WeekCardValidation.isValidScreeningsInput(localScreeningsInput, availableScreeningsValue)
        }
    }

    // Check if multiplier input is valid
    val multiplierInputValid by remember {
        derivedStateOf {
            WeekCardValidation.isValidMultiplierInput(localMultiplierInput)
        }
    }

    // Sync screenings external changes
    LaunchedEffect(
        screeningsFocused,
        availableScreeningsOverrideInputs,
        weekIndex,
        currentMovieResultId
    ) {
        if (!screeningsFocused) {
            val externalInput = availableScreeningsOverrideInputs[weekIndex] ?: ""
            if (externalInput != localScreeningsInput) {
                localScreeningsInput = externalInput
            }
        }
    }

    // Sync multiplier external changes
    LaunchedEffect(
        multiplierFocused,
        weekMultiplierOverrideInputs,
        weekIndex,
        currentMovieResultId
    ) {
        if (!multiplierFocused) {
            val externalInput = weekMultiplierOverrideInputs[weekIndex] ?: ""
            if (externalInput != localMultiplierInput) {
                localMultiplierInput = externalInput
            }
        }
    }

    val hasScreeningsOverride by remember {
        derivedStateOf { localScreeningsInput.isNotEmpty() }
    }
    val hasMultiplierOverride by remember {
        derivedStateOf { localMultiplierInput.isNotEmpty() }
    }
    val hasAnyOverride by remember {
        derivedStateOf { hasScreeningsOverride || hasMultiplierOverride }
    }
    val allValidated by remember {
        derivedStateOf {
            screeningsValidated && multiplierValidated
        }
    }

    // Display formatted screenings value
    val screeningsDisplayValue by remember {
        derivedStateOf {
            if (!screeningsFocused && hasScreeningsOverride) {
                localScreeningsInput.toDoubleOrNull()?.toLong()?.formatNumberThousands()
                    ?: localScreeningsInput
            } else {
                localScreeningsInput
            }
        }
    }

    // Display formatted multiplier value
    val multiplierDisplayValue by remember {
        derivedStateOf {
            if (!multiplierFocused && hasMultiplierOverride) {
                localMultiplierInput
            } else {
                localMultiplierInput
            }
        }
    }

    val backgroundColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WeekCardHeader(weekNumber = weekNumber)

            WeekResultValue(
                resultValue = resultValue,
                isValidated = allValidated,
                hasOverride = hasAnyOverride
            )

            WeekOverrideInput(
                weekIndex = weekIndex,
                displayValue = screeningsDisplayValue,
                localOverrideInput = localScreeningsInput,
                availableScreeningsValue = availableScreeningsValue,
                hasOverride = hasScreeningsOverride,
                isValidated = screeningsValidated,
                isInputValid = screeningsInputValid,
                isFocused = screeningsFocused,
                onLocalInputChange = { localScreeningsInput = it },
                onFocusChanged = { focused ->
                    val wasFocused = screeningsFocused
                    screeningsFocused = focused
                    if (wasFocused && !focused && screeningsInputValid && !screeningsValidated) {
                        val numValue = localScreeningsInput.toDoubleOrNull()
                        val normalizedInput =
                            if (numValue != null && numValue == numValue.toLong().toDouble()) {
                                numValue.toLong().toString()
                            } else {
                                localScreeningsInput
                            }
                        localScreeningsInput = normalizedInput
                        onAvailableScreeningsOverrideChange(weekIndex, normalizedInput)
                    }
                },
                onValidateInput = {
                    if (screeningsInputValid) {
                        val numValue = localScreeningsInput.toDoubleOrNull()
                        val normalizedInput =
                            if (numValue != null && numValue == numValue.toLong().toDouble()) {
                                numValue.toLong().toString()
                            } else {
                                localScreeningsInput
                            }
                        localScreeningsInput = normalizedInput
                        onAvailableScreeningsOverrideChange(weekIndex, normalizedInput)
                        focusManager.clearFocus()
                    }
                },
                onClearOverride = {
                    localScreeningsInput = ""
                    onAvailableScreeningsOverrideChange(weekIndex, "")
                    focusManager.clearFocus()
                }
            )

            WeekMultiplierInput(
                weekIndex = weekIndex,
                displayValue = multiplierDisplayValue,
                localOverrideInput = localMultiplierInput,
                defaultMultiplier = defaultMultiplier,
                hasOverride = hasMultiplierOverride,
                isValidated = multiplierValidated,
                isInputValid = multiplierInputValid,
                isFocused = multiplierFocused,
                onLocalInputChange = { localMultiplierInput = it },
                onFocusChanged = { focused ->
                    val wasFocused = multiplierFocused
                    multiplierFocused = focused
                    if (wasFocused && !focused && multiplierInputValid && !multiplierValidated) {
                        onWeekMultiplierOverrideChange(weekIndex, localMultiplierInput)
                    }
                },
                onValidateInput = {
                    if (multiplierInputValid) {
                        onWeekMultiplierOverrideChange(weekIndex, localMultiplierInput)
                        focusManager.clearFocus()
                    }
                },
                onClearOverride = {
                    localMultiplierInput = ""
                    onWeekMultiplierOverrideChange(weekIndex, "")
                    focusManager.clearFocus()
                }
            )
        }
    }
}
