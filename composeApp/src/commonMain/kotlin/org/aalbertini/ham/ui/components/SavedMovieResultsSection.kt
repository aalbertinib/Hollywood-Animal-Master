package org.aalbertini.ham.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.aalbertini.ham.model.MovieResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedMovieResultsSection(
    movieResults: List<MovieResult>,
    expanded: Boolean,
    onToggleExpand: () -> Unit,
    onLoadClick: (MovieResult) -> Unit,
    onEditClick: (MovieResult) -> Unit,
    onDeleteClick: (MovieResult) -> Unit,
    onClearAllClick: () -> Unit,
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
                        .padding(horizontal = UiConstants.SectionHeader.horizontalPadding, vertical = UiConstants.SectionHeader.verticalPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = UiStrings.savedMoviesCount(movieResults.size),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f),
                        softWrap = true,
                        maxLines = 2
                    )
                    if (movieResults.isNotEmpty()) {
                        TooltipBox(
                            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                            tooltip = { PlainTooltip { Text(UiStrings.ACTION_CLEAR_ALL) } },
                            state = rememberTooltipState()
                        ) {
                            IconButton(onClick = onClearAllClick) {
                                Icon(
                                    Icons.Filled.DeleteSweep,
                                    contentDescription = UiStrings.CONTENT_DESC_CLEAR_ALL,
                                    tint = MaterialTheme.colorScheme.error
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
                            Icon(
                                if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                                contentDescription = if (expanded) UiStrings.CONTENT_DESC_COLLAPSE else UiStrings.CONTENT_DESC_EXPAND
                            )
                        }
                    }
                }
            }
            
            AnimatedVisibility(visible = expanded) {
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
                                            text = UiStrings.movieResultDetails(movieResult.commercialScore, movieResult.numberOfSeats),
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
}
