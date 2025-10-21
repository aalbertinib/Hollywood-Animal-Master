package org.aalbertini.ham.core.ui.components.input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.aalbertini.ham.core.ui.components.text.SmallCenteredLabel

/**
 * Wrapper that displays a small centered label above a validated numeric input.
 * Keeps screens/components DRY while preserving atomic, stateless behavior.
 */
@Composable
fun LabeledValidatedNumericField(
    topLabel: String,
    value: String,
    onValueChange: (String) -> Unit,
    inputLabel: String = "",
    placeholder: String,
    hasOverride: Boolean,
    isValidated: Boolean,
    isError: Boolean,
    errorMessage: String? = null,
    keyboardType: KeyboardType = KeyboardType.Number,
    onFocusChanged: (Boolean) -> Unit,
    onValidateInput: () -> Unit,
    onClearOverride: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        SmallCenteredLabel(text = topLabel)

        ValidatedNumericInput(
            value = value,
            onValueChange = onValueChange,
            label = inputLabel,
            placeholder = placeholder,
            hasOverride = hasOverride,
            isValidated = isValidated,
            isError = isError,
            errorMessage = errorMessage,
            keyboardType = keyboardType,
            onFocusChanged = onFocusChanged,
            onValidateInput = onValidateInput,
            onClearOverride = onClearOverride,
        )
    }
}
