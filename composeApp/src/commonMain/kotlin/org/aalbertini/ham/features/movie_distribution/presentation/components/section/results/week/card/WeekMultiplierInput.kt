package org.aalbertini.ham.features.movie_distribution.presentation.components.section.results.week.card

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import org.aalbertini.ham.core.ui.components.input.LabeledValidatedNumericField
import org.aalbertini.ham.core.ui.resources.Strings
import org.jetbrains.compose.resources.stringResource
import org.aalbertini.ham.core.util.format.toFixed
import org.aalbertini.ham.core.util.input.isNumeric
import org.aalbertini.ham.core.util.input.sanitizeNumeric

/**
 * Week multiplier override input component.
 * 
 * Features:
 * - Numeric input with validation (0.0 to 10.0 range)
 * - Real-time error feedback
 * - Clear button for removing overrides
 * - Visual indicators for validation states
 * - Shows default multiplier value as placeholder
 * 
 * @param weekIndex Week index (0-indexed, used for callbacks)
 * @param displayValue Formatted display value
 * @param localOverrideInput Raw local input value
 * @param defaultMultiplier Default multiplier for this week
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
    
    LabeledValidatedNumericField(
        topLabel = stringResource(Strings.movieResultsWeekMultiplierOverride),
        value = displayValue,
        onValueChange = { newValue ->
            val cleanValue = sanitizeNumeric(newValue)
            if (cleanValue.isEmpty() || isNumeric(cleanValue)) {
                onLocalInputChange(cleanValue)
            }
        },
        inputLabel = stringResource(Strings.movieResultsWeekMultiplierOverride),
        placeholder = stringResource(
            Strings.defaultMultiplier,
            defaultMultiplier.toFixed(2)
        ),
        hasOverride = hasOverride,
        isValidated = isValidated,
        isError = isError,
        errorMessage = errorMessage,
        keyboardType = KeyboardType.Decimal,
        onFocusChanged = onFocusChanged,
        onValidateInput = onValidateInput,
        onClearOverride = onClearOverride,
        modifier = modifier
    )
}
