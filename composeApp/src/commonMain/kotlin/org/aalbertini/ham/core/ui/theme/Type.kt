package org.aalbertini.ham.core.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Material 3 type scale with tabular figures on the styles that show screening counts,
 * so week columns line up digit for digit.
 */
private val Baseline = Typography()

private fun TextStyle.tabular() = copy(fontFeatureSettings = "tnum")

val HamTypography = Typography(
    displayLarge = Baseline.displayLarge.tabular(),
    displayMedium = Baseline.displayMedium.tabular(),
    displaySmall = Baseline.displaySmall.copy(fontWeight = FontWeight.SemiBold).tabular(),
    headlineLarge = Baseline.headlineLarge.copy(fontWeight = FontWeight.SemiBold).tabular(),
    headlineMedium = Baseline.headlineMedium.copy(fontWeight = FontWeight.SemiBold).tabular(),
    headlineSmall = Baseline.headlineSmall.copy(fontWeight = FontWeight.SemiBold).tabular(),
    titleLarge = Baseline.titleLarge.copy(fontWeight = FontWeight.SemiBold),
    titleMedium = Baseline.titleMedium.copy(fontWeight = FontWeight.SemiBold).tabular(),
    titleSmall = Baseline.titleSmall.tabular(),
    bodyLarge = Baseline.bodyLarge,
    bodyMedium = Baseline.bodyMedium.tabular(),
    bodySmall = Baseline.bodySmall.tabular(),
    labelLarge = Baseline.labelLarge.tabular(),
    labelMedium = Baseline.labelMedium.tabular(),
    labelSmall = Baseline.labelSmall.tabular(),
)

/** M3 shape scale (4 / 8 / 12 / 16 / 28 dp). */
val HamShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(28.dp),
)
