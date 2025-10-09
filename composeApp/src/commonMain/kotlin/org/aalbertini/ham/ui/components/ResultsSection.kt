package org.aalbertini.ham.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.aalbertini.ham.MovieDistributionConstants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsSection(
    results: List<Long>,
    expanded: Boolean,
    availableSeatsValue: Double,
    availableSeatsOverrideInputs: Map<Int, String>,
    onToggleExpand: () -> Unit,
    onCopyClick: () -> Unit,
    onAvailableSeatsOverrideChange: (weekIndex: Int, value: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(UiConstants.Card.padding)
            .border(
                width = UiConstants.Card.borderWidth,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(UiConstants.Card.cornerRadiusLarge)
            )
            .animateContentSize(),
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
                        .padding(
                            horizontal = UiConstants.SectionHeader.horizontalPadding,
                            vertical = UiConstants.SectionHeader.verticalPadding
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = UiStrings.SECTION_RESULTS,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f),
                        softWrap = true,
                        maxLines = 2
                    )
                    if (results.isNotEmpty()) {
                        TooltipBox(
                            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                            tooltip = { PlainTooltip { Text(UiStrings.ACTION_COPY_RESULTS) } },
                            state = rememberTooltipState()
                        ) {
                            IconButton(onClick = onCopyClick) {
                                Icon(
                                    Icons.Filled.ContentCopy,
                                    contentDescription = UiStrings.ACTION_COPY_RESULTS
                                )
                            }
                        }
                    }
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                        tooltip = { PlainTooltip { Text(if (expanded) UiStrings.ACTION_COLLAPSE else UiStrings.ACTION_EXPAND) } },
                        state = rememberTooltipState()
                    ) {
                        IconButton(onClick = onToggleExpand) {
                            AnimatedIcon(
                                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                                contentDescription = if (expanded) UiStrings.CONTENT_DESC_COLLAPSE else UiStrings.CONTENT_DESC_EXPAND
                            )
                        }
                    }
                }
            }

            SectionAnimatedVisibility(visible = expanded) {
                AnimatedContent(
                    targetState = results,
                    label = "results",
                    transitionSpec = { fadeIn() togetherWith fadeOut() }
                ) { list ->
                    Column(modifier = Modifier.padding(UiConstants.Padding.contentStandard)) {
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
                                            availableSeatsValue = availableSeatsValue,
                                            availableSeatsOverrideInputs = availableSeatsOverrideInputs,
                                            onAvailableSeatsOverrideChange = onAvailableSeatsOverrideChange,
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
    availableSeatsValue: Double,
    availableSeatsOverrideInputs: Map<Int, String>,
    onAvailableSeatsOverrideChange: (Int, String) -> Unit,
    focusManager: androidx.compose.ui.focus.FocusManager
) {
    // Use local state to prevent focus loss during typing
    var localOverrideInput by remember(weekIndex) {
        mutableStateOf(availableSeatsOverrideInputs[weekIndex] ?: "")
    }
    
    // Track if field is focused
    var isFocused by remember(weekIndex) { mutableStateOf(false) }
    
    // Track if current input is validated (synced to ViewModel)
    val isValidated by remember(weekIndex) {
        derivedStateOf {
            val vmValue = availableSeatsOverrideInputs[weekIndex] ?: ""
            localOverrideInput == vmValue
        }
    }
    
    // Check if input is valid (memoized)
    val isInputValid by remember {
        derivedStateOf {
            localOverrideInput.isEmpty() || 
            (localOverrideInput.toDoubleOrNull()?.let { 
                it >= 0.0 && it <= availableSeatsValue 
            } ?: false)
        }
    }
    
    // Sync external changes only when not focused (like loading a saved movie)
    // Using LaunchedEffect to avoid side effects during composition
    LaunchedEffect(isFocused, availableSeatsOverrideInputs, weekIndex) {
        if (!isFocused) {
            val externalInput = availableSeatsOverrideInputs[weekIndex] ?: ""
            if (externalInput != localOverrideInput) {
                localOverrideInput = externalInput
            }
        }
    }
    
    // Check if there's an override (memoized)
    val hasOverride by remember {
        derivedStateOf {
            localOverrideInput.isNotEmpty()
        }
    }
    
    // Display formatted value when validated and not focused (memoized)
    val displayValue by remember {
        derivedStateOf {
            if (!isFocused && isValidated && hasOverride) {
                // Format the validated value
                localOverrideInput.toDoubleOrNull()?.toLong()?.let { 
                    UiStrings.formatNumber(it)
                } ?: localOverrideInput
            } else {
                // Show raw input while typing or when not validated
                localOverrideInput
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Week result row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = UiStrings.weekNumber(weekNumber),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(0.8f)
            )
            // Isolated result value - only recomposes when resultValue changes
            key(resultValue) {
                Text(
                    text = UiStrings.formatNumber(resultValue),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.End,
                    color = if (!isValidated && hasOverride) {
                        // Show pending validation state
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Available seats override field with improved layout
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
                    text = "Seats:",
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
                            // Validate value is within range [0, availableSeatsValue]
                            val numValue = cleanValue.toDoubleOrNull()
                            if (numValue != null) {
                                // Only accept if within valid range
                                if (numValue >= 0.0 && numValue <= availableSeatsValue) {
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
                            
                            // Sync to ViewModel when focus is lost
                            if (wasFocused && !focusState.isFocused && isInputValid) {
                                onAvailableSeatsOverrideChange(weekIndex, localOverrideInput)
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
                            text = "Default: ${UiStrings.formatNumber(availableSeatsValue.toLong())}",
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
                            // Validate and sync when Return is pressed
                            if (isInputValid) {
                                // Sync to ViewModel for calculation update
                                onAvailableSeatsOverrideChange(weekIndex, localOverrideInput)
                                // Dismiss keyboard on successful validation
                                focusManager.clearFocus()
                            }
                            // Invalid - keep focus for correction (no sync)
                        }
                    ),
                    singleLine = true,
                    trailingIcon = if (hasOverride) {
                        {
                            IconButton(
                                onClick = {
                                    // Clear both local and ViewModel state
                                    localOverrideInput = ""
                                    onAvailableSeatsOverrideChange(weekIndex, "")
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
                    } else null,
                    isError = localOverrideInput.isNotEmpty() &&
                            (localOverrideInput.toDoubleOrNull()?.let { 
                                // Validate: must be between 0 and availableSeatsInput
                                it < 0.0 || it > availableSeatsValue 
                            } ?: true),
                    supportingText = if (localOverrideInput.isNotEmpty() &&
                        (localOverrideInput.toDoubleOrNull()?.let { 
                            it < 0.0 || it > availableSeatsValue 
                        } ?: true)
                    ) {
                        {
                            Text(
                                text = if (availableSeatsValue > 0.0) {
                                    "Must be between 0 and ${UiStrings.formatNumber(availableSeatsValue.toLong())}"
                                } else {
                                    "Enter available seats first"
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
