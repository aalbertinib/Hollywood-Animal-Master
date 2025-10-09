package org.aalbertini.ham.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import org.aalbertini.ham.model.MovieResult
import org.aalbertini.ham.util.filterNumericInput

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
                    }
                ) {
                    Text(UiStrings.BUTTON_OVERWRITE)
                }
            },
            dismissButton = {
                TextButton(onClick = { showOverwriteConfirmation = false }) {
                    Text(UiStrings.BUTTON_CANCEL)
                }
            }
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.Companion.fillMaxWidth()
                .padding(UiConstants.Padding.contentStandard),
            elevation = CardDefaults.cardElevation(defaultElevation = UiConstants.Card.elevationDialog)
        ) {
            Column(
                modifier = Modifier.Companion.padding(UiConstants.Padding.contentStandard),
                verticalArrangement = Arrangement.spacedBy(UiConstants.Spacing.betweenSections)
            ) {
                Text(
                    text = if (isUpdate) UiStrings.DIALOG_TITLE_UPDATE_MOVIE else UiStrings.DIALOG_TITLE_SAVE_MOVIE,
                    style = MaterialTheme.typography.titleLarge
                )
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(UiStrings.LABEL_TITLE) },
                    singleLine = true,
                    modifier = Modifier.Companion.fillMaxWidth(),
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
                    modifier = Modifier.Companion.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Companion.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
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
    var p1Input by remember(movieResult.commercialScore) { mutableStateOf(movieResult.commercialScore.toString()) }
    var p2Input by remember(movieResult.numberOfSeats) { mutableStateOf(movieResult.numberOfSeats.toString()) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.Companion.fillMaxWidth()
                .padding(UiConstants.Padding.contentStandard),
            elevation = CardDefaults.cardElevation(defaultElevation = UiConstants.Card.elevationDialog)
        ) {
            Column(
                modifier = Modifier.Companion.padding(UiConstants.Padding.contentStandard),
                verticalArrangement = Arrangement.spacedBy(UiConstants.Spacing.betweenSections)
            ) {
                Text(
                    text = UiStrings.DIALOG_TITLE_EDIT_MOVIE,
                    style = MaterialTheme.typography.titleLarge
                )
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(UiStrings.LABEL_TITLE) },
                    singleLine = true,
                    modifier = Modifier.Companion.fillMaxWidth()
                )
                OutlinedTextField(
                    value = p1Input,
                    onValueChange = { p1Input = it.filterNumericInput() },
                    label = { Text(UiStrings.LABEL_COMMERCIAL_SCORE_SHORT) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Companion.Decimal),
                    modifier = Modifier.Companion.fillMaxWidth()
                )
                OutlinedTextField(
                    value = p2Input,
                    onValueChange = { p2Input = it.filterNumericInput() },
                    label = { Text(UiStrings.LABEL_SEATS_SHORT) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Companion.Decimal),
                    modifier = Modifier.Companion.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.Companion.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Companion.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(UiStrings.BUTTON_CANCEL)
                    }
                    Button(
                        onClick = {
                            val p1 = p1Input.toDoubleOrNull()
                            val p2 = p2Input.toDoubleOrNull()
                            if (title.isNotBlank() && p1 != null && p2 != null) {
                                onUpdate(title, p1, p2)
                            }
                        },
                        enabled = title.isNotBlank() &&
                                p1Input.toDoubleOrNull() != null &&
                                p2Input.toDoubleOrNull() != null
                    ) {
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
                Text(UiStrings.BUTTON_CLEAR_ALL)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(UiStrings.BUTTON_CANCEL)
            }
        }
    )
}

@Composable
fun ParameterComparisonDialog(
    movieTitle: String,
    existingCommercialScore: Double,
    existingSeats: Double,
    newCommercialScore: Double,
    newSeats: Double,
    onDismiss: () -> Unit,
    onKeepExisting: () -> Unit,
    onOverwrite: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Parameter Conflict",
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

                Spacer(modifier = Modifier.Companion.height(4.dp))

                // Existing parameters
                Text(
                    text = "Saved Parameters:",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Companion.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Commercial Score: $existingCommercialScore",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Number of Seats: $existingSeats",
                    style = MaterialTheme.typography.bodyMedium
                )

                HorizontalDivider(modifier = Modifier.Companion.padding(vertical = 4.dp))

                // New parameters
                Text(
                    text = "Current Parameters:",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Companion.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "Commercial Score: $newCommercialScore",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Number of Seats: $newSeats",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.Companion.height(4.dp))

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
                    Text("Keep Saved")
                }
                Button(onClick = onOverwrite) {
                    Text("Overwrite")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(UiStrings.BUTTON_CANCEL)
            }
        }
    )
}