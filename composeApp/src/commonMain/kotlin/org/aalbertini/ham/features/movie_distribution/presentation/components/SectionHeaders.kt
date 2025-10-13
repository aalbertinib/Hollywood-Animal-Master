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
import org.aalbertini.ham.core.ui.resources.Strings
import org.jetbrains.compose.resources.stringResource

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
        title = stringResource(Strings.parametersSectionTitle),
        icon = UiStrings.SECTION_PARAMETERS_ICON,
        expanded = expanded,
        modifier = modifier
    ) {
        if (hasCurrentMovieResult) {
            GenericActionButton(
                onClick = onNewClick,
                icon = {
                    Icon(Icons.Filled.Add, contentDescription = stringResource(Strings.actionNewMovie))
                },
                contentDescription = stringResource(Strings.actionNewMovie),
                tooltipText = stringResource(Strings.actionNewMovie)
            )
        }
        if (commercialScoreValid && availableScreeningsValid && !isSavedWithoutChanges) {
            IconButton(onClick = onSaveClick) {
                Icon(Icons.Filled.Save, contentDescription = stringResource(Strings.actionSaveMovie))
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
        title = stringResource(Strings.resultsSectionTitle),
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
                        contentDescription = stringResource(Strings.actionCopyResults)
                    )
                },
                contentDescription = stringResource(Strings.actionCopyResults),
                tooltipText = stringResource(Strings.actionCopyResults)
            )
        }
        GenericActionButton(
            onClick = onToggleExpand,
            icon = {
                AnimatedIcon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expanded) stringResource(Strings.contentDescriptionCollapse) else stringResource(Strings.contentDescriptionExpand)
                )
            },
            contentDescription = if (expanded) stringResource(Strings.contentDescriptionCollapse) else stringResource(Strings.contentDescriptionExpand),
            tooltipText = if (expanded) stringResource(Strings.actionCollapse) else stringResource(Strings.actionExpand)
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
        title = stringResource(Strings.savedMoviesCount, movieCount),
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
                        contentDescription = stringResource(Strings.contentDescriptionClearAll),
                        tint = MaterialTheme.colorScheme.error
                    )
                },
                contentDescription = stringResource(Strings.contentDescriptionClearAll),
                tooltipText = stringResource(Strings.action_clear_all_with_count, movieCount)
            )
        }
        GenericActionButton(
            onClick = onToggleExpand,
            icon = {
                AnimatedIcon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expanded) stringResource(Strings.contentDescriptionCollapse) else stringResource(Strings.contentDescriptionExpand)
                )
            },
            contentDescription = if (expanded) stringResource(Strings.contentDescriptionCollapse) else stringResource(Strings.contentDescriptionExpand),
            tooltipText = if (expanded) stringResource(Strings.actionCollapse) else stringResource(Strings.actionExpand)
        )
    }
}
