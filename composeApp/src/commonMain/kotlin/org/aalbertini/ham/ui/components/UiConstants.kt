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
    
    // Section header dimensions
    object SectionHeader {
        val tonalElevation = 6.dp
        val shadowElevation = 4.dp
        val horizontalPadding = 16.dp
        val verticalPadding = 12.dp
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
        val desktopWidth = 1200.dp
        val desktopHeight = 800.dp
    }
    
    // Layout constants
    object Layout {
        val expandedColumnWidth = 400.dp
        val compactPadding = 4.dp
        val mediumSpacing = 16.dp
    }
}
