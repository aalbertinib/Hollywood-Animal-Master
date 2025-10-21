package org.aalbertini.ham.features.movie_distribution.presentation.components.section

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
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
import org.aalbertini.ham.core.ui.components.section.ModernSectionHeader
import org.aalbertini.ham.core.ui.resources.Strings
import org.jetbrains.compose.resources.stringResource

/**
 * Modern header for Parameters Section
 * 
 * Uses ModernSectionHeader for clean dashboard design.
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
    ModernSectionHeader(
        title = stringResource(Strings.parametersSectionTitle),
        modifier = modifier,
        actions = {
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
        if (commercialScoreValid && availableScreeningsValid) {
            if (isSavedWithoutChanges) {
                // Show "up to date" icon when parameters match saved movie
                GenericActionButton(
                    onClick = { /* No action needed - already saved */ },
                    icon = {
                        Icon(
                            Icons.Filled.CheckCircle,
                            contentDescription = stringResource(Strings.upToDate),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    contentDescription = stringResource(Strings.upToDate),
                    tooltipText = stringResource(Strings.upToDate)
                )
            } else {
                // Show save button when there are changes
                IconButton(onClick = onSaveClick) {
                    Icon(Icons.Filled.Save, contentDescription = stringResource(Strings.actionSaveMovie))
                }
            }
        }
        }
    )
}

/**
 * Modern header for Results Section
 * 
 * Uses ModernSectionHeader for clean dashboard design.
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
    ModernSectionHeader(
        title = stringResource(Strings.resultsSectionTitle),
        modifier = modifier,
        actions = {
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
    )
}

/**
 * Modern header for Saved Movies Section
 * 
 * Uses ModernSectionHeader for clean dashboard design.
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
    ModernSectionHeader(
        title = stringResource(Strings.savedDistributionsTitle),
        subtitle = "($movieCount)",
        modifier = modifier,
        actions = {
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
    )
}
