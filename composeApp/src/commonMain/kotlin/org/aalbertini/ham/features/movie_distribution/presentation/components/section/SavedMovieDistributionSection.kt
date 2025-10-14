package org.aalbertini.ham.features.movie_distribution.presentation.components.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.aalbertini.ham.core.ui.components.GenericActionButton
import org.aalbertini.ham.core.ui.components.GenericListItemCard
import org.aalbertini.ham.core.ui.components.GenericSectionCard
import org.aalbertini.ham.core.ui.components.TextIcon
import org.aalbertini.ham.core.ui.resources.Strings
import org.aalbertini.ham.core.ui.resources.UiConstants
import org.aalbertini.ham.core.ui.resources.UiIcons
import org.aalbertini.ham.features.movie_distribution.domain.formatting.formatNumberThousands
import org.aalbertini.ham.features.movie_distribution.domain.model.MovieResult
import org.jetbrains.compose.resources.stringResource

/**
 * Saved Movie Results Section using generic components.
 * 
 * Refactored to use GenericSectionCard and GenericListItemCard
 * for consistency, reusability, and adherence to SOLID principles.
 */
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
    GenericSectionCard(
        modifier = modifier,
        expanded = expanded
    ) {
        if (movieResults.isEmpty()) {
            TextIcon(
                text = stringResource(Strings.infoNoSavedMovies),
                icon = UiIcons.INFO,
                softWrap = true
            )
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(UiConstants.Spacing.betweenElements)
            ) {
                movieResults.forEachIndexed { index, movieResult ->
                    GenericListItemCard(
                        trailingActions = {
                            GenericActionButton(
                                onClick = { onLoadClick(movieResult) },
                                icon = {
                                    Icon(
                                        Icons.Filled.ContentCopy,
                                        contentDescription = stringResource(Strings.actionLoadMovie),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                },
                                contentDescription = stringResource(Strings.actionLoadMovie),
                                tooltipText = stringResource(Strings.actionLoadMovie)
                            )
                            GenericActionButton(
                                onClick = { onEditClick(movieResult) },
                                icon = {
                                    Icon(
                                        Icons.Filled.Edit,
                                        contentDescription = stringResource(Strings.actionEditMovie),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                },
                                contentDescription = stringResource(Strings.actionEditMovie),
                                tooltipText = stringResource(Strings.actionEditMovie)
                            )
                            GenericActionButton(
                                onClick = { onDeleteClick(movieResult) },
                                icon = {
                                    Icon(
                                        Icons.Filled.Delete,
                                        contentDescription = stringResource(Strings.actionDeleteMovie),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                },
                                contentDescription = stringResource(Strings.actionDeleteMovie),
                                tooltipText = stringResource(Strings.actionDeleteMovie)
                            )
                        }
                    ) {
                        Text(
                            text = movieResult.title,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            softWrap = true
                        )
                        TextIcon(
                            text = stringResource(
                                Strings.commercial_score_with_value,
                                movieResult.commercialScore.toString()
                            ),
                            icon = UiIcons.MOVIE_RESULT_COMMERCIAL_SCORE,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            softWrap = true
                        )
                        TextIcon(
                            text = stringResource(
                                Strings.number_of_screenings_with_value,
                                movieResult.numberOfScreenings.toInt().formatNumberThousands()
                            ),
                            icon = UiIcons.MOVIE_RESULT_SCREENINGS,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            softWrap = true
                        )
                    }
                    if (index < movieResults.size - 1) {
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}
