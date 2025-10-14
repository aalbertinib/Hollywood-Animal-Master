package org.aalbertini.ham.core.ui.components.text

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

/**
 * Small centered label text component.
 * 
 * Stateless atomic component for field labels.
 * 
 * @param text Label text
 * @param modifier Optional modifier
 * @param color Text color
 * @param style Text style
 */
@Composable
fun SmallCenteredLabel(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    style: TextStyle = MaterialTheme.typography.bodySmall
) {
    Text(
        text = text,
        style = style,
        color = color,
        textAlign = TextAlign.Center,
        modifier = modifier.fillMaxWidth()
    )
}

/**
 * Section description text component.
 * 
 * Stateless component for descriptive text in sections.
 * 
 * @param text Description text
 * @param modifier Optional modifier
 * @param color Text color
 * @param style Text style
 */
@Composable
fun SectionDescription(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    style: TextStyle = MaterialTheme.typography.bodyMedium
) {
    Text(
        text = text,
        style = style,
        color = color,
        softWrap = true,
        modifier = modifier
    )
}

/**
 * Error message text component.
 * 
 * Stateless component for displaying errors.
 * 
 * @param text Error message text
 * @param modifier Optional modifier
 * @param style Text style
 */
@Composable
fun ErrorMessage(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.labelSmall
) {
    Text(
        text = text,
        style = style,
        color = MaterialTheme.colorScheme.error,
        modifier = modifier
    )
}

/**
 * Warning message text component.
 * 
 * Stateless component for displaying warnings.
 * 
 * @param text Warning message text
 * @param modifier Optional modifier
 * @param style Text style
 */
@Composable
fun WarningMessage(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium
) {
    Text(
        text = text,
        style = style,
        color = MaterialTheme.colorScheme.error,
        softWrap = true,
        modifier = modifier
    )
}

/**
 * Info message text component.
 * 
 * Stateless component for displaying info messages.
 * 
 * @param text Info message text
 * @param modifier Optional modifier
 * @param style Text style
 */
 
