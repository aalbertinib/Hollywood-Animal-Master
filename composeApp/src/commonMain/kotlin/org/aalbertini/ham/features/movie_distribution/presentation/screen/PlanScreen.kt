package org.aalbertini.ham.features.movie_distribution.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.UnfoldLess
import androidx.compose.material.icons.outlined.UnfoldMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import hollywoodanimalmaster.composeapp.generated.resources.Res
import hollywoodanimalmaster.composeapp.generated.resources.action_collapse_all
import hollywoodanimalmaster.composeapp.generated.resources.action_expand_all
import hollywoodanimalmaster.composeapp.generated.resources.action_view_plan
import hollywoodanimalmaster.composeapp.generated.resources.group_total
import hollywoodanimalmaster.composeapp.generated.resources.phase_extension
import hollywoodanimalmaster.composeapp.generated.resources.phase_extension_hint
import hollywoodanimalmaster.composeapp.generated.resources.phase_initial
import hollywoodanimalmaster.composeapp.generated.resources.strip_summary
import kotlinx.coroutines.launch
import org.aalbertini.ham.core.util.format.grouped
import org.aalbertini.ham.features.movie_distribution.domain.model.RunPhase
import org.aalbertini.ham.features.movie_distribution.domain.model.WeekPlan
import org.aalbertini.ham.features.movie_distribution.presentation.PlannerUiState
import org.aalbertini.ham.features.movie_distribution.presentation.components.MethodCard
import org.aalbertini.ham.features.movie_distribution.presentation.components.MovieEdit
import org.aalbertini.ham.features.movie_distribution.presentation.components.MovieInputsCard
import org.aalbertini.ham.features.movie_distribution.presentation.components.PlanChart
import org.aalbertini.ham.features.movie_distribution.presentation.components.SummaryCard
import org.aalbertini.ham.features.movie_distribution.presentation.components.WeekCard
import org.jetbrains.compose.resources.stringResource

/** Two panes from 840 dp (M3 "expanded" width): inputs on the left, the plan on the right. */
private val TwoPaneMinWidth = 840.dp
private val InputPaneWidth = 400.dp
private val PlanPaneMaxWidth = 880.dp
private val ContentMaxWidth = 720.dp

private const val SUMMARY_KEY = "summary"

/** Which week cards are open. Kept per screen so groups can open/close together. */
private class ExpandedWeeks(initial: Set<Int>) {
    var indices by mutableStateOf(initial)

    fun toggle(index: Int) {
        indices = if (index in indices) indices - index else indices + index
    }

    fun setAll(weeks: List<WeekPlan>, expanded: Boolean) {
        val group = weeks.map { it.index }.toSet()
        indices = if (expanded) indices + group else indices - group
    }

    companion object {
        val Saver = Saver<ExpandedWeeks, List<Int>>(
            save = { it.indices.toList() },
            restore = { ExpandedWeeks(it.toSet()) },
        )
    }
}

@Composable
fun PlanScreen(state: PlannerUiState, onEdit: MovieEdit, modifier: Modifier = Modifier) {
    val expanded = rememberSaveable(saver = ExpandedWeeks.Saver) { ExpandedWeeks(emptySet()) }
    BoxWithConstraints(modifier.fillMaxSize()) {
        val contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp)
        if (maxWidth >= TwoPaneMinWidth) {
            Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                LazyColumn(
                    modifier = Modifier.width(InputPaneWidth).fillMaxHeight(),
                    contentPadding = contentPadding,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) { inputs(state, onEdit) }
                Box(Modifier.weight(1f).fillMaxHeight(), contentAlignment = Alignment.TopCenter) {
                    LazyColumn(
                        modifier = Modifier.widthIn(max = PlanPaneMaxWidth).fillMaxSize(),
                        contentPadding = contentPadding,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) { results(state, onEdit, expanded) }
                }
            }
        } else {
            val listState = rememberLazyListState()
            Column(Modifier.fillMaxSize()) {
                SummaryStrip(state, listState)
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = contentPadding,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    inputs(state, onEdit, Modifier.widthIn(max = ContentMaxWidth))
                    results(state, onEdit, expanded, Modifier.widthIn(max = ContentMaxWidth))
                }
            }
        }
    }
}

/**
 * Single-column only: while the inputs fill the screen the results are out of sight,
 * so keep the two numbers that matter pinned under the top bar, with a jump to the plan.
 */
@Composable
private fun SummaryStrip(state: PlannerUiState, listState: LazyListState) {
    val scope = rememberCoroutineScope()
    val summaryIndex = 2 // after the movie and method cards
    val visible by remember {
        derivedStateOf { listState.layoutInfo.visibleItemsInfo.none { it.key == SUMMARY_KEY || it.index > summaryIndex } }
    }
    AnimatedVisibility(visible = visible, enter = expandVertically(), exit = shrinkVertically()) {
        Surface(color = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    stringResource(Res.string.strip_summary, state.plan.totalRent.grouped(), state.plan.peakDemand.grouped()),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.weight(1f),
                )
                TextButton(onClick = { scope.launch { listState.animateScrollToItem(summaryIndex) } }) {
                    Text(stringResource(Res.string.action_view_plan))
                }
            }
        }
    }
}

private fun LazyListScope.inputs(state: PlannerUiState, onEdit: MovieEdit, itemModifier: Modifier = Modifier) {
    item(key = "movie") { MovieInputsCard(state.movie, onEdit, itemModifier) }
    item(key = "method") { MethodCard(state.movie, onEdit, itemModifier) }
}

private fun LazyListScope.results(
    state: PlannerUiState,
    onEdit: MovieEdit,
    expanded: ExpandedWeeks,
    itemModifier: Modifier = Modifier,
) {
    item(key = SUMMARY_KEY) { SummaryCard(state.plan, itemModifier) }
    item(key = "chart") { PlanChart(state.plan, itemModifier) }
    val (initial, extension) = state.plan.weeks.partition { it.phase == RunPhase.INITIAL }
    item(key = "initial-header") {
        GroupHeader(stringResource(Res.string.phase_initial), null, initial, expanded, itemModifier)
    }
    items(initial, key = { "week-${it.index}" }) { week -> week(state, week, onEdit, expanded, itemModifier) }
    if (extension.isNotEmpty()) {
        item(key = "extension-header") {
            GroupHeader(
                title = stringResource(Res.string.phase_extension, state.plan.weeks.size),
                supporting = stringResource(Res.string.phase_extension_hint),
                weeks = extension,
                expanded = expanded,
                modifier = itemModifier,
            )
        }
        items(extension, key = { "week-${it.index}" }) { week -> week(state, week, onEdit, expanded, itemModifier) }
    }
}

/** Group title, its rent subtotal, and one button that opens or closes every week of the group. */
@Composable
private fun GroupHeader(
    title: String,
    supporting: String?,
    weeks: List<WeekPlan>,
    expanded: ExpandedWeeks,
    modifier: Modifier,
) {
    val allOpen = weeks.all { it.index in expanded.indices }
    Row(
        modifier = modifier.fillMaxWidth().padding(top = 8.dp, start = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.semantics { heading() },
            )
            Text(
                stringResource(Res.string.group_total, weeks.sumOf { it.toRent }.grouped()),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            supporting?.let {
                Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        TextButton(onClick = { expanded.setAll(weeks, expanded = !allOpen) }) {
            Icon(
                if (allOpen) Icons.Outlined.UnfoldLess else Icons.Outlined.UnfoldMore,
                contentDescription = null,
                modifier = Modifier.padding(end = 6.dp),
            )
            Text(stringResource(if (allOpen) Res.string.action_collapse_all else Res.string.action_expand_all))
        }
    }
}

@Composable
private fun week(
    state: PlannerUiState,
    week: WeekPlan,
    onEdit: MovieEdit,
    expanded: ExpandedWeeks,
    modifier: Modifier,
) {
    WeekCard(
        week = week,
        expanded = week.index in expanded.indices,
        onToggle = { expanded.toggle(week.index) },
        defaultOwn = state.movie.numberOfScreenings,
        onOwnOverride = { value ->
            onEdit { movie ->
                val overrides = if (value == null) movie.availableScreeningsOverrides - week.index
                else movie.availableScreeningsOverrides + (week.index to value)
                movie.copy(availableScreeningsOverrides = overrides)
            }
        },
        onActual = { actual ->
            onEdit { movie ->
                val actuals = if (actual == null) movie.actuals - week.index else movie.actuals + (week.index to actual)
                movie.copy(actuals = actuals)
            }
        },
        modifier = modifier,
    )
}
