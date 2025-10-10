package org.aalbertini.ham.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.aalbertini.ham.model.MovieResult

/**
 * Content-only wrapper for Parameters Section
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParametersSectionContent(
    commercialScoreInput: String,
    availableScreeningsInput: String,
    editableTitle: String,
    originalTitle: String?,
    originalCommercialScore: String?,
    originalAvailableScreenings: String?,
    onTitleChange: (String) -> Unit,
    onCommercialScoreChange: (String) -> Unit,
    onAvailableScreeningsChange: (String) -> Unit,
    onRevertTitle: () -> Unit,
    onRevertCommercialScore: () -> Unit,
    onRevertAvailableScreenings: () -> Unit,
    modifier: Modifier = Modifier
) {
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
        // Render ParametersSection without calling it - just its content
        Column(
            modifier = Modifier.padding(UiConstants.Padding.contentStandard)
        ) {
            // Title field
            OutlinedTextField(
                value = editableTitle,
                onValueChange = onTitleChange,
                label = { Text(UiStrings.LABEL_MOVIE_NAME) },
                singleLine = false,
                maxLines = 3,
                placeholder = { Text(UiStrings.PLACEHOLDER_UNSAVED) },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = if (originalTitle != null && editableTitle != originalTitle && editableTitle.isNotBlank()) {
                    {
                        TooltipBox(
                            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                            tooltip = { PlainTooltip { Text(UiStrings.ACTION_REVERT_TO_ORIGINAL) } },
                            state = rememberTooltipState()
                        ) {
                            IconButton(onClick = onRevertTitle) {
                                Icon(
                                    Icons.AutoMirrored.Filled.Undo,
                                    contentDescription = UiStrings.ACTION_REVERT_TO_ORIGINAL,
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }
                } else null
            )
            
            Spacer(modifier = Modifier.height(UiConstants.Spacing.betweenSections))
            
            // Continue with the form fields - simplified version for now
            // The full implementation would include all the logic from ParametersSection
        }
    }
}

/**
 * Content-only wrapper for Results Section  
 */
@Composable
fun ResultsSectionContent(
    results: List<Long>,
    expanded: Boolean,
    availableScreeningsValue: Double,
    availableScreeningsOverrideInputs: Map<Int, String>,
    currentMovieResultId: String?,
    onAvailableScreeningsOverrideChange: (Int, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(UiConstants.Card.padding)
            .border(
                width = UiConstants.Card.borderWidth,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(UiConstants.Card.cornerRadiusLarge)
            )
            .animateContentSizeFast(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(UiConstants.Card.cornerRadiusLarge)
    ) {
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
                        // Render results - simplified for now
                        list.forEachIndexed { index, value ->
                            Text("Week ${index + 1}: ${UiStrings.formatNumber(value)}")
                            if (index < list.size - 1) {
                                Spacer(modifier = Modifier.height(UiConstants.Spacing.betweenElements))
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Content-only wrapper for Saved Movies Section
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedMoviesSectionContent(
    movieResults: List<MovieResult>,
    expanded: Boolean,
    onLoadClick: (MovieResult) -> Unit,
    onEditClick: (MovieResult) -> Unit,
    onDeleteClick: (MovieResult) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(UiConstants.Card.padding)
            .border(
                width = UiConstants.Card.borderWidth,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(UiConstants.Card.cornerRadiusLarge)
            )
            .animateContentSizeFast(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(UiConstants.Card.cornerRadiusLarge)
    ) {
        SectionAnimatedVisibility(visible = expanded) {
            Column(modifier = Modifier.padding(UiConstants.Padding.contentStandard), verticalArrangement = Arrangement.spacedBy(UiConstants.Spacing.betweenElements)) {
                if (movieResults.isEmpty()) {
                    Text(
                        text = UiStrings.INFO_NO_SAVED_MOVIES,
                        style = MaterialTheme.typography.bodyMedium,
                        softWrap = true
                    )
                } else {
                    movieResults.forEachIndexed { index, movieResult ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = UiConstants.Card.borderWidth,
                                    color = MaterialTheme.colorScheme.outlineVariant,
                                    shape = RoundedCornerShape(UiConstants.Card.cornerRadiusSmall)
                                ),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            shape = RoundedCornerShape(UiConstants.Card.cornerRadiusSmall)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(UiConstants.Padding.itemInCard),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = movieResult.title,
                                        style = MaterialTheme.typography.titleSmall,
                                        softWrap = true
                                    )
                                    Text(
                                        text = UiStrings.movieResultCommercialScore(movieResult.commercialScore),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        softWrap = true
                                    )
                                    Text(
                                        text = UiStrings.movieResultNumberOfScreening(movieResult.numberOfScreenings),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        softWrap = true
                                    )
                                }
                                TooltipBox(
                                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                                    tooltip = { PlainTooltip { Text(UiStrings.ACTION_LOAD_MOVIE) } },
                                    state = rememberTooltipState()
                                ) {
                                    IconButton(onClick = { onLoadClick(movieResult) }) {
                                        Icon(Icons.Filled.ContentCopy, contentDescription = UiStrings.ACTION_LOAD_MOVIE)
                                    }
                                }
                                TooltipBox(
                                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                                    tooltip = { PlainTooltip { Text(UiStrings.ACTION_EDIT_MOVIE) } },
                                    state = rememberTooltipState()
                                ) {
                                    IconButton(onClick = { onEditClick(movieResult) }) {
                                        Icon(Icons.Filled.Edit, contentDescription = UiStrings.ACTION_EDIT_MOVIE)
                                    }
                                }
                                TooltipBox(
                                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                                    tooltip = { PlainTooltip { Text(UiStrings.ACTION_DELETE_MOVIE) } },
                                    state = rememberTooltipState()
                                ) {
                                    IconButton(onClick = { onDeleteClick(movieResult) }) {
                                        Icon(Icons.Filled.Delete, contentDescription = UiStrings.ACTION_DELETE_MOVIE)
                                    }
                                }
                            }
                        }
                        if (index < movieResults.size - 1) {
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}
