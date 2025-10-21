package org.aalbertini.ham.features.movie_distribution.presentation.components.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.aalbertini.ham.core.ui.components.list.ModernListItem
import org.aalbertini.ham.core.ui.components.section.ModernSectionWithHeader
import org.aalbertini.ham.core.ui.resources.Strings
import org.aalbertini.ham.features.movie_distribution.domain.formatting.formatNumberThousands
import org.aalbertini.ham.features.movie_distribution.domain.model.MovieResult
import org.jetbrains.compose.resources.stringResource

/**
 * Saved Movie Results Section with modern dashboard design.
 * Header is inside the card.
 * 
 * Features:
 * - Clean list design with icon badges
 * - Table-like structure
 * - Action buttons on the right
 * - Proper dividers between items
 * - Clear all and expand/collapse actions
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedMovieResultsSection(
    movieResults: List<MovieResult>,
    expanded: Boolean,
    movieCount: Int,
    onLoadClick: (MovieResult) -> Unit,
    onEditClick: (MovieResult) -> Unit,
    onDeleteClick: (MovieResult) -> Unit,
    onClearAllClick: () -> Unit,
    onToggleExpand: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModernSectionWithHeader(
        title = stringResource(Strings.savedDistributionsTitle),
        subtitle = if (movieCount > 0) "($movieCount)" else null,
        modifier = modifier,
        headerActions = {
            // Clear all button
            if (movieCount > 0) {
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = { PlainTooltip { Text(stringResource(Strings.clearAllButton)) } },
                    state = rememberTooltipState()
                ) {
                    FilledTonalIconButton(
                        onClick = onClearAllClick,
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    ) {
                        Icon(
                            Icons.Filled.DeleteSweep,
                            contentDescription = stringResource(Strings.clearAllButton)
                        )
                    }
                }
            }
            
            // Expand/collapse button
            if (movieCount > 0) {
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = { PlainTooltip { Text(if (expanded) stringResource(Strings.actionCollapse) else stringResource(Strings.actionExpand)) } },
                    state = rememberTooltipState()
                ) {
                    FilledTonalIconButton(
                        onClick = onToggleExpand,
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    ) {
                        Icon(
                            if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                            contentDescription = if (expanded) stringResource(Strings.actionCollapse) else stringResource(Strings.actionExpand)
                        )
                    }
                }
            }
        }
    ) {
        if (expanded) {
            if (movieResults.isEmpty()) {
                Text(
                    text = stringResource(Strings.infoNoSavedMovies),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    movieResults.forEachIndexed { index, movieResult ->
                        ModernListItem(
                            title = movieResult.title,
                            subtitle = "${stringResource(Strings.labelCommercialScore)}: ${movieResult.commercialScore} • ${stringResource(Strings.labelScreenings)}: ${movieResult.numberOfScreenings.toLong().formatNumberThousands()}",
                            icon = Icons.Default.Movie,
                            iconBackgroundColor = MaterialTheme.colorScheme.primaryContainer,
                            iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
                            showDivider = index < movieResults.size - 1,
                            trailingContent = {
                                IconButton(onClick = { onLoadClick(movieResult) }) {
                                    Icon(
                                        Icons.Filled.ContentCopy,
                                        contentDescription = stringResource(Strings.actionLoadMovie),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                IconButton(onClick = { onEditClick(movieResult) }) {
                                    Icon(
                                        Icons.Filled.Edit,
                                        contentDescription = stringResource(Strings.actionEditMovie),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                IconButton(onClick = { onDeleteClick(movieResult) }) {
                                    Icon(
                                        Icons.Filled.Delete,
                                        contentDescription = stringResource(Strings.actionDeleteMovie),
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Legacy saved movie results section for backward compatibility.
 * Delegates to the new modern implementation.
 */
@Deprecated("Use SavedMovieResultsSection directly", ReplaceWith("SavedMovieResultsSection"))
@Composable
fun SavedMovieDistributionSection(
    movieResults: List<MovieResult>,
    expanded: Boolean,
    onLoadClick: (MovieResult) -> Unit,
    onEditClick: (MovieResult) -> Unit,
    onDeleteClick: (MovieResult) -> Unit,
    modifier: Modifier = Modifier
) {
    SavedMovieResultsSection(
        movieResults = movieResults,
        expanded = expanded,
        movieCount = movieResults.size,
        onLoadClick = onLoadClick,
        onEditClick = onEditClick,
        onDeleteClick = onDeleteClick,
        onClearAllClick = {},
        onToggleExpand = {},
        modifier = modifier
    )
}
