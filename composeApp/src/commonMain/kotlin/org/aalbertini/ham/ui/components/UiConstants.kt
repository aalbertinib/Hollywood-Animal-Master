package org.aalbertini.ham.ui.components

import androidx.compose.ui.unit.dp

/**
 * UI dimension and display constants.
 * 
 * These constants are for UI layout, spacing, and display purposes only.
 * For business logic constants (validation, calculations), see CalculationConstants.kt
 */
object UiConstants {
    // Card dimensions
    object Card {
        val padding = 6.dp
        val borderWidth = 1.dp
        val cornerRadiusLarge = 12.dp
        val cornerRadiusSmall = 8.dp
        val elevationDefault = 2.dp
        val elevationDialog = 8.dp
    }

    object Dialog {
        val defaultDialogWidth = 600.dp
        val defaultDialogHeight = 800.dp
    }
    
    // Section header dimensions
    object SectionHeader {
        val tonalElevation = 6.dp
        val shadowElevation = 4.dp
        val horizontalPadding = 16.dp
        val verticalPadding = 12.dp
        val minHeight = 56.dp
    }
    
    // Content padding
    object Padding {
        val contentStandard = 16.dp
        val contentSmall = 8.dp
        val contentTiny = 4.dp
        val itemInCard = 12.dp
    }
    
    // Spacing
    object Spacing {
        val betweenSections = 16.dp
        val betweenFields = 8.dp
        val betweenElements = 8.dp
    }
    
    // Window/Layout dimensions
    object Window {
        val desktopWidth = 1280.dp
        val desktopHeight = 720.dp
    }
    
    // Layout constants
    object Layout {
        val minSectionWidth = 300.dp  // Minimum width for each section in adaptive layout
        val expandedColumnWidth = 400.dp  // Maximum width for fixed-width columns (input/saved)
        val compactPadding = 4.dp
        val mediumSpacing = 16.dp
    }
    
    // Animation durations
    object Animation {
        const val sectionExpandCollapseDurationMs = 150
        const val errorMessageDurationMs = 150
        const val contentSizeDurationMs = 200
        const val headerCornerRadiusDurationMs = 200
        const val headerCornerRadiusDelayMs = 150  // Delay for collapse animation
    }
}
