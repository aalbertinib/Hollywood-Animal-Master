package org.aalbertini.ham.features.movie_distribution.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hollywoodanimalmaster.composeapp.generated.resources.Res
import hollywoodanimalmaster.composeapp.generated.resources.action_cancel
import hollywoodanimalmaster.composeapp.generated.resources.action_import_confirm
import hollywoodanimalmaster.composeapp.generated.resources.import_body
import hollywoodanimalmaster.composeapp.generated.resources.import_label
import hollywoodanimalmaster.composeapp.generated.resources.import_title
import org.jetbrains.compose.resources.stringResource

/** Paste box for the text "Export library" put on the clipboard (another device or a backup). */
@Composable
fun ImportDialog(onImport: (String) -> Unit, onDismiss: () -> Unit) {
    var text by rememberSaveable { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.import_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(stringResource(Res.string.import_body), style = MaterialTheme.typography.bodyMedium)
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text(stringResource(Res.string.import_label)) },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 120.dp, max = 240.dp),
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onImport(text) }, enabled = text.isNotBlank()) {
                Text(stringResource(Res.string.action_import_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(Res.string.action_cancel)) }
        },
    )
}
