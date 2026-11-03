package org.aalbertini.ham.features.movie_distribution.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import hollywoodanimalmaster.composeapp.generated.resources.Res
import hollywoodanimalmaster.composeapp.generated.resources.chart_caption
import hollywoodanimalmaster.composeapp.generated.resources.chart_description
import hollywoodanimalmaster.composeapp.generated.resources.chart_hint
import hollywoodanimalmaster.composeapp.generated.resources.chart_title
import hollywoodanimalmaster.composeapp.generated.resources.legend_own
import hollywoodanimalmaster.composeapp.generated.resources.legend_own_capacity
import hollywoodanimalmaster.composeapp.generated.resources.legend_rent
import org.aalbertini.ham.core.ui.components.SectionCard
import org.aalbertini.ham.core.util.format.grouped
import org.aalbertini.ham.features.movie_distribution.domain.model.DistributionPlan
import org.aalbertini.ham.features.movie_distribution.domain.model.WeekPlan
import org.jetbrains.compose.resources.stringResource

/**
 * Stacked bars per week: screenings covered by your own cinemas (neutral, at the baseline)
 * and screenings to rent (primary, on top). The week cards below are the table view.
 * Hover (desktop/web) or tap (touch) selects a week and shows its numbers in the caption.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PlanChart(plan: DistributionPlan, modifier: Modifier = Modifier) {
    val ownColor = MaterialTheme.colorScheme.outline
    val rentColor = MaterialTheme.colorScheme.primary
    val gapColor = MaterialTheme.colorScheme.surfaceContainerLow
    val baselineColor = MaterialTheme.colorScheme.outlineVariant
    val selectionColor = MaterialTheme.colorScheme.onSurface
    val capacityColor = MaterialTheme.colorScheme.onSurfaceVariant
    var selected by remember(plan.weeks.size) { mutableStateOf<Int?>(null) }

    SectionCard(title = stringResource(Res.string.chart_title), modifier = modifier) {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            LegendItem(rentColor, stringResource(Res.string.legend_rent))
            LegendItem(ownColor, stringResource(Res.string.legend_own))
            CapacityLegendItem(capacityColor, stringResource(Res.string.legend_own_capacity))
        }
        val description = stringResource(
            Res.string.chart_description,
            plan.weeks.size,
            plan.peakDemand.grouped(),
            plan.peakWeek?.number ?: 1,
        )
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(168.dp)
                .semantics { contentDescription = description }
                .pointerInput(plan.weeks.size) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            val position = event.changes.firstOrNull()?.position ?: continue
                            selected = when (event.type) {
                                PointerEventType.Exit -> null
                                else -> {
                                    val slot = size.width / plan.weeks.size.coerceAtLeast(1)
                                    (position.x / slot).toInt().coerceIn(0, plan.weeks.lastIndex)
                                }
                            }
                        }
                    }
                },
        ) {
            drawBars(plan.weeks, ownColor, rentColor, gapColor, baselineColor, selectionColor, capacityColor, selected)
        }
        Row(Modifier.fillMaxWidth()) {
            plan.weeks.forEach { week ->
                Text(
                    text = "${week.number}",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (week.index == selected) FontWeight.Bold else FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        val week = selected?.let { plan.weeks.getOrNull(it) }
        Text(
            text = if (week != null) {
                stringResource(
                    Res.string.chart_caption,
                    week.number,
                    week.demand.grouped(),
                    week.ownScreenings.grouped(),
                    week.toRent.grouped(),
                )
            } else {
                stringResource(Res.string.chart_hint)
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

private fun DrawScope.drawBars(
    weeks: List<WeekPlan>,
    ownColor: Color,
    rentColor: Color,
    gapColor: Color,
    baselineColor: Color,
    selectionColor: Color,
    capacityColor: Color,
    selected: Int?,
) {
    if (weeks.isEmpty()) return
    val gap = 2.dp.toPx()
    val radius = 4.dp.toPx()
    val slot = size.width / weeks.size
    val barWidth = (slot * 0.56f).coerceAtMost(40.dp.toPx())
    val tallestBar = weeks.maxOf { ownPart(it) + it.toRent }.coerceAtLeast(1)
    // Show own capacity above the bars, but don't let a huge cinema chain flatten them.
    val max = maxOf(tallestBar, minOf(weeks.maxOf { it.ownScreenings }, tallestBar * 3 / 2))
    val scale = (size.height - gap) / max
    val dash = PathEffect.dashPathEffect(floatArrayOf(4.dp.toPx(), 3.dp.toPx()))

    weeks.forEachIndexed { index, week ->
        val left = index * slot + (slot - barWidth) / 2
        val ownHeight = ownPart(week) * scale
        val rentHeight = week.toRent * scale
        var top = size.height
        if (ownHeight > 0f) {
            top -= ownHeight
            drawTopRounded(ownColor, left, top, barWidth, ownHeight, if (rentHeight > 0f) 0f else radius)
        }
        if (rentHeight > 0f) {
            if (ownHeight > 0f) {
                // 2 dp surface gap between stacked segments.
                drawRect(gapColor, Offset(left, top - gap), Size(barWidth, gap))
                top -= gap
            }
            top -= rentHeight
            drawTopRounded(rentColor, left, top, barWidth, rentHeight, radius)
        }
        if (week.ownScreenings in 1..max) {
            val y = size.height - week.ownScreenings * scale
            drawLine(
                color = capacityColor,
                start = Offset(index * slot + slot * 0.08f, y),
                end = Offset((index + 1) * slot - slot * 0.08f, y),
                strokeWidth = 1.5.dp.toPx(),
                pathEffect = dash,
            )
        }
        if (index == selected) {
            val pad = 3.dp.toPx()
            drawRoundRect(
                color = selectionColor,
                topLeft = Offset(left - pad, top - pad),
                size = Size(barWidth + pad * 2, size.height - top + pad),
                cornerRadius = CornerRadius(radius + pad),
                style = Stroke(width = 1.5.dp.toPx()),
            )
        }
    }
    drawLine(baselineColor, Offset(0f, size.height), Offset(size.width, size.height), strokeWidth = 1.dp.toPx())
}

/** Part of the bar covered by own cinemas: never more than the audience fills. */
private fun ownPart(week: WeekPlan): Long = minOf(week.ownScreenings, week.demand)

private fun DrawScope.drawTopRounded(color: Color, left: Float, top: Float, width: Float, height: Float, radius: Float) {
    val r = minOf(radius, height, width / 2)
    val path = Path().apply {
        addRoundRect(
            RoundRect(
                left = left, top = top, right = left + width, bottom = top + height,
                topLeftCornerRadius = CornerRadius(r), topRightCornerRadius = CornerRadius(r),
                bottomRightCornerRadius = CornerRadius.Zero, bottomLeftCornerRadius = CornerRadius.Zero,
            )
        )
    }
    drawPath(path, color)
}

@Composable
private fun CapacityLegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Canvas(Modifier.size(width = 16.dp, height = 12.dp)) {
            drawLine(
                color = color,
                start = Offset(0f, size.height / 2),
                end = Offset(size.width, size.height / 2),
                strokeWidth = 1.5.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(4.dp.toPx(), 3.dp.toPx())),
            )
        }
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(Modifier.size(12.dp).clip(RoundedCornerShape(3.dp)).background(color))
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
