package org.aalbertini.ham.resources

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Centralized dimension values for the Hollywood Animals Master application.
 * 
 * Following Material Design 3 spacing system with 4dp base unit.
 * All dimensions are immutable and thread-safe.
 * 
 * Naming convention:
 * - spacing: General spacing between elements
 * - padding: Internal padding within components
 * - size: Fixed sizes for specific elements
 * - elevation: Shadow elevation levels
 * 
 * Best practices:
 * - Use spacing scale consistently (4dp, 8dp, 12dp, 16dp, etc.)
 * - Prefer semantic names over raw values in UI code
 * - Consider responsive design for different screen sizes
 */
object Dimensions {
    
    // Spacing Scale (Material Design 3)
    val spacing0: Dp = 0.dp
    val spacing1: Dp = 4.dp
    val spacing2: Dp = 8.dp
    val spacing3: Dp = 12.dp
    val spacing4: Dp = 16.dp
    val spacing5: Dp = 20.dp
    val spacing6: Dp = 24.dp
    val spacing7: Dp = 28.dp
    val spacing8: Dp = 32.dp
    val spacing10: Dp = 40.dp
    val spacing12: Dp = 48.dp
    val spacing16: Dp = 64.dp
    val spacing20: Dp = 80.dp
    val spacing24: Dp = 96.dp
    
    /**
     * Spacing values for margins and gaps between elements
     */
    object Spacing {
        val none: Dp = spacing0
        val extraSmall: Dp = spacing1
        val small: Dp = spacing2
        val medium: Dp = spacing4
        val large: Dp = spacing6
        val extraLarge: Dp = spacing8
        val huge: Dp = spacing10
    }
    
    /**
     * Padding values for internal component spacing
     */
    object Padding {
        val none: Dp = spacing0
        val extraSmall: Dp = spacing1
        val small: Dp = spacing2
        val medium: Dp = spacing4
        val large: Dp = spacing6
        val extraLarge: Dp = spacing8
        val huge: Dp = spacing10
        val contentStandard: Dp = spacing4
    }
    
    // Common Padding (deprecated, use Padding object)
    val paddingXSmall: Dp = spacing1
    val paddingSmall: Dp = spacing2
    val paddingMedium: Dp = spacing4
    val paddingLarge: Dp = spacing6
    val paddingXLarge: Dp = spacing8
    
    // Screen Edge Padding
    val screenPaddingHorizontal: Dp = spacing4
    val screenPaddingVertical: Dp = spacing6
    
    // Card Padding
    val cardPadding: Dp = spacing4
    val cardElevation: Dp = 2.dp
    
    // Button Dimensions
    val buttonHeight: Dp = 48.dp
    val buttonPaddingHorizontal: Dp = spacing6
    val buttonPaddingVertical: Dp = spacing3
    val buttonCornerRadius: Dp = 12.dp
    
    // Text Field Dimensions
    val textFieldHeight: Dp = 56.dp
    val textFieldCornerRadius: Dp = 8.dp
    
    // Icon Sizes
    val iconSizeSmall: Dp = 16.dp
    val iconSizeMedium: Dp = 24.dp
    val iconSizeLarge: Dp = 32.dp
    val iconSizeXLarge: Dp = 48.dp
    
    // Divider
    val dividerThickness: Dp = 1.dp
    
    // Corner Radius
    val cornerRadiusSmall: Dp = 4.dp
    val cornerRadiusMedium: Dp = 8.dp
    val cornerRadiusLarge: Dp = 12.dp
    val cornerRadiusXLarge: Dp = 16.dp
    
    // Elevation Levels
    val elevationLevel0: Dp = 0.dp
    val elevationLevel1: Dp = 1.dp
    val elevationLevel2: Dp = 3.dp
    val elevationLevel3: Dp = 6.dp
    val elevationLevel4: Dp = 8.dp
    val elevationLevel5: Dp = 12.dp
    
    // Chart & Graph Dimensions
    val chartHeight: Dp = 240.dp
    val chartBarWidth: Dp = 32.dp
    val chartBarSpacing: Dp = spacing2
    
    // List Item Dimensions
    val listItemHeight: Dp = 72.dp
    val listItemIconSize: Dp = iconSizeLarge
    
    // Bottom Navigation
    val bottomNavHeight: Dp = 80.dp
    
    // Top App Bar
    val topAppBarHeight: Dp = 64.dp
    
    // Modal / Dialog
    val dialogMinWidth: Dp = 280.dp
    val dialogMaxWidth: Dp = 560.dp
    val dialogCornerRadius: Dp = cornerRadiusLarge
    
    // Progress Indicators
    val progressIndicatorSize: Dp = 48.dp
    val progressIndicatorStroke: Dp = 4.dp
    
    // Borders
    val borderWidthThin: Dp = 1.dp
    val borderWidthMedium: Dp = 2.dp
    val borderWidthThick: Dp = 4.dp
}
