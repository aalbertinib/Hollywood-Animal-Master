package org.aalbertini.ham.core.ui.components.input

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.aalbertini.ham.core.ui.resources.Strings
import org.aalbertini.ham.core.ui.components.text.ErrorMessage
import org.jetbrains.compose.resources.stringResource

/**
 * Reusable validated numeric input field component.
 * 
 * Stateless and atomic component for numeric inputs with validation.
 * Can be used for any numeric input with custom validation logic.
 * 
 * Features:
 * - Numeric input with validation
 * - Optional clear button
 * - Visual validation feedback
 * - Customizable placeholder
 * - Focus state management
 * - Error message display
 * 
 * @param value Current input value
 * @param onValueChange Callback for value changes
 * @param label Input label text
 * @param placeholder Placeholder text
 * @param hasOverride Whether there's an active override/value
 * @param isValidated Whether value is validated and synced
 * @param isError Whether current value has errors
 * @param errorMessage Optional error message to display
 * @param keyboardType Type of keyboard to show
 * @param onFocusChanged Callback for focus changes
 * @param onValidateInput Callback to validate and sync input
 * @param onClearOverride Optional callback to clear the value
 * @param modifier Optional modifier
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValidatedNumericInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    hasOverride: Boolean,
    isValidated: Boolean,
    isError: Boolean,
    errorMessage: String? = null,
    keyboardType: KeyboardType = KeyboardType.Number,
    onFocusChanged: (Boolean) -> Unit,
    onValidateInput: () -> Unit,
    onClearOverride: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                onFocusChanged(focusState.isFocused)
            },
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            textAlign = TextAlign.Center
        ),
        label = if (hasOverride && !isValidated) {
            { Text(stringResource(Strings.pendingValidation), style = MaterialTheme.typography.labelSmall) }
        } else if (hasOverride && isValidated) {
            { Text(stringResource(Strings.overrideLabel), style = MaterialTheme.typography.labelSmall) }
        } else {
            { Text(label, style = MaterialTheme.typography.labelSmall) }
        },
        shape = RoundedCornerShape(8.dp),
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                textAlign = TextAlign.Center
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = if (!isValidated && hasOverride) {
                MaterialTheme.colorScheme.tertiary
            } else {
                MaterialTheme.colorScheme.primary
            },
            unfocusedBorderColor = if (!isValidated && hasOverride) {
                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f)
            } else {
                MaterialTheme.colorScheme.outline
            }
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { onValidateInput() }
        ),
        singleLine = true,
        trailingIcon = if (hasOverride && onClearOverride != null) {
            {
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = { PlainTooltip { Text(stringResource(Strings.clearOverride)) } },
                    state = rememberTooltipState()
                ) {
                    IconButton(onClick = onClearOverride) {
                        Icon(
                            Icons.Filled.Clear,
                            contentDescription = stringResource(Strings.clearOverride),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        } else null,
        isError = isError,
        supportingText = if (isError && errorMessage != null) {
            {
                ErrorMessage(text = errorMessage)
            }
        } else null,
        interactionSource = interactionSource
    )
}
