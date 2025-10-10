package org.aalbertini.ham.ui.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.aalbertini.ham.ui.components.UiConstants

enum class WindowSizeClass {
    COMPACT,  // Phone portrait
    MEDIUM,   // Tablet, Phone landscape
    EXPANDED  // Desktop, Large tablet
}

/**
 * Adaptive layout for movie distribution screen with responsive column management.
 *
 * Layout Strategy:
 * - Each section has a minimum width of 300.dp to ensure readability
 * - Automatically switches between 1, 2, or 3 columns based on available space
 * - Uses BoxWithConstraints to measure actual available width
 *
 * Layout Modes:
 * 1. Single Column (< 616.dp): All sections stacked vertically
 * 2. Two Column (616-932.dp): Input+Saved on left, Results on right
 * 3. Three Column (>= 932.dp): Input (max 400dp) | Results (min 300dp) | Saved (max 400dp)
 *
 * @param windowSizeClass Initial size class hint (COMPACT, MEDIUM, EXPANDED)
 * @param inputSection Parameters input composable
 * @param resultsSection Results display composable
 * @param savedSection Saved results composable
 * @param modifier Optional modifier for the layout
 */
@Composable
fun AdaptiveMovieDistributionLayout(
    windowSizeClass: WindowSizeClass,
    parametersHeader: @Composable () -> Unit,
    parametersContent: @Composable () -> Unit,
    resultsHeader: @Composable () -> Unit,
    resultsContent: @Composable () -> Unit,
    savedHeader: @Composable () -> Unit,
    savedContent: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        // Layout calculation constants
        val minSectionWidth = UiConstants.Layout.minSectionWidth
        val spacing = UiConstants.Layout.mediumSpacing
        val totalSpacing = spacing * 2  // For 3 columns
        
        // Determine actual layout based on available width
        val canFitThreeColumns by remember(maxWidth, windowSizeClass) {
            derivedStateOf {
                windowSizeClass == WindowSizeClass.EXPANDED && 
                maxWidth >= (minSectionWidth * 3 + totalSpacing)
            }
        }
        
        val canFitTwoColumns by remember(maxWidth, windowSizeClass) {
            derivedStateOf {
                windowSizeClass != WindowSizeClass.COMPACT && 
                maxWidth >= (minSectionWidth * 2 + spacing)
            }
        }
        
        when {
            canFitThreeColumns -> {
                // Three column layout for desktop
                ThreeColumnLayout(
                    parametersHeader = parametersHeader,
                    parametersContent = parametersContent,
                    resultsHeader = resultsHeader,
                    resultsContent = resultsContent,
                    savedHeader = savedHeader,
                    savedContent = savedContent
                )
            }
            canFitTwoColumns -> {
                // Two column layout for tablets
                TwoColumnLayout(
                    parametersHeader = parametersHeader,
                    parametersContent = parametersContent,
                    resultsHeader = resultsHeader,
                    resultsContent = resultsContent,
                    savedHeader = savedHeader,
                    savedContent = savedContent
                )
            }
            else -> {
                // Single column for phones
                SingleColumnLayout(
                    parametersHeader = parametersHeader,
                    parametersContent = parametersContent,
                    resultsHeader = resultsHeader,
                    resultsContent = resultsContent,
                    savedHeader = savedHeader,
                    savedContent = savedContent
                )
            }
        }
    }
}

@Composable
private fun SingleColumnLayout(
    parametersHeader: @Composable () -> Unit,
    parametersContent: @Composable () -> Unit,
    resultsHeader: @Composable () -> Unit,
    resultsContent: @Composable () -> Unit,
    savedHeader: @Composable () -> Unit,
    savedContent: @Composable () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(UiConstants.Layout.compactPadding)
    ) {
        stickyHeader { parametersHeader() }
        item { 
            parametersContent()
            Spacer(modifier = Modifier.height(UiConstants.Layout.mediumSpacing))
        }
        stickyHeader { savedHeader() }
        item { 
            savedContent()
            Spacer(modifier = Modifier.height(UiConstants.Layout.mediumSpacing))
        }
        stickyHeader { resultsHeader() }
        item { resultsContent() }
    }
}

@Composable
private fun TwoColumnLayout(
    parametersHeader: @Composable () -> Unit,
    parametersContent: @Composable () -> Unit,
    resultsHeader: @Composable () -> Unit,
    resultsContent: @Composable () -> Unit,
    savedHeader: @Composable () -> Unit,
    savedContent: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(UiConstants.Layout.mediumSpacing)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .widthIn(min = UiConstants.Layout.minSectionWidth)
                .fillMaxHeight()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(UiConstants.Layout.compactPadding)
            ) {
                stickyHeader { parametersHeader() }
                item { 
                    parametersContent()
                    Spacer(modifier = Modifier.height(UiConstants.Layout.mediumSpacing))
                }
                stickyHeader { savedHeader() }
                item { savedContent() }
            }
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .widthIn(min = UiConstants.Layout.minSectionWidth)
                .fillMaxHeight()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(UiConstants.Layout.compactPadding)
            ) {
                stickyHeader { resultsHeader() }
                item { resultsContent() }
            }
        }
    }
}

@Composable
private fun ThreeColumnLayout(
    parametersHeader: @Composable () -> Unit,
    parametersContent: @Composable () -> Unit,
    resultsHeader: @Composable () -> Unit,
    resultsContent: @Composable () -> Unit,
    savedHeader: @Composable () -> Unit,
    savedContent: @Composable () -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        // Calculate widths explicitly to ensure minimums are respected
        val spacing = UiConstants.Layout.mediumSpacing
        val minWidth = UiConstants.Layout.minSectionWidth
        val maxFixedWidth = UiConstants.Layout.expandedColumnWidth
        
        // Input and Saved sections: prefer maxFixedWidth, but respect minWidth
        val inputWidth by remember(maxWidth) {
            derivedStateOf {
                minOf(maxFixedWidth, maxOf(minWidth, maxWidth / 4))
            }
        }
        val savedWidth by remember(maxWidth) {
            derivedStateOf {
                minOf(maxFixedWidth, maxOf(minWidth, maxWidth / 4))
            }
        }
        
        // Results section: takes remaining space, but never less than minWidth
        val resultsWidth by remember(maxWidth, inputWidth, savedWidth) {
            derivedStateOf {
                maxOf(minWidth, maxWidth - inputWidth - savedWidth - spacing * 2)
            }
        }
        
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            // Input + Saved sections (left column)
            Box(
                modifier = Modifier
                    .width(inputWidth)
                    .fillMaxHeight()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(UiConstants.Layout.compactPadding)
                ) {
                    stickyHeader { parametersHeader() }
                    item { 
                        parametersContent()
                        Spacer(modifier = Modifier.height(UiConstants.Layout.mediumSpacing))
                    }
                    stickyHeader { savedHeader() }
                    item { savedContent() }
                }
            }
            // Results section (right column - takes remaining space)
            Box(
                modifier = Modifier
                    .width(resultsWidth + savedWidth + spacing)
                    .fillMaxHeight()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(UiConstants.Layout.compactPadding)
                ) {
                    stickyHeader { resultsHeader() }
                    item { resultsContent() }
                }
            }
        }
    }
}

/**
 * Determine window size class based on width
 */
@Composable
expect fun rememberWindowSizeClass(): WindowSizeClass


