package org.aalbertini.ham.features.movie_distribution.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import hollywoodanimalmaster.composeapp.generated.resources.Res
import hollywoodanimalmaster.composeapp.generated.resources.kpi_extension
import hollywoodanimalmaster.composeapp.generated.resources.kpi_peak
import hollywoodanimalmaster.composeapp.generated.resources.kpi_total_rent
import hollywoodanimalmaster.composeapp.generated.resources.kpi_unit_screenings
import hollywoodanimalmaster.composeapp.generated.resources.section_summary
import hollywoodanimalmaster.composeapp.generated.resources.summary_calibrated
import hollywoodanimalmaster.composeapp.generated.resources.summary_extension_warning
import hollywoodanimalmaster.composeapp.generated.resources.summary_hint
import org.aalbertini.ham.core.ui.components.SectionCard
import org.aalbertini.ham.core.util.format.grouped
import org.aalbertini.ham.features.movie_distribution.domain.model.DistributionPlan
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SummaryCard(plan: DistributionPlan, modifier: Modifier = Modifier) {
    SectionCard(title = stringResource(Res.string.section_summary), modifier = modifier) {
        val unit = stringResource(Res.string.kpi_unit_screenings)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Kpi(stringResource(Res.string.kpi_total_rent), plan.totalRent.grouped(), unit, hero = true)
            Kpi(stringResource(Res.string.kpi_peak), plan.peakDemand.grouped(), unit)
            if (plan.weeks.size > 4) {
                Kpi(stringResource(Res.string.kpi_extension), plan.extensionRent.grouped(), unit)
            }
        }
        if (plan.extensionRent > 0) {
            Notice(Icons.Outlined.WarningAmber, stringResource(Res.string.summary_extension_warning), MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.onTertiaryContainer)
        }
        if (plan.isCalibrated) {
            Notice(Icons.Outlined.Tune, stringResource(Res.string.summary_calibrated), MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.onSecondaryContainer)
        } else {
            Notice(Icons.Outlined.Info, stringResource(Res.string.summary_hint), MaterialTheme.colorScheme.surfaceContainerHighest, MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun Kpi(label: String, value: String, unit: String, hero: Boolean = false) {
    Column(
        modifier = Modifier
            .widthIn(min = 96.dp)
            .clearAndSetSemantics { contentDescription = "$label: $value $unit" },
    ) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            value,
            style = if (hero) MaterialTheme.typography.displaySmall else MaterialTheme.typography.headlineSmall,
            color = if (hero) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
        )
        Text(unit, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun Notice(icon: ImageVector, text: String, container: Color, content: Color, modifier: Modifier = Modifier) {
    Surface(color = container, contentColor = content, shape = MaterialTheme.shapes.medium, modifier = modifier) {
        Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.Top) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
            Text(text, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
