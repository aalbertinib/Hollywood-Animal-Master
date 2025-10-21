package org.aalbertini.ham.features.movie_distribution.presentation.components.dialog

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.aalbertini.ham.core.ui.components.animateContentSizeFast
import org.aalbertini.ham.core.ui.components.button.DialogActionButtons
import org.aalbertini.ham.core.ui.resources.Strings
import org.aalbertini.ham.core.ui.resources.UiConstants
import org.aalbertini.ham.core.ui.theme.CustomShapes
import org.aalbertini.ham.core.util.input.filterIntegerInput
import org.aalbertini.ham.core.util.input.filterNumericInput
import org.aalbertini.ham.features.movie_distribution.domain.calculator.MovieDistributionConstants
import org.aalbertini.ham.features.movie_distribution.domain.formatting.formatNumberThousands
import org.aalbertini.ham.features.movie_distribution.domain.model.MovieResult
import org.jetbrains.compose.resources.stringResource

/**
 * Edit movie result dialog.
 * 
 * Features:
 * - Editable title, commercial score, and screenings
 * - Input validation with range enforcement
 * - Number formatting for screenings
 * - Real-time validation feedback
 * 
 * @param movieResult Movie result to edit
 * @param onDismiss Callback when dialog is dismissed
 * @param onUpdate Callback when update is confirmed
 */
@Composable
fun EditDialog(
    movieResult: MovieResult,
    onDismiss: () -> Unit,
    onUpdate: (String, Double, Double) -> Unit
) {
    var title by remember(movieResult.title) { mutableStateOf(movieResult.title) }
    var commercialScoreInput by remember(movieResult.commercialScore) { 
        mutableStateOf(movieResult.commercialScore.toString()) 
    }
    var screeningsInput by remember(movieResult.numberOfScreenings) { 
        mutableStateOf(movieResult.numberOfScreenings.toLong().toString()) 
    }
    
    val commercialScoreInteractionSource = remember { MutableInteractionSource() }
    val screeningsInteractionSource = remember { MutableInteractionSource() }
    val commercialScoreIsFocused by commercialScoreInteractionSource.collectIsFocusedAsState()
    val screeningsIsFocused by screeningsInteractionSource.collectIsFocusedAsState()
    
    val displayScreenings = remember(screeningsInput, screeningsIsFocused) {
        if (!screeningsIsFocused && screeningsInput.isNotEmpty()) {
            screeningsInput.toDoubleOrNull()?.toLong()?.formatNumberThousands() ?: screeningsInput
        } else {
            screeningsInput
        }
    }
    
    LaunchedEffect(screeningsIsFocused) {
        if (screeningsIsFocused) {
            val numValue = screeningsInput.toDoubleOrNull()
            if (numValue != null && numValue > 0.0) {
                val rawValue = numValue.toLong().toString()
                if (screeningsInput != rawValue) {
                    screeningsInput = rawValue
                }
            }
        } else if (!screeningsIsFocused) {
            val numValue = screeningsInput.toDoubleOrNull()
            if (numValue != null && numValue > 0.0) {
                val normalizedValue = numValue.toLong().toString()
                if (screeningsInput != normalizedValue) {
                    screeningsInput = normalizedValue
                }
            }
        }
    }
    
    val commercialScore = remember(commercialScoreInput) { commercialScoreInput.toDoubleOrNull() }
    val screeningsValue = remember(screeningsInput) { screeningsInput.toDoubleOrNull() }
    
    val commercialScoreValid = remember(commercialScore) {
        commercialScore != null && 
        commercialScore >= MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MIN && 
        commercialScore <= MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MAX
    }
    
    val screeningsValid = remember(screeningsValue) {
        screeningsValue != null && 
        screeningsValue >= MovieDistributionConstants.Validation.SCREENINGS_MIN && 
        screeningsValue <= MovieDistributionConstants.Validation.SCREENINGS_MAX
    }

    EditDialogContent(
        title = title,
        onTitleChange = { title = it },
        commercialScoreInput = commercialScoreInput,
        onCommercialScoreChange = { newValue ->
            val filtered = newValue.filterNumericInput(maxDecimalPlaces = 1)
            val finalValue = if (filtered.isNotEmpty() && !filtered.endsWith(".")) {
                val numValue = filtered.toDoubleOrNull()
                if (numValue != null && numValue > MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MAX) {
                    MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MAX.toString()
                } else {
                    filtered
                }
            } else {
                filtered
            }
            commercialScoreInput = finalValue
        },
        screeningsInput = displayScreenings,
        onScreeningsChange = { newValue ->
            val filtered = newValue.filterIntegerInput()
            val finalValue = if (filtered.isNotEmpty()) {
                val numValue = filtered.toDoubleOrNull()
                if (numValue != null && numValue > MovieDistributionConstants.Validation.SCREENINGS_MAX) {
                    MovieDistributionConstants.Validation.SCREENINGS_MAX.toLong().toString()
                } else {
                    filtered
                }
            } else {
                filtered
            }
            screeningsInput = finalValue
        },
        commercialScoreInteractionSource = commercialScoreInteractionSource,
        screeningsInteractionSource = screeningsInteractionSource,
        commercialScoreIsFocused = commercialScoreIsFocused,
        screeningsIsFocused = screeningsIsFocused,
        commercialScoreValid = commercialScoreValid,
        screeningsValid = screeningsValid,
        onDismiss = onDismiss,
        onUpdate = {
            val commercialScoreValue = commercialScoreInput.toDoubleOrNull()
            val availableScreenings = screeningsInput.toDoubleOrNull()
            if (title.isNotBlank() && commercialScoreValue != null && availableScreenings != null) {
                onUpdate(title, commercialScoreValue, availableScreenings)
            }
        }
    )
}

/**
 * Stateless edit dialog content.
 */
@Composable
private fun EditDialogContent(
    title: String,
    onTitleChange: (String) -> Unit,
    commercialScoreInput: String,
    onCommercialScoreChange: (String) -> Unit,
    screeningsInput: String,
    onScreeningsChange: (String) -> Unit,
    commercialScoreInteractionSource: MutableInteractionSource,
    screeningsInteractionSource: MutableInteractionSource,
    commercialScoreIsFocused: Boolean,
    screeningsIsFocused: Boolean,
    commercialScoreValid: Boolean,
    screeningsValid: Boolean,
    onDismiss: () -> Unit,
    onUpdate: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth()
                .padding(UiConstants.Padding.contentStandard),
            shape = CustomShapes.DialogShape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp,
                pressedElevation = 8.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(UiConstants.Padding.contentStandard)
                    .animateContentSizeFast(),
                verticalArrangement = Arrangement.spacedBy(UiConstants.Spacing.betweenSections)
            ) {
                Text(
                    text = stringResource(Strings.dialogTitleEditMovie),
                    style = MaterialTheme.typography.titleLarge
                )
                
                OutlinedTextField(
                    value = title,
                    onValueChange = onTitleChange,
                    label = { Text(stringResource(Strings.movieNameLabel)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = commercialScoreInput,
                    onValueChange = onCommercialScoreChange,
                    label = { Text(stringResource(Strings.labelCommercialScore)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    interactionSource = commercialScoreInteractionSource,
                    isError = !commercialScoreIsFocused && commercialScoreInput.isNotEmpty() && !commercialScoreValid,
                    supportingText = {
                        val showError = !commercialScoreIsFocused && commercialScoreInput.isNotEmpty() && !commercialScoreValid
                        if (showError) {
                            Text(
                                stringResource(
                                    Strings.errorCommercialScoreRange,
                                    MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MIN.toString(),
                                    MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MAX.toInt().toString()
                                ),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
                
                OutlinedTextField(
                    value = screeningsInput,
                    onValueChange = onScreeningsChange,
                    label = { Text(stringResource(Strings.labelScreenings)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    interactionSource = screeningsInteractionSource,
                    isError = !screeningsIsFocused && screeningsInput.isNotEmpty() && !screeningsValid,
                    supportingText = {
                        val showError = !screeningsIsFocused && screeningsInput.isNotEmpty() && !screeningsValid
                        if (showError) {
                            Text(
                                stringResource(
                                    Strings.errorNumberOfScreeningsRange,
                                    MovieDistributionConstants.Validation.SCREENINGS_MIN.toInt().toString(),
                                    MovieDistributionConstants.Validation.SCREENINGS_MAX.toInt().formatNumberThousands()
                                ),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
                
                DialogActionButtons(
                    onDismiss = onDismiss,
                    onConfirm = onUpdate,
                    confirmEnabled = title.isNotBlank() && commercialScoreValid && screeningsValid,
                    confirmText = stringResource(Strings.update),
                    confirmIcon = Icons.Filled.Check
                )
            }
        }
    }
}
