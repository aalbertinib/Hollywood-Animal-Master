package org.aalbertini.ham.features.movie_distribution.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import hollywoodanimalmaster.composeapp.generated.resources.Res
import hollywoodanimalmaster.composeapp.generated.resources.compare_open
import hollywoodanimalmaster.composeapp.generated.resources.compare_overview
import hollywoodanimalmaster.composeapp.generated.resources.compare_reference
import hollywoodanimalmaster.composeapp.generated.resources.compare_row_after_week_four
import hollywoodanimalmaster.composeapp.generated.resources.compare_row_method
import hollywoodanimalmaster.composeapp.generated.resources.compare_row_own
import hollywoodanimalmaster.composeapp.generated.resources.compare_row_peak
import hollywoodanimalmaster.composeapp.generated.resources.compare_row_ran
import hollywoodanimalmaster.composeapp.generated.resources.compare_row_run
import hollywoodanimalmaster.composeapp.generated.resources.compare_row_scores
import hollywoodanimalmaster.composeapp.generated.resources.compare_row_total
import hollywoodanimalmaster.composeapp.generated.resources.compare_tip
import hollywoodanimalmaster.composeapp.generated.resources.compare_weeks
import hollywoodanimalmaster.composeapp.generated.resources.run_weeks
import hollywoodanimalmaster.composeapp.generated.resources.untitled_movie
import hollywoodanimalmaster.composeapp.generated.resources.week_label
import org.aalbertini.ham.core.ui.components.SectionCard
import org.aalbertini.ham.core.util.format.grouped
import org.aalbertini.ham.core.util.format.signedGrouped
import org.aalbertini.ham.core.util.format.toPlainString
import org.aalbertini.ham.features.movie_distribution.domain.calculator.MovieComparison
import org.aalbertini.ham.features.movie_distribution.domain.model.DistributionDefaults
import org.aalbertini.ham.features.movie_distribution.domain.model.MovieResult
import org.aalbertini.ham.features.movie_distribution.presentation.components.Tag
import org.aalbertini.ham.features.movie_distribution.presentation.components.titleRes
import org.jetbrains.compose.resources.stringResource

private val LabelWidth = 120.dp
private val CompactLabelWidth = 96.dp
private val MinColumnWidth = 112.dp
private val ContentMaxWidth = 960.dp

/** Shown for a week past the end of a shorter run. */
private const val NO_VALUE = "–"

/**
 * Two to four saved movies side by side: inputs and totals, then what to rent each week.
 * Differences are measured against the first movie. On narrow screens the tables scroll sideways.
 */
@Composable
fun CompareScreen(movies: List<MovieResult>, onOpen: (String) -> Unit, modifier: Modifier = Modifier) {
    val comparison = remember(movies) { MovieComparison(movies) }
    // One sideways position for both tables, so a column is the same movie in each card.
    val scroll = rememberScrollState()
    val itemModifier = Modifier.widthIn(max = ContentMaxWidth)
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item(key = "overview") {
            SectionCard(stringResource(Res.string.compare_overview), itemModifier) {
                CompareTable(comparison, scroll, onOpen) { layout -> OverviewRows(comparison, layout) }
            }
        }
        item(key = "weeks") {
            SectionCard(stringResource(Res.string.compare_weeks), itemModifier) {
                CompareTable(comparison, scroll, onOpen) { layout -> WeekRows(comparison, layout) }
            }
        }
        item(key = "tip") {
            Text(
                stringResource(Res.string.compare_tip),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = itemModifier.fillMaxWidth().padding(horizontal = 4.dp),
            )
        }
    }
}

/**
 * Geometry shared by every row of one table. The label column stays put; the movie columns
 * scroll sideways together (one [scroll] state for all rows) when they don't fit.
 */
private class TableLayout(val labelWidth: Dp, val columnWidth: Dp, val scroll: ScrollState)

/**
 * Label column plus one column per movie. Columns share the width when it fits and keep
 * [MinColumnWidth] otherwise.
 */
@Composable
private fun CompareTable(
    comparison: MovieComparison,
    scroll: ScrollState,
    onOpen: (String) -> Unit,
    rows: @Composable (TableLayout) -> Unit,
) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val count = comparison.movies.size.coerceAtLeast(1)
        val labelWidth = if (maxWidth < 480.dp) CompactLabelWidth else LabelWidth
        // Whole pixels: a fractional dp width rounds up and leaves a 1 px scroll when columns fit.
        val density = LocalDensity.current
        val fitted = with(density) { ((constraints.maxWidth - labelWidth.roundToPx()) / count).toDp() }
        val layout = TableLayout(labelWidth, maxOf(MinColumnWidth, fitted), scroll)
        Column {
            HeaderRow(comparison.movies, layout, onOpen)
            HorizontalDivider()
            rows(layout)
        }
    }
}

@Composable
private fun HeaderRow(movies: List<MovieResult>, layout: TableLayout, onOpen: (String) -> Unit) {
    TableRow(label = "", layout = layout) {
        movies.forEachIndexed { index, movie ->
            val title = movie.title.ifBlank { stringResource(Res.string.untitled_movie) }
            Column(
                modifier = Modifier
                    .width(layout.columnWidth)
                    .clip(MaterialTheme.shapes.small)
                    .clickable(onClickLabel = stringResource(Res.string.compare_open, title), role = Role.Button) {
                        onOpen(movie.id)
                    }
                    .padding(horizontal = 8.dp, vertical = 2.dp),
                horizontalAlignment = Alignment.End,
            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.End,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.semantics { heading() },
                )
                // Differences below are measured against this one.
                if (index == 0) Tag(stringResource(Res.string.compare_reference), Modifier.padding(top = 4.dp))
            }
        }
    }
}

@Composable
private fun OverviewRows(comparison: MovieComparison, layout: TableLayout) {
    val columnWidth = layout.columnWidth
    val movies = comparison.movies
    TableRow(stringResource(Res.string.compare_row_method), layout) {
        movies.forEach { Cell(stringResource(it.method.titleRes), columnWidth) }
    }
    TableRow(stringResource(Res.string.compare_row_scores), layout) {
        movies.forEach { movie ->
            val artistic = movie.artisticScore?.toPlainString(1) ?: NO_VALUE
            Cell("${movie.commercialScore.toPlainString(1)} / $artistic", columnWidth)
        }
    }
    TableRow(stringResource(Res.string.compare_row_own), layout) {
        movies.forEach { Cell(it.numberOfScreenings.toLong().grouped(), columnWidth) }
    }
    TableRow(stringResource(Res.string.compare_row_run), layout) {
        movies.forEach { Cell(stringResource(Res.string.run_weeks, it.runWeeks), columnWidth) }
    }
    TableRow(stringResource(Res.string.compare_row_total), layout, emphasized = true) {
        movies.indices.forEach { i ->
            Cell(comparison.plans[i].totalRent.grouped(), columnWidth, delta = comparison.totalRentDelta(i).takeIf { i > 0 }, strong = true)
        }
    }
    TableRow(stringResource(Res.string.compare_row_peak), layout) {
        movies.indices.forEach { i ->
            Cell(comparison.plans[i].peakDemand.grouped(), columnWidth, delta = comparison.peakDelta(i).takeIf { i > 0 })
        }
    }
    TableRow(stringResource(Res.string.compare_row_after_week_four), layout) {
        comparison.plans.forEach { Cell(it.extensionRent.grouped(), columnWidth) }
    }
    TableRow(stringResource(Res.string.compare_row_ran), layout) {
        movies.indices.forEach { Cell(comparison.weeksRan(it).toString(), columnWidth) }
    }
}

@Composable
private fun WeekRows(comparison: MovieComparison, layout: TableLayout) {
    val columnWidth = layout.columnWidth
    val scale = comparison.maxWeeklyRent.coerceAtLeast(1)
    repeat(comparison.weekCount) { weekIndex ->
        // Weeks 5+ are a separate booking in the game: mark where they start.
        if (weekIndex == DistributionDefaults.INITIAL_RUN_WEEKS) {
            HorizontalDivider()
        }
        TableRow(stringResource(Res.string.week_label, weekIndex + 1), layout) {
            comparison.movies.indices.forEach { movie ->
                val week = comparison.week(movie, weekIndex)
                if (week == null) {
                    Cell(NO_VALUE, columnWidth, muted = true)
                } else {
                    BarCell(week.toRent, week.toRent.toFloat() / scale, columnWidth)
                }
            }
        }
    }
    HorizontalDivider()
    TableRow(stringResource(Res.string.compare_row_total), layout, emphasized = true) {
        comparison.plans.forEach { Cell(it.totalRent.grouped(), columnWidth, strong = true) }
    }
}

@Composable
private fun TableRow(
    label: String,
    layout: TableLayout,
    emphasized: Boolean = false,
    cells: @Composable RowScope.() -> Unit,
) {
    Row(Modifier.padding(vertical = 6.dp), verticalAlignment = Alignment.Top) {
        Text(
            label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = if (emphasized) FontWeight.SemiBold else null,
            color = if (emphasized) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(layout.labelWidth).padding(end = 8.dp, top = 2.dp),
        )
        Row(Modifier.weight(1f).horizontalScroll(layout.scroll), verticalAlignment = Alignment.Top, content = cells)
    }
}

/** Right-aligned value; [delta] is the difference from the first movie, hidden when 0. */
@Composable
private fun Cell(
    text: String,
    width: Dp,
    delta: Long? = null,
    strong: Boolean = false,
    muted: Boolean = false,
) {
    Column(Modifier.width(width).padding(horizontal = 8.dp), horizontalAlignment = Alignment.End) {
        Text(
            text,
            style = if (strong) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            color = if (muted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End,
        )
        if (delta != null && delta != 0L) {
            Text(
                delta.signedGrouped(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

/** Weekly rent with a thin bar; every bar in the table shares one scale. */
@Composable
private fun BarCell(rent: Long, fraction: Float, width: Dp) {
    Column(Modifier.width(width).padding(horizontal = 8.dp), horizontalAlignment = Alignment.End) {
        Text(
            rent.grouped(),
            style = MaterialTheme.typography.bodyMedium,
            color = if (rent > 0) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Box(
            Modifier
                .padding(top = 4.dp)
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerHighest),
        ) {
            if (fraction > 0f) {
                Box(
                    Modifier
                        .fillMaxWidth(fraction.coerceIn(0.02f, 1f))
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(MaterialTheme.colorScheme.primary),
                )
            }
        }
    }
}
