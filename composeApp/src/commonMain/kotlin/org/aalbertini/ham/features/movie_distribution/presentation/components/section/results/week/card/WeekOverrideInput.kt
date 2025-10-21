package org.aalbertini.ham.features.movie_distribution.presentation.components.section.results.week.card

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import org.aalbertini.ham.core.ui.components.input.LabeledValidatedNumericField
import org.aalbertini.ham.core.ui.resources.Strings
import org.aalbertini.ham.features.movie_distribution.domain.formatting.formatNumberThousands
import org.aalbertini.ham.core.util.input.isNumeric
import org.aalbertini.ham.core.util.input.sanitizeNumeric
import org.jetbrains.compose.resources.stringResource

/**
 * Override input component for week cards.
 * 
 * Features:
 * - Numeric input with validation
 * - Real-time error feedback
 * - Clear button for removing overrides
 * - Visual indicators for validation states
 * - Formatted number display when not focused
 * 
 * @param weekIndex Week index (0-indexed, used for callbacks)
 * @param displayValue Formatted display value
 * @param localOverrideInput Raw local input value
 * @param availableScreeningsValue Maximum allowed value
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
internal fun WeekOverrideInput(
        weekIndex: Int,
        displayValue: String,
        localOverrideInput: String,
        availableScreeningsValue: Double,
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
        if (availableScreeningsValue > 0.0) {
            stringResource(
                Strings.validationBetweenZeroAnd,
                availableScreeningsValue.toLong().formatNumberThousands()
            )
        } else {
            stringResource(Strings.enterAvailableScreeningsFirst)
        }
    } else null
    
    LabeledValidatedNumericField(
        topLabel = stringResource(Strings.movieResultsYourScreeningsOverride),
        value = displayValue,
        onValueChange = { newValue ->
            val cleanValue = sanitizeNumeric(newValue)
            if (cleanValue.isEmpty() || isNumeric(cleanValue)) {
                onLocalInputChange(cleanValue)
            }
        },
        placeholder = availableScreeningsValue.toLong().formatNumberThousands(),
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
