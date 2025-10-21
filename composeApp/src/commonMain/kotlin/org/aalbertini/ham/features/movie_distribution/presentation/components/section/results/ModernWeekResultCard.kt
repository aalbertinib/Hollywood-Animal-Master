package org.aalbertini.ham.features.movie_distribution.presentation.components.section.results

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.aalbertini.ham.core.ui.resources.Strings
import org.aalbertini.ham.core.ui.theme.CustomShapes
import org.aalbertini.ham.features.movie_distribution.domain.formatting.formatNumberThousands
import org.jetbrains.compose.resources.stringResource

// Visual transformation to display a percent suffix as formatting (non-editable), while keeping raw input
private val PercentSuffixTransformation = VisualTransformation { text ->
    val suffix = " %"
    val out = AnnotatedString(text.text + suffix)
    val offsetMapping = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int = offset
        override fun transformedToOriginal(offset: Int): Int = offset.coerceAtMost(text.text.length)
    }
    TransformedText(out, offsetMapping)
}

@Composable
fun ModernWeekResultCard(
    weekNumber: Int,
    screenings: Long,
    availableScreeningsValue: Double,
    availableScreeningsOverride: String,
    weekMultiplierOverride: String,
    weekMultiplierDefaultValue: Double,
    onAvailableScreeningsOverrideChange: (String) -> Unit,
    onWeekMultiplierOverrideChange: (String) -> Unit,
    focusManager: FocusManager,
    modifier: Modifier = Modifier
) {
    val defaultPercentage = remember(weekMultiplierDefaultValue) {
        val offset = ((weekMultiplierDefaultValue - 1.0) * 100).toInt()
        if (offset > 0) "+$offset" else "$offset"
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = CustomShapes.ElevatedCardShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .animateContentSize(spring(stiffness = Spring.StiffnessMediumLow)),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
            Text(
                text = "Week $weekNumber",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            
            // Week Result with tertiary background
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(Strings.movieResultsScreenings),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    tonalElevation = 1.dp,
                ) {
                    Text(
                        text = screenings.formatNumberThousands(),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // Available Screenings Override
            OutlinedTextField(
                value = availableScreeningsOverride,
                onValueChange = onAvailableScreeningsOverrideChange,
                label = { Text("Available Screenings") },
                placeholder = { Text(availableScreeningsValue.toLong().formatNumberThousands()) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    if (availableScreeningsOverride.isNotBlank()) {
                        IconButton(onClick = { onAvailableScreeningsOverrideChange("") }) {
                            Icon(Icons.Filled.Clear, contentDescription = "Clear")
                        }
                    } else {
                        Box(Modifier.size(24.dp))
                    }
                }
            )

            // Week Multiplier Override (as percentage)
            OutlinedTextField(
                value = weekMultiplierOverride,
                onValueChange = onWeekMultiplierOverrideChange,
                label = { Text(stringResource(Strings.movieResultsWeekMultiplierOverride)) },
                placeholder = { Text("$defaultPercentage") },
                visualTransformation = PercentSuffixTransformation,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    if (weekMultiplierOverride.isNotBlank()) {
                        IconButton(onClick = { onWeekMultiplierOverrideChange("") }) {
                            Icon(Icons.Filled.Clear, contentDescription = "Clear")
                        }
                    } else {
                        Box(Modifier.size(24.dp))
                    }
                }
            )
        }
    }
}

