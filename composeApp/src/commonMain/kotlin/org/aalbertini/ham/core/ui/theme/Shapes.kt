package org.aalbertini.ham.core.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp
import org.aalbertini.ham.core.ui.resources.Dimensions

/**
 * Shape definitions for Hollywood Animal Master.
 * 
 * Follows Material Design 3 shape system with rounded corners.
 * Uses dimensions from Dimensions.kt for consistency.
 */
val Shapes = Shapes(
    extraSmall = RoundedCornerShape(Dimensions.cornerRadiusSmall),
    small = RoundedCornerShape(Dimensions.cornerRadiusMedium),
    medium = RoundedCornerShape(Dimensions.cornerRadiusLarge),
    large = RoundedCornerShape(Dimensions.cornerRadiusXLarge),
    extraLarge = RoundedCornerShape(28.dp)
)

object CustomShapes {
    val SectionContentShape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = Dimensions.cornerRadiusLarge,
        bottomEnd = Dimensions.cornerRadiusLarge
    )
}