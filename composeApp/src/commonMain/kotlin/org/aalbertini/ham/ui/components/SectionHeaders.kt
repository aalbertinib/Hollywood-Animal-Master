package org.aalbertini.ham.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Sticky header for Parameters Section
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
    val (bottomStartRadius, bottomEndRadius) = animatedSectionHeaderCorners(expanded)
    
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primaryContainer,
        tonalElevation = UiConstants.SectionHeader.tonalElevation,
        shape = RoundedCornerShape(
            topStart = UiConstants.Card.cornerRadiusLarge,
            topEnd = UiConstants.Card.cornerRadiusLarge,
            bottomStart = bottomStartRadius,
            bottomEnd = bottomEndRadius
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = UiConstants.SectionHeader.horizontalPadding, vertical = UiConstants.SectionHeader.verticalPadding)
                .height(UiConstants.SectionHeader.minHeight),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextIcon(
                text = UiStrings.SECTION_PARAMETERS,
                icon = UiStrings.SECTION_PARAMETERS_ICON,
                style = MaterialTheme.typography.titleMedium,
                softWrap = true
            )
            Row {
                if (hasCurrentMovieResult) {
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                        tooltip = { PlainTooltip { Text(UiStrings.ACTION_NEW_MOVIE) } },
                        state = rememberTooltipState()
                    ) {
                        IconButton(onClick = onNewClick) {
                            Icon(Icons.Filled.Add, contentDescription = UiStrings.ACTION_NEW_MOVIE)
                        }
                    }
                }
                if (commercialScoreValid && availableScreeningsValid) {
                    if (!isSavedWithoutChanges) {
                        IconButton(onClick = onSaveClick) {
                            Icon(Icons.Filled.Save, contentDescription = UiStrings.ACTION_SAVE_MOVIE)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Sticky header for Results Section
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
    val (bottomStartRadius, bottomEndRadius) = animatedSectionHeaderCorners(expanded)
    
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primaryContainer,
        tonalElevation = UiConstants.SectionHeader.tonalElevation,
        shape = RoundedCornerShape(
            topStart = UiConstants.Card.cornerRadiusLarge,
            topEnd = UiConstants.Card.cornerRadiusLarge,
            bottomStart = bottomStartRadius,
            bottomEnd = bottomEndRadius
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = UiConstants.SectionHeader.horizontalPadding,
                    vertical = UiConstants.SectionHeader.verticalPadding
                )
                .height(UiConstants.SectionHeader.minHeight),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextIcon(
                text = UiStrings.SECTION_RESULTS,
                icon = UiStrings.SECTION_RESULTS_ICON,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
                softWrap = true
            )
            if (hasResults) {
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
}

/**
 * Sticky header for Saved Movies Section
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
    val (bottomStartRadius, bottomEndRadius) = animatedSectionHeaderCorners(expanded)
    
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primaryContainer,
        tonalElevation = UiConstants.SectionHeader.tonalElevation,
        shape = RoundedCornerShape(
            topStart = UiConstants.Card.cornerRadiusLarge,
            topEnd = UiConstants.Card.cornerRadiusLarge,
            bottomStart = bottomStartRadius,
            bottomEnd = bottomEndRadius
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = UiConstants.SectionHeader.horizontalPadding, vertical = UiConstants.SectionHeader.verticalPadding)
                .height(UiConstants.SectionHeader.minHeight),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextIcon(
                text = UiStrings.savedMoviesCount(movieCount),
                icon = UiStrings.SAVED_MOVIES_COUNT_ICON,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
                softWrap = true
            )
            if (movieCount > 0) {
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = { PlainTooltip { Text(UiStrings.actionClearAll(movieCount)) } },
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
                    AnimatedIcon(
                        imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = if (expanded) UiStrings.CONTENT_DESC_COLLAPSE else UiStrings.CONTENT_DESC_EXPAND
                    )
                }
            }
        }
    }
}
