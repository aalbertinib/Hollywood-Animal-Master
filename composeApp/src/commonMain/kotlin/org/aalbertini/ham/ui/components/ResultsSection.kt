@file:OptIn(ExperimentalMaterial3Api::class)

package org.aalbertini.ham.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.aalbertini.ham.MovieDistributionConstants
import org.aalbertini.ham.ui.theme.CustomShapes
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsSection(
    results: List<Long>,
    expanded: Boolean,
    availableScreeningsValue: Double,
    availableScreeningsOverrideInputs: Map<Int, String>,
    currentMovieResultId: String?,
    onAvailableScreeningsOverrideChange: (weekIndex: Int, value: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSizeFast(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = CustomShapes.SectionContentShape
    ) {
        SectionAnimatedVisibility(visible = expanded) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
                shape = CustomShapes.SectionContentShape
            ) {
                AnimatedContent(
                    targetState = results,
                    label = "results",
                    transitionSpec = { fadeIn() togetherWith fadeOut() }
                ) { list ->
                    Column(modifier = Modifier.padding(
                        horizontal = UiConstants.SectionHeader.horizontalPadding,
                        vertical = UiConstants.SectionHeader.verticalPadding
                    )) {
                        if (list.isEmpty()) {
                            Text(
                                text = UiStrings.messageEnterValidParameters(),
                                style = MaterialTheme.typography.bodyMedium,
                                softWrap = true
                            )
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(UiConstants.Spacing.betweenElements)) {
                                for (i in 1..MovieDistributionConstants.WeeklyCalculation.NUMBER_OF_WEEKS) {
                                    val weekIndex = i - 1
                                    
                                    // Key each week row to isolate recomposition
                                    key(weekIndex) {
                                        WeekResultRow(
                                            weekNumber = i,
                                            weekIndex = weekIndex,
                                            resultValue = list[weekIndex],
                                            availableScreeningsValue = availableScreeningsValue,
                                            availableScreeningsOverrideInputs = availableScreeningsOverrideInputs,
                                            currentMovieResultId = currentMovieResultId,
                                            onAvailableScreeningsOverrideChange = onAvailableScreeningsOverrideChange,
                                            focusManager = focusManager
                                        )
                                    }
                                    
                                    // Add dividers between weeks
                                    if (i < MovieDistributionConstants.WeeklyCalculation.NUMBER_OF_WEEKS) {
                                        // Every 4 weeks, add a distinctive group divider
                                        if (i % 4 == 0) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = UiConstants.Spacing.betweenElements)
                                            ) {
                                                HorizontalDivider(
                                                    thickness = 3.dp,
                                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WeekResultRow(
    weekNumber: Int,
    weekIndex: Int,
    resultValue: Long,
    availableScreeningsValue: Double,
    availableScreeningsOverrideInputs: Map<Int, String>,
    currentMovieResultId: String?,
    onAvailableScreeningsOverrideChange: (Int, String) -> Unit,
    focusManager: androidx.compose.ui.focus.FocusManager
) {
    // Use local state to prevent focus loss during typing
    var localOverrideInput by remember(weekIndex) {
        mutableStateOf(availableScreeningsOverrideInputs[weekIndex] ?: "")
    }

    // Track if field is focused
    var isFocused by remember(weekIndex) { mutableStateOf(false) }
    
    // Track if current input is validated (synced to ViewModel)
    // derivedStateOf automatically tracks dependencies (localOverrideInput and availableScreeningsOverrideInputs)
    val isValidated by derivedStateOf {
        val vmValue = availableScreeningsOverrideInputs[weekIndex] ?: ""
        // Compare numeric values to handle different string representations
        if (localOverrideInput.isEmpty() && vmValue.isEmpty()) {
            true
        } else {
            val localNum = localOverrideInput.toDoubleOrNull()
            val vmNum = vmValue.toDoubleOrNull()
            localNum != null && vmNum != null && abs(localNum - vmNum) < 0.0001
        }
    }

    // Check if input is valid (memoized)
    val isInputValid by derivedStateOf {
        if (localOverrideInput.isEmpty()) {
            true
        } else {
            val numValue = localOverrideInput.toDoubleOrNull()
            if (numValue == null) {
                false
            } else {
                // Use epsilon for floating point comparison to handle edge cases
                numValue >= 0.0 && numValue <= availableScreeningsValue + 0.0001
            }
        }
    }
    
    // Sync external changes only when not focused (like loading a saved movie)
    // Force-clear pending input when a different movie is loaded
    // Using LaunchedEffect to avoid side effects during composition
    LaunchedEffect(isFocused, availableScreeningsOverrideInputs, weekIndex, currentMovieResultId) {
        if (!isFocused) {
            val externalInput = availableScreeningsOverrideInputs[weekIndex] ?: ""
            if (externalInput != localOverrideInput) {
                localOverrideInput = externalInput
                focusManager.clearFocus()
            }
        } else {
            // Force-clear pending input when movie changes (even if focused)
            val externalInput = availableScreeningsOverrideInputs[weekIndex] ?: ""
            if (externalInput != localOverrideInput) {
                localOverrideInput = externalInput
                // Clear focus to complete the transition
                focusManager.clearFocus()
            }
        }
    }

    // Check if there's an override (memoized)
    val hasOverride by derivedStateOf {
        localOverrideInput.isNotEmpty()
    }

    // Display formatted value when not focused (memoized)
    val displayValue by derivedStateOf {
        if (!isFocused && hasOverride) {
            // Format the value when not focused
            localOverrideInput.toDoubleOrNull()?.toLong()?.let {
                UiStrings.formatNumber(it)
            } ?: localOverrideInput
        } else {
            // Show raw input while typing
            localOverrideInput
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Week result row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                modifier = Modifier.weight(0.2f),
                shape = RoundedCornerShape(6.dp),
            ) {
                TextIcon(
                    text = UiStrings.weekNumber(weekNumber),
                    icon = UiStrings.WEEK_NUMBER_ICON,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                )
            }
            // Isolated result value - only recomposes when resultValue changes
            key(resultValue) {
                Surface(
                    modifier = Modifier.wrapContentWidth(),
                    shape = RoundedCornerShape(6.dp),
                    color = if (!isValidated && hasOverride) {
                        MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.7f)
                    } else {
                        MaterialTheme.colorScheme.primaryContainer
                    },
                    tonalElevation = 1.dp,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    ) {
                        TextIcon(
                            text = UiStrings.MOVIE_RESULTS_SCREENINGS,
                            icon = UiStrings.MOVIE_RESULTS_SCREENINGS_ICON,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .weight(1f, fill = false)
                                .padding(horizontal = 2.dp, vertical = 6.dp)
                        )
                        Text(
                            text = UiStrings.formatNumber(resultValue),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = if (!isValidated && hasOverride) {
                                MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.5f)
                            } else {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            },
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .wrapContentWidth()
                                .padding(horizontal = 2.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Available screenings override field with improved layout
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = if (hasOverride) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
            tonalElevation = 1.dp,
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = UiStrings.MOVIE_RESULTS_YOUR_SCREENINGS_OVERRIDE,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(0.25f)
                )
                OutlinedTextField(
                    value = displayValue,
                    onValueChange = { newValue ->
                        // Remove formatting characters if present (user might paste formatted text)
                        val cleanValue = newValue.replace(",", "").replace(" ", "")

                        // Filter to only allow numeric input with decimal point
                        if (cleanValue.isEmpty()) {
                            localOverrideInput = cleanValue
                        } else if (cleanValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                            // Validate value is within range [0, availableScreeningsValue]
                            val numValue = cleanValue.toDoubleOrNull()
                            if (numValue != null) {
                                // Only accept if within valid range (with epsilon for floating point)
                                if (numValue >= 0.0 && numValue <= availableScreeningsValue + 0.0001) {
                                    localOverrideInput = cleanValue
                                }
                                // If out of range, reject the input (don't update state)
                            } else {
                                // Partial input (e.g., "12."), allow it for typing
                                localOverrideInput = cleanValue
                            }
                        }
                        // NO immediate sync - only on Return or focus loss
                    },
                    modifier = Modifier
                        .weight(0.75f)
                        .onFocusChanged { focusState ->
                            val wasFocused = isFocused
                            isFocused = focusState.isFocused

                            // Sync to ViewModel when focus is lost, only if valid AND not already validated
                            // This prevents re-saving an already validated value when clicking away
                            if (wasFocused && !focusState.isFocused && isInputValid && !isValidated) {
                                // Normalize before syncing (remove trailing zeros/decimal for whole numbers)
                                val numValue = localOverrideInput.toDoubleOrNull()
                                val normalizedInput = if (numValue != null && numValue == numValue.toLong().toDouble()) {
                                    numValue.toLong().toString()
                                } else {
                                    localOverrideInput
                                }
                                localOverrideInput = normalizedInput
                                onAvailableScreeningsOverrideChange(weekIndex, normalizedInput)
                            }
                        },
                    label = if (hasOverride && !isValidated) {
                        { Text("Pending validation", style = MaterialTheme.typography.labelSmall) }
                    } else if (hasOverride && isValidated) {
                        { Text("Override", style = MaterialTheme.typography.labelSmall) }
                    } else {
                        null
                    },
                    placeholder = {
                        Text(
                            text = "Default: ${UiStrings.formatNumber(availableScreeningsValue.toLong())}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                        )
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
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
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            // Validate and sync when Return/Done is pressed (explicit user action)
                            // Allow re-validation even if already validated (user explicitly pressed Enter)
                            if (isInputValid) {
                                // Normalize before syncing (remove trailing zeros/decimal for whole numbers)
                                val numValue = localOverrideInput.toDoubleOrNull()
                                val normalizedInput = if (numValue != null && numValue == numValue.toLong().toDouble()) {
                                    numValue.toLong().toString()
                                } else {
                                    localOverrideInput
                                }
                                localOverrideInput = normalizedInput
                                onAvailableScreeningsOverrideChange(weekIndex, normalizedInput)
                                // Dismiss keyboard on successful validation
                                focusManager.clearFocus()
                            }
                            // Invalid - keep focus for correction (no sync)
                        }
                    ),
                    singleLine = true,
                    trailingIcon = if (hasOverride) {
                        {
                            // Clear button - shown when there's any override
                            TooltipBox(
                                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                                tooltip = { PlainTooltip { Text("Clear override") } },
                                state = rememberTooltipState()
                            ) {
                                IconButton(
                                    onClick = {
                                        // Clear both local and ViewModel state
                                        localOverrideInput = ""
                                        onAvailableScreeningsOverrideChange(weekIndex, "")
                                        focusManager.clearFocus()
                                    }
                                ) {
                                    Icon(
                                        Icons.Filled.Clear,
                                        contentDescription = "Clear override",
                                        tint = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }
                        }
                    } else null,
                    isError = localOverrideInput.isNotEmpty() &&
                            (localOverrideInput.toDoubleOrNull()?.let {
                                // Validate: must be between 0 and availableScreeningsInput (with epsilon)
                                it < 0.0 || it > availableScreeningsValue + 0.0001
                            } ?: true),
                    supportingText = if (localOverrideInput.isNotEmpty() &&
                        (localOverrideInput.toDoubleOrNull()?.let {
                            it < 0.0 || it > availableScreeningsValue + 0.0001
                        } ?: true)
                    ) {
                        {
                            Text(
                                text = if (availableScreeningsValue > 0.0) {
                                    "Must be between 0 and ${
                                        UiStrings.formatNumber(
                                            availableScreeningsValue.toLong()
                                        )
                                    }"
                                } else {
                                    "Enter available screenings first"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    } else null
                )
            }
        }
    }
}
