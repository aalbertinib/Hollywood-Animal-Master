package org.aalbertini.ham.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.aalbertini.ham.core.ui.resources.UiConstants

/**
 * Generic full-screen dialog with card wrapper.
 * 
 * Provides consistent styling and behavior for custom dialogs:
 * - Card elevation and styling
 * - Header with icon and title
 * - Content area
 * - Action buttons
 * 
 * Follows SOLID principles through composition.
 * Thread-safe and null-safe.
 * 
 * @param onDismiss Callback when dialog is dismissed
 * @param title Dialog title
 * @param icon Optional header icon
 * @param iconTint Optional icon tint color
 * @param confirmText Confirm button text
 * @param dismissText Dismiss button text
 * @param onConfirm Confirm button callback
 * @param confirmEnabled Whether confirm button is enabled
 * @param content Dialog content
 */
@Composable
fun GenericFormDialog(
    onDismiss: () -> Unit,
    title: String,
    icon: ImageVector? = null,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    confirmText: String = "OK",
    dismissText: String = "Cancel",
    onConfirm: () -> Unit,
    confirmEnabled: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(UiConstants.Padding.contentStandard),
            elevation = CardDefaults.cardElevation(
                defaultElevation = UiConstants.Card.elevationDialog
            )
        ) {
            Column(
                modifier = Modifier.padding(UiConstants.Padding.contentStandard),
                verticalArrangement = Arrangement.spacedBy(UiConstants.Spacing.betweenSections)
            ) {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    icon?.let {
                        Icon(
                            imageVector = it,
                            contentDescription = null,
                            tint = iconTint,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                
                // Content
                content()
                
                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(dismissText)
                    }
                    Button(
                        onClick = onConfirm,
                        enabled = confirmEnabled
                    ) {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(confirmText)
                    }
                }
            }
        }
    }
}

/**
 * Generic confirmation dialog using AlertDialog.
 * 
 * Provides consistent styling for confirmation prompts:
 * - Icon header
 * - Title and message
 * - Confirm and dismiss buttons
 * 
 * Thread-safe and null-safe.
 * 
 * @param onDismiss Callback when dialog is dismissed
 * @param title Dialog title
 * @param message Dialog message content
 * @param icon Optional header icon
 * @param iconTint Optional icon tint color
 * @param confirmText Confirm button text
 * @param dismissText Dismiss button text
 * @param onConfirm Confirm button callback
 * @param confirmButtonColor Optional confirm button color
 */
@Composable
fun GenericConfirmationDialog(
    onDismiss: () -> Unit,
    title: String,
    message: String,
    icon: ImageVector? = null,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    confirmText: String = "Confirm",
    dismissText: String = "Cancel",
    onConfirm: () -> Unit,
    confirmButtonColor: Color? = null
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = icon?.let {
            {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                softWrap = true
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = if (confirmButtonColor != null) {
                    ButtonDefaults.buttonColors(containerColor = confirmButtonColor)
                } else {
                    ButtonDefaults.buttonColors()
                }
            ) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(dismissText)
            }
        }
    )
}

/**
 * Generic action button row for dialogs.
 * 
 * Provides consistent button layout:
 * - Right-aligned
 * - Proper spacing
 * - Standard button styling
 * 
 * @param onDismiss Dismiss callback
 * @param onConfirm Confirm callback
 * @param confirmText Confirm button text
 * @param dismissText Dismiss button text
 * @param confirmEnabled Whether confirm button is enabled
 * @param confirmIcon Optional confirm button icon
 * @param dismissIcon Optional dismiss button icon
 */
@Composable
fun GenericDialogActions(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    confirmText: String = "OK",
    dismissText: String = "Cancel",
    confirmEnabled: Boolean = true,
    confirmIcon: ImageVector = Icons.Filled.Check,
    dismissIcon: ImageVector = Icons.Filled.Close,
    additionalActions: @Composable RowScope.() -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        additionalActions()
        
        TextButton(onClick = onDismiss) {
            Icon(
                dismissIcon,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(dismissText)
        }
        
        Button(
            onClick = onConfirm,
            enabled = confirmEnabled
        ) {
            Icon(
                confirmIcon,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(confirmText)
        }
    }
}
