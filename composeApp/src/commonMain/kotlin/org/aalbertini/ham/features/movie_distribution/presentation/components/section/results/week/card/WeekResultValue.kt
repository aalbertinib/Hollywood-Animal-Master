package org.aalbertini.ham.features.movie_distribution.presentation.components.section.results.week.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.aalbertini.ham.core.ui.resources.Strings
import org.aalbertini.ham.features.movie_distribution.domain.formatting.formatNumberThousands
import org.jetbrains.compose.resources.stringResource

/**
 * Result value display component for week cards.
 * 
 * Features:
 * - Large formatted number display
 * - Visual feedback for pending validation state
 * - Isolated recomposition using key()
 * - Accessibility-friendly text labels
 * 
 * @param resultValue Calculated screening result
 * @param isValidated Whether override input is validated
 * @param hasOverride Whether there's an active override
 * @param modifier Optional modifier
 */
@Composable
internal fun WeekResultValue(
    resultValue: Long,
    isValidated: Boolean,
    hasOverride: Boolean,
    modifier: Modifier = Modifier
) {
    key(resultValue) {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(Strings.movieResultsScreenings),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = if (!isValidated && hasOverride) {
                    MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.7f)
                } else {
                    MaterialTheme.colorScheme.tertiaryContainer
                },
                tonalElevation = 1.dp,
                modifier = Modifier
            ) {
                Text(
                    text = resultValue.formatNumberThousands(),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    color = if (!isValidated && hasOverride) {
                        MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.5f)
                    } else {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    },
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        }
    }
}
