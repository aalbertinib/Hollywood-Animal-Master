package org.aalbertini.ham.features.movie_distribution.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.aalbertini.ham.core.ui.components.AnimatedIcon
import org.aalbertini.ham.core.ui.components.GenericActionButton
import org.aalbertini.ham.core.ui.components.GenericSectionHeader
import org.aalbertini.ham.core.ui.resources.UiStrings

/**
 * Sticky header for Parameters Section
 * 
 * Refactored to use GenericSectionHeader for consistency and reusability.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParametersSectionHeader(
    hasCurrentMovieResult: Boolean,
    commercialScoreValid: Boolean,
    availableScreeningsValid: Boolean,
    isSavedWithoutChanges: Boolean,
    expanded: Boolean,
    onSaveClick: () -> Unit,
    onNewClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GenericSectionHeader(
        title = UiStrings.SECTION_PARAMETERS,
        icon = UiStrings.SECTION_PARAMETERS_ICON,
        expanded = expanded,
        modifier = modifier
    ) {
        if (hasCurrentMovieResult) {
            GenericActionButton(
                onClick = onNewClick,
                icon = {
                    Icon(Icons.Filled.Add, contentDescription = UiStrings.ACTION_NEW_MOVIE)
                },
                contentDescription = UiStrings.ACTION_NEW_MOVIE,
                tooltipText = UiStrings.ACTION_NEW_MOVIE
            )
        }
        if (commercialScoreValid && availableScreeningsValid && !isSavedWithoutChanges) {
            IconButton(onClick = onSaveClick) {
                Icon(Icons.Filled.Save, contentDescription = UiStrings.ACTION_SAVE_MOVIE)
            }
        }
    }
}

/**
 * Sticky header for Results Section
 * 
 * Refactored to use GenericSectionHeader for consistency and reusability.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsSectionHeader(
    hasResults: Boolean,
    expanded: Boolean,
    onCopyClick: () -> Unit,
    onToggleExpand: () -> Unit,
    modifier: Modifier = Modifier
) {
    GenericSectionHeader(
        title = UiStrings.SECTION_RESULTS,
        icon = UiStrings.SECTION_RESULTS_ICON,
        expanded = expanded,
        modifier = modifier
    ) {
        if (hasResults) {
            GenericActionButton(
                onClick = onCopyClick,
                icon = {
                    Icon(
                        Icons.Filled.ContentCopy,
                        contentDescription = UiStrings.ACTION_COPY_RESULTS
                    )
                },
                contentDescription = UiStrings.ACTION_COPY_RESULTS,
                tooltipText = UiStrings.ACTION_COPY_RESULTS
            )
        }
        GenericActionButton(
            onClick = onToggleExpand,
            icon = {
                AnimatedIcon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expanded) UiStrings.CONTENT_DESC_COLLAPSE else UiStrings.CONTENT_DESC_EXPAND
                )
            },
            contentDescription = if (expanded) UiStrings.CONTENT_DESC_COLLAPSE else UiStrings.CONTENT_DESC_EXPAND,
            tooltipText = if (expanded) UiStrings.ACTION_COLLAPSE else UiStrings.ACTION_EXPAND
        )
    }
}

/**
 * Sticky header for Saved Movies Section
 * 
 * Refactored to use GenericSectionHeader for consistency and reusability.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedMoviesSectionHeader(
    movieCount: Int,
    expanded: Boolean,
    onClearAllClick: () -> Unit,
    onToggleExpand: () -> Unit,
    modifier: Modifier = Modifier
) {
    GenericSectionHeader(
        title = UiStrings.savedMoviesCount(movieCount),
        icon = UiStrings.SAVED_MOVIES_COUNT_ICON,
        expanded = expanded,
        modifier = modifier
    ) {
        if (movieCount > 0) {
            GenericActionButton(
                onClick = onClearAllClick,
                icon = {
                    Icon(
                        Icons.Filled.DeleteSweep,
                        contentDescription = UiStrings.CONTENT_DESC_CLEAR_ALL,
                        tint = MaterialTheme.colorScheme.error
                    )
                },
                contentDescription = UiStrings.CONTENT_DESC_CLEAR_ALL,
                tooltipText = UiStrings.actionClearAll(movieCount)
            )
        }
        GenericActionButton(
            onClick = onToggleExpand,
            icon = {
                AnimatedIcon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expanded) UiStrings.CONTENT_DESC_COLLAPSE else UiStrings.CONTENT_DESC_EXPAND
                )
            },
            contentDescription = if (expanded) UiStrings.CONTENT_DESC_COLLAPSE else UiStrings.CONTENT_DESC_EXPAND,
            tooltipText = if (expanded) UiStrings.ACTION_COLLAPSE else UiStrings.ACTION_EXPAND
        )
    }
}
