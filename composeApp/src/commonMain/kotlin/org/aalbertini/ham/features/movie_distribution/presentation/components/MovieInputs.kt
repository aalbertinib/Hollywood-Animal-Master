package org.aalbertini.ham.features.movie_distribution.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import hollywoodanimalmaster.composeapp.generated.resources.action_open_source
import hollywoodanimalmaster.composeapp.generated.resources.support_commercial
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import hollywoodanimalmaster.composeapp.generated.resources.Res
import hollywoodanimalmaster.composeapp.generated.resources.badge_recommended
import hollywoodanimalmaster.composeapp.generated.resources.custom_average
import hollywoodanimalmaster.composeapp.generated.resources.custom_decay
import hollywoodanimalmaster.composeapp.generated.resources.custom_week_one
import hollywoodanimalmaster.composeapp.generated.resources.custom_week_two
import hollywoodanimalmaster.composeapp.generated.resources.label_artistic
import hollywoodanimalmaster.composeapp.generated.resources.label_bonus
import hollywoodanimalmaster.composeapp.generated.resources.label_commercial
import hollywoodanimalmaster.composeapp.generated.resources.label_own
import hollywoodanimalmaster.composeapp.generated.resources.label_run
import hollywoodanimalmaster.composeapp.generated.resources.label_title
import hollywoodanimalmaster.composeapp.generated.resources.run_weeks
import hollywoodanimalmaster.composeapp.generated.resources.section_method
import hollywoodanimalmaster.composeapp.generated.resources.section_movie
import hollywoodanimalmaster.composeapp.generated.resources.support_artistic
import hollywoodanimalmaster.composeapp.generated.resources.support_bonus
import hollywoodanimalmaster.composeapp.generated.resources.support_own
import hollywoodanimalmaster.composeapp.generated.resources.untitled_movie
import org.aalbertini.ham.core.ui.components.NumberField
import org.aalbertini.ham.core.ui.components.SectionCard
import org.aalbertini.ham.core.util.format.toPlainString
import org.aalbertini.ham.features.movie_distribution.domain.model.CustomCurve
import org.aalbertini.ham.features.movie_distribution.domain.model.DistributionDefaults
import org.aalbertini.ham.features.movie_distribution.domain.model.DistributionMethod
import org.aalbertini.ham.features.movie_distribution.domain.model.MovieResult
import org.jetbrains.compose.resources.stringResource
import kotlin.math.roundToInt

typealias MovieEdit = ((MovieResult) -> MovieResult) -> Unit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieInputsCard(movie: MovieResult, onEdit: MovieEdit, modifier: Modifier = Modifier) {
    SectionCard(title = stringResource(Res.string.section_movie), modifier = modifier) {
        OutlinedTextField(
            value = movie.title,
            onValueChange = { title -> onEdit { it.copy(title = title) } },
            label = { Text(stringResource(Res.string.label_title)) },
            placeholder = { Text(stringResource(Res.string.untitled_movie)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            NumberField(
                value = movie.commercialScore,
                onValueChange = { value -> value?.let { onEdit { m -> m.copy(commercialScore = it) } } },
                label = stringResource(Res.string.label_commercial),
                min = DistributionDefaults.SCORE_MIN,
                max = DistributionDefaults.SCORE_MAX,
                decimals = 1,
                supportingText = stringResource(Res.string.support_commercial),
                modifier = Modifier.weight(1f),
            )
            NumberField(
                value = movie.artisticScore,
                onValueChange = { value -> onEdit { it.copy(artisticScore = value) } },
                label = stringResource(Res.string.label_artistic),
                min = DistributionDefaults.SCORE_MIN,
                max = DistributionDefaults.SCORE_MAX,
                decimals = 1,
                allowEmpty = true,
                supportingText = stringResource(Res.string.support_artistic),
                modifier = Modifier.weight(1f),
            )
        }
        NumberField(
            value = movie.numberOfScreenings,
            onValueChange = { value -> value?.let { onEdit { m -> m.copy(numberOfScreenings = it) } } },
            label = stringResource(Res.string.label_own),
            max = DistributionDefaults.SCREENINGS_MAX.toDouble(),
            supportingText = stringResource(Res.string.support_own),
            modifier = Modifier.fillMaxWidth(),
        )
        NumberField(
            value = movie.weekOneBonus.toDouble(),
            onValueChange = { value -> onEdit { it.copy(weekOneBonus = value?.toLong() ?: 0) } },
            label = stringResource(Res.string.label_bonus),
            max = DistributionDefaults.SCREENINGS_MAX.toDouble(),
            allowEmpty = true,
            supportingText = stringResource(Res.string.support_bonus),
            modifier = Modifier.fillMaxWidth(),
        )
        Text(stringResource(Res.string.label_run), style = MaterialTheme.typography.labelLarge)
        val options = DistributionDefaults.RUN_WEEK_OPTIONS
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            options.forEachIndexed { index, weeks ->
                SegmentedButton(
                    selected = movie.runWeeks == weeks,
                    onClick = { onEdit { it.copy(runWeeks = weeks) } },
                    shape = SegmentedButtonDefaults.itemShape(index, options.size),
                ) { Text(stringResource(Res.string.run_weeks, weeks)) }
            }
        }
    }
}

@Composable
fun MethodCard(movie: MovieResult, onEdit: MovieEdit, modifier: Modifier = Modifier) {
    SectionCard(title = stringResource(Res.string.section_method), modifier = modifier) {
        Column(Modifier.selectableGroup(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            DistributionMethod.entries.forEach { method ->
                MethodOption(
                    method = method,
                    selected = movie.method == method,
                    onSelect = { onEdit { it.copy(method = method) } },
                )
            }
        }
        if (movie.method == DistributionMethod.CUSTOM) {
            CustomCurveEditor(
                curve = movie.customCurve,
                onChange = { curve -> onEdit { it.copy(customCurve = curve) } },
            )
        }
    }
}

@Composable
private fun MethodOption(method: DistributionMethod, selected: Boolean, onSelect: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = if (selected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent,
        contentColor = if (selected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectable(selected = selected, onClick = onSelect, role = Role.RadioButton)
                .padding(start = 8.dp, end = 4.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.Top,
        ) {
            // onClick = null: the whole row is the touch target and carries the semantics.
            RadioButton(selected = selected, onClick = null, modifier = Modifier.padding(top = 2.dp, end = 12.dp))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(stringResource(method.titleRes), style = MaterialTheme.typography.bodyLarge)
                    if (method == DistributionDefaults.DEFAULT_METHOD) {
                        Tag(stringResource(Res.string.badge_recommended), emphasized = true)
                    }
                }
                Text(
                    stringResource(method.descriptionRes),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (selected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            method.sourceUrl?.let { url ->
                TextButton(
                    onClick = { uriHandler.openUri(url) },
                    contentPadding = PaddingValues(horizontal = 8.dp),
                ) {
                    Text(stringResource(Res.string.action_open_source), style = MaterialTheme.typography.labelMedium)
                    Icon(
                        Icons.AutoMirrored.Outlined.OpenInNew,
                        contentDescription = null,
                        modifier = Modifier.padding(start = 4.dp).size(14.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun CustomCurveEditor(curve: CustomCurve, onChange: (CustomCurve) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        SliderRow(
            label = stringResource(Res.string.custom_week_one, curve.weekOneMultiplier.toPlainString(1)),
            value = curve.weekOneMultiplier,
            range = 0.5..4.0,
            step = 0.1,
            onChange = { onChange(curve.copy(weekOneMultiplier = it)) },
        )
        SliderRow(
            label = stringResource(Res.string.custom_week_two, "${(curve.weekTwoRatio * 100).roundToInt()} %"),
            value = curve.weekTwoRatio,
            range = 0.2..1.0,
            step = 0.05,
            onChange = { onChange(curve.copy(weekTwoRatio = it)) },
        )
        SliderRow(
            label = stringResource(Res.string.custom_decay, "${(curve.weeklyDecay * 100).roundToInt()} %"),
            value = curve.weeklyDecay,
            range = 0.2..1.0,
            step = 0.05,
            onChange = { onChange(curve.copy(weeklyDecay = it)) },
        )
        Row(
            modifier = Modifier.fillMaxWidth()
                .selectable(
                    selected = curve.averageScores,
                    onClick = { onChange(curve.copy(averageScores = !curve.averageScores)) },
                    role = Role.Switch,
                )
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(stringResource(Res.string.custom_average), Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
            Switch(checked = curve.averageScores, onCheckedChange = null)
        }
    }
}

@Composable
private fun SliderRow(
    label: String,
    value: Double,
    range: ClosedFloatingPointRange<Double>,
    step: Double,
    onChange: (Double) -> Unit,
) {
    Column {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        val steps = ((range.endInclusive - range.start) / step).roundToInt() - 1
        Slider(
            value = value.toFloat(),
            onValueChange = { raw ->
                // Snap to the step so stored values stay round (0.8, not 0.80000001).
                val snapped = (raw / step).roundToInt() * step
                onChange(snapped.coerceIn(range.start, range.endInclusive))
            },
            valueRange = range.start.toFloat()..range.endInclusive.toFloat(),
            steps = steps,
        )
    }
}

/** Small non-interactive label (method badge, week source). */
@Composable
fun Tag(text: String, modifier: Modifier = Modifier, emphasized: Boolean = false) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = if (emphasized) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
        contentColor = if (emphasized) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer,
    ) {
        Text(text, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
    }
}
