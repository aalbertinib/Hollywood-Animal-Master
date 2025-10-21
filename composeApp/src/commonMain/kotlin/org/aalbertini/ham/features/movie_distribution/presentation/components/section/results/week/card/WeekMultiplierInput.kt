package org.aalbertini.ham.features.movie_distribution.presentation.components.section.results.week.card

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import org.aalbertini.ham.core.ui.components.input.LabeledValidatedNumericField
import org.aalbertini.ham.core.ui.resources.Strings
import org.jetbrains.compose.resources.stringResource

/**
 * Week multiplier override input component.
 * 
 * Features:
 * - Reduction percentage input (0% to 100% range, positive values only)
 * - Real-time error feedback
 * - Clear button for removing overrides
 * - Visual indicators for validation states
 * - Shows default as reduction % from previous week (e.g., 20% means result is 80% of previous)
 * 
 * @param weekIndex Week index (0-indexed, used for callbacks)
 * @param displayValue Formatted display value
 * @param localOverrideInput Raw local input value (reduction percentage as positive integer)
 * @param defaultMultiplier Default multiplier for this week
 * @param previousWeekDefaultMultiplier Default multiplier for previous week (used to calculate reduction %)
 * @param hasOverride Whether there's an active override
 * @param isValidated Whether override is synced with ViewModel
 * @param isInputValid Whether current input is valid
 * @param isFocused Whether input field is focused
 * @param onLocalInputChange Callback for local input changes
 * @param onFocusChanged Callback for focus state changes
 * @param onValidateInput Callback to validate and sync input
 * @param onClearOverride Callback to clear override
 * @param modifier Optional modifier
 */
@Composable
internal fun WeekMultiplierInput(
    weekIndex: Int,
    displayValue: String,
    localOverrideInput: String,
    defaultMultiplier: Double,
    previousWeekDefaultMultiplier: Double,
    hasOverride: Boolean,
    isValidated: Boolean,
    isInputValid: Boolean,
    isFocused: Boolean,
    onLocalInputChange: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit,
    onValidateInput: () -> Unit,
    onClearOverride: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isError = localOverrideInput.isNotEmpty() && !isInputValid
    
    val errorMessage = if (isError) {
        stringResource(Strings.validationMultiplierRange)
    } else null
    
    // Convert default multiplier to reduction percentage from previous week
    // e.g., if previous=1.0 and current=0.8, then reduction = (1 - (0.8/1.0))*100 = 20%
    val defaultReductionPercentage = if (previousWeekDefaultMultiplier > 0.0) {
        ((1.0 - (defaultMultiplier / previousWeekDefaultMultiplier)) * 100).toInt()
    } else {
        0
    }
    val placeholderText = "$defaultReductionPercentage%"
    
    LabeledValidatedNumericField(
        topLabel = stringResource(Strings.movieResultsWeekMultiplierOverride),
        value = displayValue,
        onValueChange = { newValue ->
            // Allow only positive integers (no negative values, only reductions)
            val filtered = newValue.filter { it.isDigit() }
            onLocalInputChange(filtered)
        },
        placeholder = placeholderText,
        hasOverride = hasOverride,
        isValidated = isValidated,
        isError = isError,
        errorMessage = errorMessage,
        keyboardType = KeyboardType.Number,
        onFocusChanged = onFocusChanged,
        onValidateInput = onValidateInput,
        onClearOverride = onClearOverride,
        modifier = modifier
    )
}
