package org.aalbertini.ham.features.movie_distribution.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import hollywoodanimalmaster.composeapp.generated.resources.state_collapsed
import hollywoodanimalmaster.composeapp.generated.resources.state_expanded
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import hollywoodanimalmaster.composeapp.generated.resources.Res
import hollywoodanimalmaster.composeapp.generated.resources.action_clear
import hollywoodanimalmaster.composeapp.generated.resources.action_collapse_week
import hollywoodanimalmaster.composeapp.generated.resources.action_expand_week
import hollywoodanimalmaster.composeapp.generated.resources.badge_own_override
import hollywoodanimalmaster.composeapp.generated.resources.label_actual_attendance
import hollywoodanimalmaster.composeapp.generated.resources.label_actual_screenings
import hollywoodanimalmaster.composeapp.generated.resources.source_actual
import hollywoodanimalmaster.composeapp.generated.resources.source_calibrated
import hollywoodanimalmaster.composeapp.generated.resources.week_actual_support
import hollywoodanimalmaster.composeapp.generated.resources.week_actual_title
import hollywoodanimalmaster.composeapp.generated.resources.week_demand
import hollywoodanimalmaster.composeapp.generated.resources.week_filled
import hollywoodanimalmaster.composeapp.generated.resources.week_idle
import hollywoodanimalmaster.composeapp.generated.resources.week_label
import hollywoodanimalmaster.composeapp.generated.resources.week_own
import hollywoodanimalmaster.composeapp.generated.resources.week_own_override
import hollywoodanimalmaster.composeapp.generated.resources.week_own_override_support
import hollywoodanimalmaster.composeapp.generated.resources.week_rent
import hollywoodanimalmaster.composeapp.generated.resources.week_rented
import hollywoodanimalmaster.composeapp.generated.resources.week_saturated
import org.aalbertini.ham.core.ui.components.NumberField
import org.aalbertini.ham.core.util.format.grouped
import org.aalbertini.ham.features.movie_distribution.domain.model.DemandSource
import org.aalbertini.ham.features.movie_distribution.domain.model.DistributionDefaults
import org.aalbertini.ham.features.movie_distribution.domain.model.WeekActual
import org.aalbertini.ham.features.movie_distribution.domain.model.WeekPlan
import org.jetbrains.compose.resources.stringResource

/**
 * One week of the plan. Collapsed: what to rent. Expanded: this week's own-screenings override
 * and the real results from the game, which recalibrate every later week.
 */
@Composable
fun WeekCard(
    week: WeekPlan,
    expanded: Boolean,
    onToggle: () -> Unit,
    defaultOwn: Double,
    onOwnOverride: (Double?) -> Unit,
    onActual: (WeekActual?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val actual = week.actual
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (actual != null) MaterialTheme.colorScheme.surfaceContainerHigh
            else MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        shape = MaterialTheme.shapes.large,
    ) {
        Column {
            val toggleLabel = stringResource(
                if (expanded) Res.string.action_collapse_week else Res.string.action_expand_week,
                week.number,
            )
            val stateLabel = stringResource(if (expanded) Res.string.state_expanded else Res.string.state_collapsed)
            // The whole header row is the toggle (48 dp+ target); the chevron is only a cue.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClickLabel = toggleLabel, role = Role.Button, onClick = onToggle)
                    .semantics { stateDescription = stateLabel }
                    .padding(start = 16.dp, end = 12.dp, top = 12.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(stringResource(Res.string.week_label, week.number), style = MaterialTheme.typography.titleSmall)
                        // Forecast is the default: only tag weeks whose numbers come from real results.
                        when (week.source) {
                            DemandSource.FORMULA -> Unit
                            DemandSource.CALIBRATED -> Tag(stringResource(Res.string.source_calibrated), emphasized = true)
                            DemandSource.ACTUAL -> Tag(stringResource(Res.string.source_actual))
                        }
                        if (week.ownOverridden) Tag(stringResource(Res.string.badge_own_override))
                    }
                    Text(
                        text = stringResource(Res.string.week_demand, week.demand.grouped()) + " · " +
                            stringResource(Res.string.week_own, week.ownScreenings.grouped()),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    if (week.idleOwnScreenings > 0 && actual == null) {
                        Text(
                            stringResource(Res.string.week_idle, week.idleOwnScreenings.grouped()),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        week.toRent.grouped(),
                        style = MaterialTheme.typography.headlineSmall,
                        color = if (week.toRent > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        stringResource(if (actual != null) Res.string.week_rented else Res.string.week_rent),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Icon(
                    if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 12.dp),
                )
            }
            AnimatedVisibility(visible = expanded) {
                Column(Modifier.padding(start = 16.dp, end = 4.dp, bottom = 12.dp)) {
                    WeekEditor(week, defaultOwn, onOwnOverride, onActual)
                }
            }
        }
    }
}

@Composable
private fun WeekEditor(
    week: WeekPlan,
    defaultOwn: Double,
    onOwnOverride: (Double?) -> Unit,
    onActual: (WeekActual?) -> Unit,
) {
    Column(
        modifier = Modifier.padding(top = 8.dp, end = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        HorizontalDivider()
        NumberField(
            value = if (week.ownOverridden) week.ownScreenings.toDouble() else null,
            onValueChange = onOwnOverride,
            label = stringResource(Res.string.week_own_override),
            placeholder = defaultOwn.toLong().grouped(),
            max = DistributionDefaults.SCREENINGS_MAX.toDouble(),
            allowEmpty = true,
            supportingText = stringResource(Res.string.week_own_override_support),
            modifier = Modifier.fillMaxWidth(),
        )
        Text(stringResource(Res.string.week_actual_title), style = MaterialTheme.typography.labelLarge)
        Text(
            stringResource(Res.string.week_actual_support),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        // Both numbers are needed for a result; keep the half-typed one locally.
        var screenings by remember { mutableStateOf(week.actual?.screenings?.toDouble()) }
        var attendance by remember { mutableStateOf(week.actual?.attendancePercent) }
        LaunchedEffect(week.actual) {
            screenings = week.actual?.screenings?.toDouble()
            attendance = week.actual?.attendancePercent
        }
        fun publish() {
            val s = screenings
            val a = attendance
            if (s != null && a != null) onActual(WeekActual(s.toLong(), a))
            else if (s == null && a == null) onActual(null)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            NumberField(
                value = screenings,
                onValueChange = { screenings = it; publish() },
                label = stringResource(Res.string.label_actual_screenings),
                max = DistributionDefaults.SCREENINGS_MAX.toDouble(),
                allowEmpty = true,
                modifier = Modifier.weight(1f),
            )
            NumberField(
                value = attendance,
                onValueChange = { attendance = it; publish() },
                label = stringResource(Res.string.label_actual_attendance),
                max = 100.0,
                decimals = 1,
                allowEmpty = true,
                imeAction = ImeAction.Done,
                modifier = Modifier.weight(1f),
            )
        }
        week.actual?.let { result ->
            Text(
                stringResource(Res.string.week_filled, result.filledScreenings.toLong().grouped()),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (result.isSaturated) {
                Notice(
                    Icons.Outlined.WarningAmber,
                    stringResource(Res.string.week_saturated),
                    MaterialTheme.colorScheme.tertiaryContainer,
                    MaterialTheme.colorScheme.onTertiaryContainer,
                )
            }
        }
        if (week.actual != null || week.ownOverridden) {
            TextButton(
                onClick = {
                    screenings = null
                    attendance = null
                    onActual(null)
                    onOwnOverride(null)
                },
                modifier = Modifier.align(Alignment.End),
            ) { Text(stringResource(Res.string.action_clear)) }
        }
    }
}
