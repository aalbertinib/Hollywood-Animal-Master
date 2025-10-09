package org.aalbertini.ham.ui.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.aalbertini.ham.ui.components.UiConstants

enum class WindowSizeClass {
    COMPACT,  // Phone portrait
    MEDIUM,   // Tablet, Phone landscape
    EXPANDED  // Desktop, Large tablet
}

@Composable
fun AdaptiveCalculatorLayout(
    windowSizeClass: WindowSizeClass,
    inputSection: @Composable () -> Unit,
    resultsSection: @Composable () -> Unit,
    savedSection: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    when (windowSizeClass) {
        WindowSizeClass.COMPACT -> {
            // Single column for phones - with scrolling and indicator
            val scrollState = rememberScrollState()
            Box(modifier = modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(UiConstants.Layout.compactPadding)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(UiConstants.Layout.mediumSpacing)
                ) {
                    inputSection()
                    resultsSection()
                    savedSection()
                }
            }
        }
        WindowSizeClass.MEDIUM -> {
            // Two column for tablets - with scrolling and scrollbars
            val scrollState1 = rememberScrollState()
            val scrollState2 = rememberScrollState()
            Row(
                modifier = modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(UiConstants.Layout.mediumSpacing)
            ) {
                Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(UiConstants.Layout.compactPadding)
                            .verticalScroll(scrollState1),
                        verticalArrangement = Arrangement.spacedBy(UiConstants.Layout.mediumSpacing)
                    ) {
                        inputSection()
                        savedSection()
                    }
                }
                Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(UiConstants.Layout.compactPadding)
                            .verticalScroll(scrollState2),
                        verticalArrangement = Arrangement.spacedBy(UiConstants.Layout.mediumSpacing)
                    ) {
                        resultsSection()
                    }
                }
            }
        }
        WindowSizeClass.EXPANDED -> {
            // Three column layout for desktop - with scrolling and scrollbars
            val scrollState1 = rememberScrollState()
            val scrollState2 = rememberScrollState()
            val scrollState3 = rememberScrollState()
            Row(
                modifier = modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(UiConstants.Layout.mediumSpacing)
            ) {
                Box(modifier = Modifier.width(UiConstants.Layout.expandedColumnWidth).fillMaxHeight()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(UiConstants.Layout.compactPadding)
                            .verticalScroll(scrollState1)
                    ) {
                        inputSection()
                    }
                }
                Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(UiConstants.Layout.compactPadding)
                            .verticalScroll(scrollState2),
                        verticalArrangement = Arrangement.spacedBy(UiConstants.Layout.mediumSpacing)
                    ) {
                        resultsSection()
                    }
                }
                Box(modifier = Modifier.width(UiConstants.Layout.expandedColumnWidth).fillMaxHeight()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(UiConstants.Layout.compactPadding)
                            .verticalScroll(scrollState3)
                    ) {
                        savedSection()
                    }
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


