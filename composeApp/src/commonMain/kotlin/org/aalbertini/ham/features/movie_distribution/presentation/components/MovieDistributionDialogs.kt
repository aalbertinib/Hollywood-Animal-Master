package org.aalbertini.ham.features.movie_distribution.presentation.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.aalbertini.ham.features.movie_distribution.domain.calculator.MovieDistributionConstants
import org.aalbertini.ham.features.movie_distribution.domain.model.MovieResult
import org.aalbertini.ham.core.util.input.filterIntegerInput
import org.aalbertini.ham.core.util.input.filterNumericInput
import org.aalbertini.ham.core.ui.resources.UiStrings
import org.aalbertini.ham.core.ui.resources.UiConstants
import org.aalbertini.ham.core.ui.components.TextIcon
import org.aalbertini.ham.core.ui.components.AppEmojiIcons
import org.aalbertini.ham.core.ui.components.animateContentSizeFast

@Composable
fun SaveDialog(
    initialTitle: String,
    existingTitles: List<String> = emptyList(),
    currentTitle: String? = null,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
    isUpdate: Boolean = false
) {
    var title by remember(initialTitle) { mutableStateOf(initialTitle) }
    var showOverwriteConfirmation by remember { mutableStateOf(false) }

    val titleExists = remember(title, existingTitles, currentTitle) {
        title.isNotBlank() && existingTitles.contains(title) && title != currentTitle
    }

    if (showOverwriteConfirmation) {
        AlertDialog(
            onDismissRequest = { showOverwriteConfirmation = false },
            icon = {
                Icon(
                    Icons.Filled.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(32.dp)
                )
            },
            title = { Text(UiStrings.DIALOG_TITLE_OVERWRITE_MOVIE) },
            text = {
                Text(
                    text = UiStrings.messageOverwriteMovie(title),
                    softWrap = true
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showOverwriteConfirmation = false
                        onSave(title)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(UiStrings.BUTTON_OVERWRITE)
                }
            },
            dismissButton = {
                TextButton(onClick = { showOverwriteConfirmation = false }) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(UiStrings.BUTTON_CANCEL)
                }
            }
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth()
                .padding(UiConstants.Padding.contentStandard),
            elevation = CardDefaults.cardElevation(defaultElevation = UiConstants.Card.elevationDialog)
        ) {
            Column(
                modifier = Modifier.padding(UiConstants.Padding.contentStandard),
                verticalArrangement = Arrangement.spacedBy(UiConstants.Spacing.betweenSections)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        Icons.Filled.Save,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = if (isUpdate) UiStrings.DIALOG_TITLE_UPDATE_MOVIE else UiStrings.DIALOG_TITLE_SAVE_MOVIE,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(UiStrings.LABEL_TITLE) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    isError = titleExists,
                    supportingText = if (titleExists) {
                        {
                            Text(
                                UiStrings.ERROR_TITLE_EXISTS,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    } else null
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(UiStrings.BUTTON_CANCEL)
                    }
                    Button(
                        onClick = {
                            if (titleExists) {
                                showOverwriteConfirmation = true
                            } else {
                                onSave(title)
                            }
                        },
                        enabled = title.isNotBlank()
                    ) {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(if (isUpdate) UiStrings.BUTTON_UPDATE else UiStrings.BUTTON_SAVE)
                    }
                }
            }
        }
    }
}

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
    
    // Focus management
    val commercialScoreInteractionSource = remember { MutableInteractionSource() }
    val screeningsInteractionSource = remember { MutableInteractionSource() }
    val commercialScoreIsFocused by commercialScoreInteractionSource.collectIsFocusedAsState()
    val screeningsIsFocused by screeningsInteractionSource.collectIsFocusedAsState()
    
    // Display values with formatting
    val displayScreenings = remember(screeningsInput, screeningsIsFocused) {
        if (!screeningsIsFocused && screeningsInput.isNotEmpty()) {
            screeningsInput.toDoubleOrNull()?.toLong()?.let {
                UiStrings.formatNumber(it)
            } ?: screeningsInput
        } else {
            screeningsInput
        }
    }
    
    // Remove formatting when gaining focus for screenings
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
            // Normalize when focus is lost
            val numValue = screeningsInput.toDoubleOrNull()
            if (numValue != null && numValue > 0.0) {
                val normalizedValue = numValue.toLong().toString()
                if (screeningsInput != normalizedValue) {
                    screeningsInput = normalizedValue
                }
            }
        }
    }
    
    // Validation
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

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth()
                .padding(UiConstants.Padding.contentStandard),
            elevation = CardDefaults.cardElevation(defaultElevation = UiConstants.Card.elevationDialog)
        ) {
            Column(
                modifier = Modifier
                    .padding(UiConstants.Padding.contentStandard)
                    .animateContentSizeFast(),
                verticalArrangement = Arrangement.spacedBy(UiConstants.Spacing.betweenSections)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = UiStrings.DIALOG_TITLE_EDIT_MOVIE,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(UiStrings.LABEL_MOVIE_NAME) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Commercial Score with validation
                OutlinedTextField(
                    value = commercialScoreInput,
                    onValueChange = { newValue ->
                        // Limit to 1 decimal place for commercial score
                        val filtered = newValue.filterNumericInput(maxDecimalPlaces = 1)
                        
                        // Apply range restriction if value is complete (not just typing)
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
                    label = { Text(UiStrings.LABEL_COMMERCIAL_SCORE) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    interactionSource = commercialScoreInteractionSource,
                    isError = !commercialScoreIsFocused && commercialScoreInput.isNotEmpty() && !commercialScoreValid,
                    supportingText = {
                        val showError = !commercialScoreIsFocused && commercialScoreInput.isNotEmpty() && !commercialScoreValid
                        if (showError) {
                            Text(
                                UiStrings.errorCommercialScore(),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
                
                // Screenings with validation and formatting
                OutlinedTextField(
                    value = displayScreenings,
                    onValueChange = { newValue ->
                        val filtered = newValue.filterIntegerInput()
                        
                        // Apply range restriction if value is complete
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
                    label = { Text(UiStrings.LABEL_SCREENINGS) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    interactionSource = screeningsInteractionSource,
                    isError = !screeningsIsFocused && screeningsInput.isNotEmpty() && !screeningsValid,
                    supportingText = {
                        val showError = !screeningsIsFocused && screeningsInput.isNotEmpty() && !screeningsValid
                        if (showError) {
                            Text(
                                UiStrings.errorNumberOfScreenings(),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(UiStrings.BUTTON_CANCEL)
                    }
                    Button(
                        onClick = {
                            val commercialScoreValue = commercialScoreInput.toDoubleOrNull()
                            val availableScreenings = screeningsInput.toDoubleOrNull()
                            if (title.isNotBlank() && commercialScoreValue != null && availableScreenings != null) {
                                onUpdate(title, commercialScoreValue, availableScreenings)
                            }
                        },
                        enabled = title.isNotBlank() && commercialScoreValid && screeningsValid
                    ) {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(UiStrings.BUTTON_UPDATE)
                    }
                }
            }
        }
    }
}

@Composable
fun ClearAllConfirmationDialog(
    movieResultCount: Int,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                Icons.Filled.DeleteSweep,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(32.dp)
            )
        },
        title = {
            Text(
                text = UiStrings.DIALOG_TITLE_CLEAR_ALL,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(UiConstants.Spacing.betweenElements)) {
                Text(
                    text = UiStrings.messageClearAll(movieResultCount),
                    style = MaterialTheme.typography.bodyMedium,
                    softWrap = true
                )
                Text(
                    text = UiStrings.WARNING_CANNOT_UNDO,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    softWrap = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    Icons.Filled.DeleteSweep,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(UiStrings.BUTTON_CLEAR_ALL)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(UiStrings.BUTTON_CANCEL)
            }
        }
    )
}

@Composable
fun ParameterComparisonDialog(
    movieTitle: String,
    existingCommercialScore: Double,
    existingScreenings: Double,
    newCommercialScore: Double,
    newScreenings: Double,
    onDismiss: () -> Unit,
    onKeepExisting: () -> Unit,
    onOverwrite: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                Icons.Filled.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(32.dp)
            )
        },
        title = {
            TextIcon(
                text = "Parameter Conflict",
                icon = AppEmojiIcons.WARNING,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "A movie with the title \"$movieTitle\" already exists with different parameters.",
                    style = MaterialTheme.typography.bodyMedium,
                    softWrap = true
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Existing parameters
                TextIcon(
                    text = "Saved Parameters:",
                    icon = AppEmojiIcons.FLOPPY_DISK,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                TextIcon(
                    text = "Commercial Score: $existingCommercialScore",
                    icon = AppEmojiIcons.STAR,
                    style = MaterialTheme.typography.bodyMedium
                )
                TextIcon(
                    text = "Number of Screenings: ${UiStrings.formatNumber(existingScreenings.toLong())}",
                    icon = AppEmojiIcons.FILM_FRAMES,
                    style = MaterialTheme.typography.bodyMedium
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                // New parameters
                TextIcon(
                    text = "Current Parameters:",
                    icon = AppEmojiIcons.PENCIL,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                TextIcon(
                    text = "Commercial Score: $newCommercialScore",
                    icon = AppEmojiIcons.STAR,
                    style = MaterialTheme.typography.bodyMedium
                )
                TextIcon(
                    text = "Number of Screenings: ${UiStrings.formatNumber(newScreenings.toLong())}",
                    icon = AppEmojiIcons.FILM_FRAMES,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Choose whether to keep the saved parameters or overwrite with current values.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    softWrap = true
                )
            }
        },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(onClick = onKeepExisting) {
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Keep Saved")
                }
                Button(onClick = onOverwrite) {
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Overwrite")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(UiStrings.BUTTON_CANCEL)
            }
        }
    )
}
