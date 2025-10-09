package org.aalbertini.ham.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import org.aalbertini.ham.CalculationConstants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsSection(
    results: List<Long>,
    expanded: Boolean,
    onToggleExpand: () -> Unit,
    onCopyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(UiConstants.Card.padding)
            .border(
                width = UiConstants.Card.borderWidth,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(UiConstants.Card.cornerRadiusLarge)
            )
            .animateContentSize(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(UiConstants.Card.cornerRadiusLarge)
    ) {
        Column {
            // Sticky header with elevated surface
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primaryContainer,
                tonalElevation = UiConstants.SectionHeader.tonalElevation,
                shadowElevation = UiConstants.SectionHeader.shadowElevation
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = UiConstants.SectionHeader.horizontalPadding, vertical = UiConstants.SectionHeader.verticalPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = UiStrings.SECTION_RESULTS,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f),
                        softWrap = true,
                        maxLines = 2
                    )
                    if (results.isNotEmpty()) {
                        TooltipBox(
                            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                            tooltip = { PlainTooltip { Text(UiStrings.ACTION_COPY_RESULTS) } },
                            state = rememberTooltipState()
                        ) {
                            IconButton(onClick = onCopyClick) {
                                Icon(
                                    Icons.Filled.ContentCopy,
                                    contentDescription = UiStrings.ACTION_COPY_RESULTS
                                )
                            }
                        }
                    }
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                        tooltip = { PlainTooltip { Text(if (expanded) UiStrings.ACTION_COLLAPSE else UiStrings.ACTION_EXPAND) } },
                        state = rememberTooltipState()
                    ) {
                        IconButton(onClick = onToggleExpand) {
                            Icon(
                                if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                                contentDescription = if (expanded) UiStrings.CONTENT_DESC_COLLAPSE else UiStrings.CONTENT_DESC_EXPAND
                            )
                        }
                    }
                }
            }
            
            AnimatedVisibility(visible = expanded) {
                AnimatedContent(
                    targetState = results,
                    label = "results",
                    transitionSpec = { fadeIn() togetherWith fadeOut() }
                ) { list ->
                    Column(modifier = Modifier.padding(UiConstants.Padding.contentStandard)) {
                        if (list.isEmpty()) {
                            Text(
                                text = UiStrings.messageEnterValidParameters(),
                                style = MaterialTheme.typography.bodyMedium,
                                softWrap = true
                            )
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(UiConstants.Spacing.betweenElements)) {
                            for (i in 1..CalculationConstants.WeeklyCalculation.NUMBER_OF_WEEKS) {
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = UiStrings.weekNumber(i),
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.weight(1f)
                                    )
                                    val value = list[i - 1]
                                    Text(
                                        text = UiStrings.formatNumber(value),
                                        style = MaterialTheme.typography.bodyLarge,
                                        textAlign = TextAlign.End,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                if (i < CalculationConstants.WeeklyCalculation.NUMBER_OF_WEEKS) HorizontalDivider()
                            }
                        }
                        }
                    }
                }
            }
        }
    }
}
