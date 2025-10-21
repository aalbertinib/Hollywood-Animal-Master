package org.aalbertini.ham.features.movie_distribution.presentation.components.section.results.week.card

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.aalbertini.ham.core.ui.components.TextIcon
import org.aalbertini.ham.core.ui.resources.Strings
import org.aalbertini.ham.core.ui.resources.UiIcons
import org.jetbrains.compose.resources.stringResource

/**
 * Header component for week result cards.
 *
 * Features:
 * - Week number display with icon
 * - Alternating background overlay for visual distinction
 * - Prominent styling with elevation
 *
 * @param weekNumber Week number to display (1-indexed)
 * @param headerColor Background color for the header
 * @param modifier Optional modifier
 */
@Composable
internal fun WeekCardHeader(
    weekNumber: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TextIcon(
            text = stringResource(Strings.weekNumber, weekNumber),
            icon = UiIcons.WEEK_NUMBER,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp, horizontal = 14.dp)
        )
    }
}
