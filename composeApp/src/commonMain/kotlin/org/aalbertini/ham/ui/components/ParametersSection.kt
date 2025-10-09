package org.aalbertini.ham.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.aalbertini.ham.CalculationConstants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParametersSection(
    p1Input: String,
    p2Input: String,
    currentMovieResultTitle: String?,
    editableTitle: String,
    originalTitle: String?,
    onTitleChange: (String) -> Unit,
    onP1Change: (String) -> Unit,
    onP2Change: (String) -> Unit,
    onSaveClick: () -> Unit,
    onNewClick: () -> Unit,
    onRevertTitle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val p1 = remember(p1Input) { p1Input.toDoubleOrNull() }
    val p2 = remember(p2Input) { p2Input.toDoubleOrNull() }
    val p1Valid = remember(p1) { 
        p1 != null && p1 >= CalculationConstants.Validation.COMMERCIAL_SCORE_MIN && p1 <= CalculationConstants.Validation.COMMERCIAL_SCORE_MAX 
    }
    val p2Valid = remember(p2) { 
        p2 != null && p2 >= CalculationConstants.Validation.SEATS_MIN && p2 <= CalculationConstants.Validation.SEATS_MAX 
    }
    val hasCurrentMovieResult = remember(currentMovieResultTitle) { currentMovieResultTitle != null }
    
    // Check if there are unsaved changes
    val hasUnsavedChanges = remember(hasCurrentMovieResult, editableTitle, originalTitle) {
        hasCurrentMovieResult && (editableTitle != originalTitle)
    }
    
    // If valid and saved with no changes, show check icon
    val isSavedWithoutChanges = remember(hasCurrentMovieResult, p1Valid, p2Valid, hasUnsavedChanges) {
        hasCurrentMovieResult && p1Valid && p2Valid && !hasUnsavedChanges
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(UiConstants.Card.padding)
            .border(
                width = UiConstants.Card.borderWidth,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(UiConstants.Card.cornerRadiusLarge)
            ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(UiConstants.Card.cornerRadiusLarge)
    ) {
        Column {
            // Sticky header with elevated surface
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primaryContainer,
                tonalElevation = UiConstants.SectionHeader.tonalElevation,
                shadowElevation = UiConstants.SectionHeader.shadowElevation
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = UiConstants.SectionHeader.horizontalPadding, vertical = UiConstants.SectionHeader.verticalPadding),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = UiStrings.SECTION_PARAMETERS,
                        style = MaterialTheme.typography.titleMedium,
                        softWrap = true,
                        maxLines = 2
                    )
                    Row {
                        if (hasCurrentMovieResult) {
                            TooltipBox(
                                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                                tooltip = { PlainTooltip { Text(UiStrings.ACTION_NEW_MOVIE) } },
                                state = rememberTooltipState()
                            ) {
                                IconButton(onClick = onNewClick) {
                                    Icon(Icons.Filled.Add, contentDescription = UiStrings.ACTION_NEW_MOVIE)
                                }
                            }
                        }
                        if (p1Valid && p2Valid) {
                            if (!isSavedWithoutChanges) {
                                // Show save button when there are changes or new calculation
                                IconButton(onClick = onSaveClick) {
                                    Icon(Icons.Filled.Save, contentDescription = UiStrings.ACTION_SAVE_MOVIE)
                                }
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.padding(UiConstants.Padding.contentStandard)
            ) {
                // Editable title with revert button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = editableTitle,
                        onValueChange = onTitleChange,
                        label = { Text(UiStrings.LABEL_MOVIE_NAME) },
                        singleLine = true,
                        placeholder = { Text(UiStrings.PLACEHOLDER_UNSAVED) },
                        modifier = Modifier.weight(1f)
                    )

                    // Show revert button if title was edited from original
                    if (originalTitle != null && editableTitle != originalTitle && editableTitle.isNotBlank()) {
                        TooltipBox(
                            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                            tooltip = { PlainTooltip { Text(UiStrings.ACTION_REVERT_TO_ORIGINAL) } },
                            state = rememberTooltipState()
                        ) {
                            IconButton(onClick = onRevertTitle) {
                                Icon(
                                    Icons.Filled.Undo,
                                    contentDescription = UiStrings.ACTION_REVERT_TO_ORIGINAL,
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(UiConstants.Spacing.betweenSections))

                // Commercial score with animated error text
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                ) {
                    OutlinedTextField(
                        value = p1Input,
                        onValueChange = onP1Change,
                        label = { Text(UiStrings.labelCommercialScore()) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        isError = p1Input.isNotEmpty() && (p1Input.toDoubleOrNull()
                            ?.let { it < CalculationConstants.Validation.COMMERCIAL_SCORE_MIN || it > CalculationConstants.Validation.COMMERCIAL_SCORE_MAX } ?: true),
                        supportingText = {
                            val showError = p1Input.isNotEmpty() && (p1Input.toDoubleOrNull()
                                ?.let { it < CalculationConstants.Validation.COMMERCIAL_SCORE_MIN || it > CalculationConstants.Validation.COMMERCIAL_SCORE_MAX } ?: true)
                            AnimatedVisibility(
                                visible = showError,
                                enter = fadeIn() + expandVertically(),
                                exit = fadeOut() + shrinkVertically()
                            ) {
                                Text(UiStrings.errorCommercialScore())
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                Spacer(modifier = Modifier.height(UiConstants.Spacing.betweenFields))

                // Number of seats with animated error text
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                ) {
                    OutlinedTextField(
                        value = p2Input,
                        onValueChange = onP2Change,
                        label = { Text(UiStrings.labelNumberOfSeats()) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        isError = p2Input.isNotEmpty() && (p2Input.toDoubleOrNull()
                            ?.let { it < CalculationConstants.Validation.SEATS_MIN || it > CalculationConstants.Validation.SEATS_MAX } ?: true),
                        supportingText = {
                            val showError = p2Input.isNotEmpty() && (p2Input.toDoubleOrNull()
                                ?.let { it < CalculationConstants.Validation.SEATS_MIN || it > CalculationConstants.Validation.SEATS_MAX } ?: true)
                            AnimatedVisibility(
                                visible = showError,
                                enter = fadeIn() + expandVertically(),
                                exit = fadeOut() + shrinkVertically()
                            ) {
                                Text(UiStrings.errorNumberOfSeats())
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
