package org.aalbertini.ham.core.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp
import org.aalbertini.ham.core.ui.resources.Dimensions

/**
 * Modern shape definitions for Hollywood Animal Master.
 * 
 * Follows Material Design 3 shape system with enhanced rounded corners
 * for a modern dashboard aesthetic.
 * 
 * Features:
 * - Larger corner radii for softer, more modern appearance
 * - Consistent with dashboard design patterns
 * - Optimized for card-based layouts
 */
val Shapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

/**
 * Custom shapes for specific UI components.
 */
object CustomShapes {
    /**
     * Shape for section content areas - rounded bottom corners only
     */
    val SectionContentShape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = 16.dp,
        bottomEnd = 16.dp
    )
    
    /**
     * Shape for metric cards - fully rounded for modern look
     */
    val MetricCardShape = RoundedCornerShape(16.dp)
    
    /**
     * Shape for elevated cards with more prominent rounding
     */
    val ElevatedCardShape = RoundedCornerShape(20.dp)
    
    /**
     * Shape for compact list items
     */
    val ListItemShape = RoundedCornerShape(12.dp)
    
    /**
     * Shape for dialogs and modals
     */
    val DialogShape = RoundedCornerShape(24.dp)
}