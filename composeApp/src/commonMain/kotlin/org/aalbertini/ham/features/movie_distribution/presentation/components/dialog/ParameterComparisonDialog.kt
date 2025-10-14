package org.aalbertini.ham.features.movie_distribution.presentation.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.aalbertini.ham.core.ui.components.TextIcon
import org.aalbertini.ham.core.ui.components.button.DialogConfirmButton
import org.aalbertini.ham.core.ui.components.button.DialogDismissButton
import org.aalbertini.ham.core.ui.components.text.SectionDescription
import org.aalbertini.ham.core.ui.resources.Strings
import org.aalbertini.ham.core.ui.resources.UiIcons
import org.aalbertini.ham.features.movie_distribution.domain.formatting.formatNumberThousands
import org.jetbrains.compose.resources.stringResource

/**
 * Parameter conflict comparison dialog.
 * 
 * Displays when attempting to save a movie with a title that exists
 * but has different parameters.
 * 
 * Features:
 * - Side-by-side parameter comparison
 * - Visual distinction between saved and current parameters
 * - Keep existing or overwrite options
 * 
 * @param movieTitle Conflicting movie title
 * @param existingCommercialScore Saved commercial score
 * @param existingScreenings Saved screenings
 * @param newCommercialScore Current commercial score
 * @param newScreenings Current screenings
 * @param onDismiss Callback when dialog is dismissed
 * @param onKeepExisting Callback to keep existing movie
 * @param onOverwrite Callback to overwrite with new parameters
 */
@Composable
fun ParameterComparisonDialog(
    movieTitle: String,
    existingCommercialScore: Double,
    existingScreenings: Double,
    newCommercialScore: Double,
    newScreenings: Double,
    onDismiss: () -> Unit,
    onKeepExisting: () -> Unit,
    onOverwrite: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                Icons.Filled.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(32.dp)
            )
        },
        title = {
            DialogTitle()
        },
        text = {
            ComparisonContent(
                movieTitle = movieTitle,
                existingCommercialScore = existingCommercialScore,
                existingScreenings = existingScreenings,
                newCommercialScore = newCommercialScore,
                newScreenings = newScreenings
            )
        },
        confirmButton = {
            ComparisonActions(
                onKeepExisting = onKeepExisting,
                onOverwrite = onOverwrite
            )
        },
        dismissButton = {
            DialogDismissButton(onClick = onDismiss)
        }
    )
}

/**
 * Stateless dialog title component.
 */
@Composable
private fun DialogTitle() {
    TextIcon(
        text = stringResource(Strings.parameterConflict),
        icon = UiIcons.WARNING,
        style = MaterialTheme.typography.titleLarge
    )
}

/**
 * Stateless comparison content showing saved vs current parameters.
 */
@Composable
private fun ComparisonContent(
    movieTitle: String,
    existingCommercialScore: Double,
    existingScreenings: Double,
    newCommercialScore: Double,
    newScreenings: Double
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SectionDescription(
            text = stringResource(Strings.messageTitleExistsWithDiffParams, movieTitle)
        )

        Spacer(modifier = Modifier.height(4.dp))

        SavedParametersSection(
            commercialScore = existingCommercialScore,
            screenings = existingScreenings
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

        CurrentParametersSection(
            commercialScore = newCommercialScore,
            screenings = newScreenings
        )

        Spacer(modifier = Modifier.height(4.dp))

        SectionDescription(
            text = stringResource(Strings.chooseKeepOrOverwrite),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

/**
 * Saved parameters display section.
 */
@Composable
private fun SavedParametersSection(
    commercialScore: Double,
    screenings: Double
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        TextIcon(
            text = stringResource(Strings.savedParametersTitle),
            icon = UiIcons.DIALOG_SAVE_MOVIE,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        ParameterRow(
            label = stringResource(Strings.commercial_score_with_value, commercialScore.toString()),
            icon = UiIcons.MOVIE_RESULT_COMMERCIAL_SCORE
        )
        ParameterRow(
            label = stringResource(Strings.number_of_screenings_with_value, screenings.toLong().formatNumberThousands()),
            icon = UiIcons.MOVIE_RESULT_SCREENINGS
        )
    }
}

/**
 * Current parameters display section.
 */
@Composable
private fun CurrentParametersSection(
    commercialScore: Double,
    screenings: Double
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        TextIcon(
            text = stringResource(Strings.currentParametersTitle),
            icon = UiIcons.DIALOG_EDIT_MOVIE,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )
        ParameterRow(
            label = stringResource(Strings.commercial_score_with_value, commercialScore.toString()),
            icon = UiIcons.MOVIE_RESULT_COMMERCIAL_SCORE
        )
        ParameterRow(
            label = stringResource(Strings.number_of_screenings_with_value, screenings.toLong().formatNumberThousands()),
            icon = UiIcons.MOVIE_RESULT_SCREENINGS
        )
    }
}

/**
 * Reusable parameter row component.
 */
@Composable
private fun ParameterRow(
    label: String,
    icon: org.aalbertini.ham.core.ui.components.EmojiIcon
) {
    TextIcon(
        text = label,
        icon = icon,
        style = MaterialTheme.typography.bodyMedium
    )
}

/**
 * Dialog action buttons.
 */
@Composable
private fun ComparisonActions(
    onKeepExisting: () -> Unit,
    onOverwrite: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DialogDismissButton(
            onClick = onKeepExisting,
            text = stringResource(Strings.keep_saved),
            icon = Icons.Filled.Check
        )
        DialogConfirmButton(
            onClick = onOverwrite,
            text = stringResource(Strings.overwrite),
            icon = Icons.Filled.Check
        )
    }
}
