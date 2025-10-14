package org.aalbertini.ham.core.ui.components.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.aalbertini.ham.core.ui.resources.Strings
import org.jetbrains.compose.resources.stringResource

/**
 * Standard dialog action buttons row.
 * 
 * Stateless component providing consistent button layout
 * for dialog actions (Cancel + Confirm).
 * 
 * @param onDismiss Callback when cancel is clicked
 * @param onConfirm Callback when confirm is clicked
 * @param confirmEnabled Whether confirm button is enabled
 * @param confirmText Confirm button text
 * @param dismissText Dismiss button text
 * @param confirmIcon Optional confirm button icon
 * @param dismissIcon Optional dismiss button icon
 * @param confirmColors Optional confirm button colors
 * @param additionalActions Optional additional action buttons
 * @param modifier Optional modifier
 */
@Composable
fun DialogActionButtons(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    confirmEnabled: Boolean = true,
    confirmText: String = stringResource(Strings.saveButton),
    dismissText: String = stringResource(Strings.cancel),
    confirmIcon: ImageVector = Icons.Filled.Check,
    dismissIcon: ImageVector = Icons.Filled.Close,
    confirmColors: ButtonColors? = null,
    additionalActions: @Composable RowScope.() -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        additionalActions()
        
        DialogDismissButton(
            onClick = onDismiss,
            text = dismissText,
            icon = dismissIcon
        )
        
        DialogConfirmButton(
            onClick = onConfirm,
            enabled = confirmEnabled,
            text = confirmText,
            icon = confirmIcon,
            colors = confirmColors
        )
    }
}

/**
 * Standard dialog dismiss/cancel button.
 * 
 * Stateless atomic button component.
 * 
 * @param onClick Callback when button is clicked
 * @param text Button text
 * @param icon Button icon
 * @param modifier Optional modifier
 */
@Composable
fun DialogDismissButton(
    onClick: () -> Unit,
    text: String = stringResource(Strings.cancel),
    icon: ImageVector = Icons.Filled.Close,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(text)
    }
}

/**
 * Standard dialog confirm button.
 * 
 * Stateless atomic button component.
 * 
 * @param onClick Callback when button is clicked
 * @param enabled Whether button is enabled
 * @param text Button text
 * @param icon Button icon
 * @param colors Optional custom colors
 * @param modifier Optional modifier
 */
@Composable
fun DialogConfirmButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
    text: String = stringResource(Strings.saveButton),
    icon: ImageVector = Icons.Filled.Check,
    colors: ButtonColors? = null,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = colors ?: ButtonDefaults.buttonColors(),
        modifier = modifier
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(text)
    }
}

/**
 * Destructive action button (e.g., delete, clear all).
 * 
 * Pre-configured with error colors for destructive actions.
 * 
 * @param onClick Callback when button is clicked
 * @param text Button text
 * @param icon Button icon
 * @param enabled Whether button is enabled
 * @param modifier Optional modifier
 */
@Composable
fun DestructiveActionButton(
    onClick: () -> Unit,
    text: String,
    icon: ImageVector,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error
        ),
        modifier = modifier
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(text)
    }
}
