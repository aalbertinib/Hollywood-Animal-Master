package org.aalbertini.ham.ui.components

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
import androidx.compose.material.icons.filled.Edit
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
import org.aalbertini.ham.ui.theme.CustomShapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedMovieResultsSection(
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
                Column(
                    modifier = Modifier.padding(
                        horizontal = UiConstants.SectionHeader.horizontalPadding,
                        vertical = UiConstants.SectionHeader.verticalPadding
                    ),
                    verticalArrangement = Arrangement.spacedBy(UiConstants.Spacing.betweenElements)
                ) {
                    if (movieResults.isEmpty()) {
                        TextIcon(
                            text = UiStrings.INFO_NO_SAVED_MOVIES,
                            icon = UiStrings.INFO_NO_SAVED_MOVIES_ICON,
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
                                    modifier = Modifier.fillMaxWidth()
                                        .padding(UiConstants.Padding.itemInCard),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = movieResult.title,
                                            style = MaterialTheme.typography.titleSmall,
                                            softWrap = true
                                        )
                                        TextIcon(
                                            text = UiStrings.movieResultCommercialScore(movieResult.commercialScore),
                                            icon = UiStrings.MOVIE_RESULT_COMMERCIAL_SCORE_ICON,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            softWrap = true
                                        )
                                        TextIcon(
                                            text = UiStrings.movieResultNumberOfScreening(
                                                movieResult.numberOfScreenings
                                            ),
                                            icon = UiStrings.MOVIE_RESULT_SCREENINGS_ICON,
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
                                            Icon(
                                                Icons.Filled.ContentCopy,
                                                contentDescription = UiStrings.ACTION_LOAD_MOVIE
                                            )
                                        }
                                    }
                                    TooltipBox(
                                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                                        tooltip = { PlainTooltip { Text(UiStrings.ACTION_EDIT_MOVIE) } },
                                        state = rememberTooltipState()
                                    ) {
                                        IconButton(onClick = { onEditClick(movieResult) }) {
                                            Icon(
                                                Icons.Filled.Edit,
                                                contentDescription = UiStrings.ACTION_EDIT_MOVIE
                                            )
                                        }
                                    }
                                    TooltipBox(
                                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                                        tooltip = { PlainTooltip { Text(UiStrings.ACTION_DELETE_MOVIE) } },
                                        state = rememberTooltipState()
                                    ) {
                                        IconButton(onClick = { onDeleteClick(movieResult) }) {
                                            Icon(
                                                Icons.Filled.Delete,
                                                contentDescription = UiStrings.ACTION_DELETE_MOVIE
                                            )
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
