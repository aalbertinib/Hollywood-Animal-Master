package org.aalbertini.ham.features.movie_distribution.presentation.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hollywoodanimalmaster.composeapp.generated.resources.Res
import hollywoodanimalmaster.composeapp.generated.resources.message_clear_all
import org.aalbertini.ham.core.ui.components.button.DialogDismissButton
import org.aalbertini.ham.core.ui.components.button.DestructiveActionButton
import org.aalbertini.ham.core.ui.components.text.SectionDescription
import org.aalbertini.ham.core.ui.components.text.WarningMessage
import org.aalbertini.ham.core.ui.resources.Strings
import org.aalbertini.ham.core.ui.resources.UiConstants
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

/**
 * Confirmation dialog for clearing all saved movie results.
 * 
 * Features:
 * - Destructive action warning
 * - Pluralized message based on movie count
 * - Cannot undo warning
 * - Error-colored confirm button
 * 
 * @param movieResultCount Number of movies to be deleted
 * @param onDismiss Callback when dialog is dismissed
 * @param onConfirm Callback when deletion is confirmed
 */
@Composable
fun ClearAllConfirmationDialog(
    movieResultCount: Int,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                Icons.Filled.DeleteSweep,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(32.dp)
            )
        },
        title = {
            Text(
                text = stringResource(Strings.dialogTitleClearAll),
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            ClearAllDialogContent(movieResultCount = movieResultCount)
        },
        confirmButton = {
            DeleteButton(onClick = onConfirm)
        },
        dismissButton = {
            CancelButton(onClick = onDismiss)
        }
    )
}

/**
 * Stateless dialog content with warnings.
 */
@Composable
private fun ClearAllDialogContent(movieResultCount: Int) {
    Column(verticalArrangement = Arrangement.spacedBy(UiConstants.Spacing.betweenElements)) {
        SectionDescription(
            text = pluralStringResource(
                Res.plurals.message_clear_all,
                movieResultCount,
                movieResultCount
            )
        )
        WarningMessage(
            text = stringResource(Strings.warningCannotUndo)
        )
    }
}

/**
 * Reusable delete button component.
 */
@Composable
private fun DeleteButton(onClick: () -> Unit) {
    DestructiveActionButton(
        onClick = onClick,
        text = stringResource(Strings.clearAllButton),
        icon = Icons.Filled.DeleteSweep
    )
}

/**
 * Reusable cancel button component.
 */
@Composable
private fun CancelButton(onClick: () -> Unit) {
    DialogDismissButton(onClick = onClick)
}
