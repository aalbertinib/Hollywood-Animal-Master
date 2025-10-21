package org.aalbertini.ham.features.movie_distribution.presentation.components.section

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import org.aalbertini.ham.core.ui.components.ErrorMessageAnimatedVisibility
import org.aalbertini.ham.core.ui.components.TextIcon
import org.aalbertini.ham.core.ui.components.section.ModernSectionWithHeader
import org.aalbertini.ham.core.ui.resources.Strings
import org.aalbertini.ham.core.ui.resources.UiConstants
import org.aalbertini.ham.core.ui.resources.UiIcons
import org.aalbertini.ham.features.movie_distribution.domain.calculator.MovieDistributionConstants
import org.aalbertini.ham.features.movie_distribution.domain.formatting.formatNumberThousands
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParametersSection(
    commercialScoreInput: String,
    availableScreeningsInput: String,
    currentMovieResultTitle: String?,
    editableTitle: String,
    originalTitle: String?,
    originalCommercialScore: String?,
    originalAvailableScreenings: String?,
    hasCurrentMovieResult: Boolean,
    commercialScoreValid: Boolean,
    availableScreeningsValid: Boolean,
    isSavedWithoutChanges: Boolean,
    showSaveButton: Boolean,
    showNewButton: Boolean,
    onTitleChange: (String) -> Unit,
    onCommercialScoreChange: (String) -> Unit,
    onAvailableScreeningsChange: (String) -> Unit,
    onRevertTitle: () -> Unit,
    onRevertCommercialScore: () -> Unit,
    onRevertAvailableScreenings: () -> Unit,
    onSaveClick: () -> Unit,
    onNewClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val commercialScore = remember(commercialScoreInput) { commercialScoreInput.toDoubleOrNull() }
    val availableScreenings =
        remember(availableScreeningsInput) { availableScreeningsInput.toDoubleOrNull() }
    val hasCurrentMovieResult =
        remember(currentMovieResultTitle) { currentMovieResultTitle != null }

    // Focus management for input fields
    val focusManager = LocalFocusManager.current
    val commercialScoreFocusRequester = remember { FocusRequester() }
    val availableScreeningsFocusRequester = remember { FocusRequester() }

    // Track if fields were ever focused to determine if we should show empty on invalid
    var commercialScoreEverFocused by remember { mutableStateOf(false) }
    var availableScreeningsEverFocused by remember { mutableStateOf(false) }

    // Track focus state for each field
    val commercialScoreInteractionSource = remember { MutableInteractionSource() }
    val availableScreeningsInteractionSource = remember { MutableInteractionSource() }
    val commercialScoreIsFocused by commercialScoreInteractionSource.collectIsFocusedAsState()
    val availableScreeningsIsFocused by availableScreeningsInteractionSource.collectIsFocusedAsState()

    // Display value logic: show empty if focused and invalid, or if value is 0.0
    val displayCommercialScore =
        remember(commercialScoreInput, commercialScoreIsFocused, commercialScoreEverFocused) {
            val numValue = commercialScoreInput.toDoubleOrNull()
            when {
                commercialScoreIsFocused && commercialScoreEverFocused && (numValue == null || numValue == 0.0) -> ""
                numValue == 0.0 && commercialScoreInput.isNotEmpty() -> ""
                else -> commercialScoreInput
            }
        }

    val displayAvailableScreenings = remember(
        availableScreeningsInput,
        availableScreeningsIsFocused,
        availableScreeningsEverFocused
    ) {
        val numValue = availableScreeningsInput.toDoubleOrNull()
        when {
            availableScreeningsIsFocused && availableScreeningsEverFocused && (numValue == null || numValue == 0.0) -> ""
            numValue == 0.0 && availableScreeningsInput.isNotEmpty() -> ""
            !availableScreeningsIsFocused && numValue != null && numValue > 0.0 -> {
                // Format with thousand separators when not focused
                numValue.toLong().formatNumberThousands()
            }

            else -> availableScreeningsInput
        }
    }

    // Clear field when focused if invalid or 0
    LaunchedEffect(commercialScoreIsFocused) {
        if (commercialScoreIsFocused) {
            commercialScoreEverFocused = true
            val numValue = commercialScoreInput.toDoubleOrNull()
            if (numValue == null || numValue == 0.0 ||
                numValue < MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MIN ||
                numValue > MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MAX
            ) {
                onCommercialScoreChange("")
            }
        }
    }

    LaunchedEffect(availableScreeningsIsFocused) {
        if (availableScreeningsIsFocused) {
            availableScreeningsEverFocused = true
            // When gaining focus, remove formatting (convert "3,200" to "3200")
            val numValue = availableScreeningsInput.toDoubleOrNull()
            if (numValue == null || numValue == 0.0 ||
                numValue < MovieDistributionConstants.Validation.SCREENINGS_MIN ||
                numValue > MovieDistributionConstants.Validation.SCREENINGS_MAX
            ) {
                onAvailableScreeningsChange("")
            } else {
                // Remove formatting when focused (ensure it's just the raw number)
                val rawValue = numValue.toLong().toString()
                if (availableScreeningsInput != rawValue) {
                    onAvailableScreeningsChange(rawValue)
                }
            }
        } else if (!availableScreeningsIsFocused && availableScreeningsEverFocused) {
            // Normalize the value when focus is lost (already integer, just ensure consistency)
            val numValue = availableScreeningsInput.toDoubleOrNull()
            if (numValue != null && numValue > 0.0) {
                val normalizedValue = numValue.toLong().toString()
                if (availableScreeningsInput != normalizedValue) {
                    onAvailableScreeningsChange(normalizedValue)
                }
            }
        }
    }

    ModernSectionWithHeader(
        title = stringResource(Strings.parametersSectionTitle),
        subtitle = if (hasCurrentMovieResult) {
            if (isSavedWithoutChanges) "Saved"
            else "Unsaved changes"
        } else null,
        modifier = modifier,
        headerActions = {
            // Success icon (when saved and up-to-date)
            if (isSavedWithoutChanges) {
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = { PlainTooltip { Text("All changes saved") } },
                    state = rememberTooltipState()
                ) {
                    FilledTonalIconButton(
                        onClick = { /* No action - just visual indicator */ },
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            Icons.Filled.CheckCircle,
                            contentDescription = "Saved",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            
            // Save button (when there are unsaved changes or creating new movie)
            if (showSaveButton) {
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = { PlainTooltip { Text(stringResource(Strings.actionSaveMovie)) } },
                    state = rememberTooltipState()
                ) {
                    FilledTonalIconButton(
                        onClick = onSaveClick,
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Icon(
                            Icons.Filled.Save,
                            contentDescription = stringResource(Strings.actionSaveMovie)
                        )
                    }
                }
            }
            
            // New button
            if (showNewButton) {
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = { PlainTooltip { Text(stringResource(Strings.actionNewMovie)) } },
                    state = rememberTooltipState()
                ) {
                    FilledTonalIconButton(
                        onClick = onNewClick,
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    ) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = stringResource(Strings.actionNewMovie)
                        )
                    }
                }
            }
        }
    ) {
        // Editable title with revert button inside field
        OutlinedTextField(
            value = editableTitle,
            onValueChange = { newValue ->
                // Filter out newlines and carriage returns
                val filteredValue = newValue.replace("\n", "").replace("\r", "")
                onTitleChange(filteredValue)
            },
            label = {
                TextIcon(
                    text = stringResource(Strings.movieNameLabel),
                    icon = UiIcons.LABEL_MOVIE_NAME
                )
            },
            singleLine = true,
            placeholder = { Text(stringResource(Strings.unsavedText)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    // Clear focus when Done/Return is pressed
                    focusManager.clearFocus()
                }
            ),
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = if (originalTitle != null && editableTitle != originalTitle && editableTitle.isNotBlank()) {
                {
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                        tooltip = { PlainTooltip { Text(stringResource(Strings.actionRevertToOriginal)) } },
                        state = rememberTooltipState()
                    ) {
                        IconButton(onClick = onRevertTitle) {
                            Icon(
                                Icons.AutoMirrored.Filled.Undo,
                                contentDescription = stringResource(Strings.actionRevertToOriginal),
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            } else null
        )

        Spacer(modifier = Modifier.height(UiConstants.Spacing.betweenSections))

        // Commercial score with animated error text and revert button inside field
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = displayCommercialScore,
                onValueChange = onCommercialScoreChange,
                label = {
                    TextIcon(
                        text = stringResource(Strings.labelCommercialScore),
                        icon = UiIcons.LABEL_COMMERCIAL_SCORE
                    )
                },
                singleLine = false,
                maxLines = 2,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = !commercialScoreIsFocused && commercialScoreInput.isNotEmpty() && (commercialScoreInput.toDoubleOrNull()
                    ?.let { it < MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MIN || it > MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MAX }
                    ?: true),
                supportingText = {
                    val showError =
                        !commercialScoreIsFocused && commercialScoreInput.isNotEmpty() && (commercialScoreInput.toDoubleOrNull()
                            ?.let { it < MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MIN || it > MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MAX }
                            ?: true)
                    ErrorMessageAnimatedVisibility(visible = showError) {
                        Text(
                            stringResource(
                                Strings.errorCommercialScoreRange,
                                MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MIN.toString(),
                                MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MAX.toInt()
                                    .toString()
                            )
                        )
                    }
                },
                interactionSource = commercialScoreInteractionSource,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(commercialScoreFocusRequester),
                trailingIcon = if (originalCommercialScore != null && commercialScoreInput != originalCommercialScore && commercialScoreInput.isNotBlank()) {
                    {
                        TooltipBox(
                            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                            tooltip = { PlainTooltip { Text(stringResource(Strings.actionRevertToOriginal)) } },
                            state = rememberTooltipState()
                        ) {
                            IconButton(onClick = onRevertCommercialScore) {
                                Icon(
                                    Icons.AutoMirrored.Filled.Undo,
                                    contentDescription = stringResource(Strings.actionRevertToOriginal),
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }
                } else null
            )
        }

        Spacer(modifier = Modifier.height(UiConstants.Spacing.betweenFields))

        // Number of screenings with animated error text and revert button inside field
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = displayAvailableScreenings,
                onValueChange = onAvailableScreeningsChange,
                label = {
                    TextIcon(
                        text = stringResource(Strings.labelScreenings),
                        icon = UiIcons.LABEL_SCREENINGS
                    )
                },
                singleLine = false,
                maxLines = 2,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = !availableScreeningsIsFocused && availableScreeningsInput.isNotEmpty() && (availableScreeningsInput.toDoubleOrNull()
                    ?.let { it < MovieDistributionConstants.Validation.SCREENINGS_MIN || it > MovieDistributionConstants.Validation.SCREENINGS_MAX }
                    ?: true),
                supportingText = {
                    val showError =
                        !availableScreeningsIsFocused && availableScreeningsInput.isNotEmpty() && (availableScreeningsInput.toDoubleOrNull()
                            ?.let { it < MovieDistributionConstants.Validation.SCREENINGS_MIN || it > MovieDistributionConstants.Validation.SCREENINGS_MAX }
                            ?: true)
                    ErrorMessageAnimatedVisibility(visible = showError) {
                        Text(
                            stringResource(
                                Strings.errorNumberOfScreeningsRange,
                                MovieDistributionConstants.Validation.SCREENINGS_MIN.toInt()
                                    .toString(),
                                MovieDistributionConstants.Validation.SCREENINGS_MAX.toInt()
                                    .formatNumberThousands()
                            )
                        )
                    }
                },
                interactionSource = availableScreeningsInteractionSource,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(availableScreeningsFocusRequester),
                trailingIcon = if (originalAvailableScreenings != null && availableScreeningsInput != originalAvailableScreenings && availableScreeningsInput.isNotBlank()) {
                    {
                        TooltipBox(
                            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                            tooltip = { PlainTooltip { Text(stringResource(Strings.actionRevertToOriginal)) } },
                            state = rememberTooltipState()
                        ) {
                            IconButton(onClick = onRevertAvailableScreenings) {
                                Icon(
                                    Icons.AutoMirrored.Filled.Undo,
                                    contentDescription = stringResource(Strings.actionRevertToOriginal),
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }
                } else null
            )
        }
    }
}
