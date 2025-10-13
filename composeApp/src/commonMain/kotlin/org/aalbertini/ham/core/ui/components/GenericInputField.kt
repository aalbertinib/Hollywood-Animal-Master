package org.aalbertini.ham.core.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import org.aalbertini.ham.core.ui.resources.UiStrings
import org.aalbertini.ham.core.ui.components.TextIcon
import org.aalbertini.ham.core.ui.components.animateContentSizeFast
import org.aalbertini.ham.core.ui.components.ErrorMessageAnimatedVisibility

/**
 * Generic validated input field with optional revert functionality.
 * 
 * Provides consistent behavior across all input fields:
 * - Thread-safe state management with derivedStateOf
 * - Null-safe validation
 * - Optional formatting (e.g., number formatting)
 * - Revert to original value functionality
 * - Error display with animation
 * 
 * @param value Current input value
 * @param onValueChange Callback when value changes
 * @param label Field label
 * @param modifier Optional modifier
 * @param icon Optional leading icon for label
 * @param placeholder Optional placeholder text
 * @param singleLine Whether the field is single line
 * @param maxLines Maximum number of lines
 * @param keyboardType Keyboard type for input
 * @param imeAction IME action for the keyboard
 * @param interactionSource Optional interaction source for focus tracking
 * @param isError Whether the field has an error
 * @param errorMessage Optional error message to display
 * @param originalValue Original value for revert functionality
 * @param onRevert Callback when revert is clicked
 * @param onFocusChange Callback when focus changes
 * @param onDone Callback when IME action is triggered
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValidatedInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    icon: EmojiIcon? = null,
    placeholder: String? = null,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    isError: Boolean = false,
    errorMessage: String? = null,
    originalValue: String? = null,
    onRevert: (() -> Unit)? = null,
    onFocusChange: ((Boolean) -> Unit)? = null,
    onDone: (() -> Unit)? = null
) {
    val focusManager = LocalFocusManager.current
    val isFocused by interactionSource.collectIsFocusedAsState()
    
    // Thread-safe derived state for showing revert button
    val showRevertButton by remember {
        derivedStateOf {
            originalValue != null && 
            value != originalValue && 
            value.isNotBlank() && 
            onRevert != null
        }
    }
    
    // Notify focus changes
    LaunchedEffect(isFocused) {
        onFocusChange?.invoke(isFocused)
    }
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSizeFast()
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                if (icon != null) {
                    TextIcon(text = label, icon = icon)
                } else {
                    Text(label)
                }
            },
            singleLine = singleLine,
            maxLines = maxLines,
            placeholder = placeholder?.let { { Text(it) } },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onDone?.invoke() ?: focusManager.clearFocus()
                }
            ),
            interactionSource = interactionSource,
            isError = !isFocused && isError,
            supportingText = if (!isFocused && isError && errorMessage != null) {
                {
                    ErrorMessageAnimatedVisibility(visible = true) {
                        Text(errorMessage)
                    }
                }
            } else null,
            trailingIcon = if (showRevertButton) {
                {
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                        tooltip = { PlainTooltip { Text(UiStrings.ACTION_REVERT_TO_ORIGINAL) } },
                        state = rememberTooltipState()
                    ) {
                        IconButton(onClick = { onRevert?.invoke() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.Undo,
                                contentDescription = UiStrings.ACTION_REVERT_TO_ORIGINAL,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            } else null,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * Generic number input field with formatting and validation.
 * 
 * Provides number-specific features:
 * - Automatic formatting when not focused (thousand separators)
 * - Raw number display when focused
 * - Range validation
 * - Null-safe numeric parsing
 * 
 * @param value Current numeric value as string
 * @param onValueChange Callback when value changes
 * @param label Field label
 * @param modifier Optional modifier
 * @param icon Optional leading icon for label
 * @param isInteger Whether the field accepts only integers
 * @param minValue Optional minimum valid value
 * @param maxValue Optional maximum valid value
 * @param formatWhenUnfocused Whether to format number when not focused
 * @param originalValue Original value for revert functionality
 * @param onRevert Callback when revert is clicked
 */
@Composable
fun NumberInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    icon: EmojiIcon? = null,
    isInteger: Boolean = false,
    minValue: Double? = null,
    maxValue: Double? = null,
    formatWhenUnfocused: Boolean = true,
    originalValue: String? = null,
    onRevert: (() -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val focusManager = LocalFocusManager.current
    
    // Thread-safe derived state for display value
    val displayValue by remember(value, isFocused, formatWhenUnfocused) {
        derivedStateOf {
            if (!isFocused && formatWhenUnfocused && value.isNotEmpty()) {
                value.toDoubleOrNull()?.toLong()?.let {
                    UiStrings.formatNumber(it)
                } ?: value
            } else {
                value
            }
        }
    }
    
    // Thread-safe validation
    val numericValue by remember(value) {
        derivedStateOf { value.toDoubleOrNull() }
    }
    
    val isValid by remember(numericValue, minValue, maxValue) {
        derivedStateOf {
            if (value.isEmpty()) {
                true
            } else {
                val num = numericValue
                num != null && 
                (minValue == null || num >= minValue) && 
                (maxValue == null || num <= maxValue)
            }
        }
    }
    
    val errorMessage by remember(isValid, minValue, maxValue) {
        derivedStateOf {
            if (!isValid && value.isNotEmpty()) {
                when {
                    numericValue == null -> "Invalid number"
                    minValue != null && maxValue != null -> 
                        "Must be between ${minValue.toLong()} and ${maxValue.toLong()}"
                    minValue != null -> "Must be at least ${minValue.toLong()}"
                    maxValue != null -> "Must be at most ${maxValue.toLong()}"
                    else -> "Invalid value"
                }
            } else {
                null
            }
        }
    }
    
    // Handle focus changes for formatting
    LaunchedEffect(isFocused) {
        if (isFocused && formatWhenUnfocused) {
            // Remove formatting when gaining focus
            val num = value.toDoubleOrNull()
            if (num != null && num > 0.0) {
                val rawValue = if (isInteger) num.toLong().toString() else num.toString()
                if (value != rawValue) {
                    onValueChange(rawValue)
                }
            }
        } else if (!isFocused && formatWhenUnfocused) {
            // Normalize when losing focus
            val num = value.toDoubleOrNull()
            if (num != null && num > 0.0) {
                val normalizedValue = if (isInteger) num.toLong().toString() else num.toString()
                if (value != normalizedValue) {
                    onValueChange(normalizedValue)
                }
            }
        }
    }
    
    ValidatedInputField(
        value = displayValue,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier,
        icon = icon,
        keyboardType = if (isInteger) KeyboardType.Number else KeyboardType.Decimal,
        interactionSource = interactionSource,
        isError = !isValid,
        errorMessage = errorMessage,
        originalValue = originalValue,
        onRevert = onRevert,
        onDone = {
            if (isValid) {
                focusManager.clearFocus()
            }
        }
    )
}
