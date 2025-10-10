package org.aalbertini.ham.resources

import androidx.compose.ui.graphics.Color

/**
 * Centralized color palette for the Hollywood Animals Master application.
 * 
 * Following Material Design 3 principles with custom Hollywood-themed colors
 * inspired by the 1920s Art Deco aesthetic.
 * 
 * Color naming convention:
 * - Use descriptive names that indicate purpose (e.g., `primary`, `error`)
 * - Add context-specific variants (e.g., `hollywoodGold`, `artDecoBronze`)
 * - Provide light/dark theme variants where needed
 * 
 * Best practices:
 * - All colors are immutable and thread-safe
 * - Use semantic color names in your UI code (refer to theme, not raw colors)
 * - Define color roles in your MaterialTheme for proper theme support
 */
object Colors {
    
    // Hollywood Art Deco Theme Colors (from icon design)
    val hollywoodGoldLight = Color(0xFFF4D58D)
    val hollywoodGold = Color(0xFFD4AF37)
    val hollywoodGoldMedium = Color(0xFFB8860B)
    val hollywoodGoldDark = Color(0xFF8B6914)
    
    val artDecoBronzeLight = Color(0xFFCD7F32)
    val artDecoBronze = Color(0xFF8B5A2B)
    
    val hollywoodRed = Color(0xFFC41E3A)
    val hollywoodRedDark = Color(0xFF8B0000)
    
    val sepiaBrown = Color(0xFF2A1810)
    val sepiaBrownDark = Color(0xFF1A0F08)
    
    // Material 3 Primary Colors
    val primary = hollywoodGold
    val onPrimary = Color(0xFF2A1810)
    val primaryContainer = hollywoodGoldLight
    val onPrimaryContainer = Color(0xFF1A0F08)
    
    // Material 3 Secondary Colors
    val secondary = artDecoBronze
    val onSecondary = Color.White
    val secondaryContainer = artDecoBronzeLight
    val onSecondaryContainer = Color(0xFF1A0F08)
    
    // Material 3 Tertiary Colors
    val tertiary = hollywoodRed
    val onTertiary = Color.White
    val tertiaryContainer = Color(0xFFFFDAD6)
    val onTertiaryContainer = Color(0xFF410002)
    
    // Error Colors
    val error = Color(0xFFB3261E)
    val onError = Color.White
    val errorContainer = Color(0xFFF9DEDC)
    val onErrorContainer = Color(0xFF410E0B)
    
    // Surface Colors (Light Theme)
    val surfaceLight = Color(0xFFFFFBFF)
    val onSurfaceLight = Color(0xFF1C1B1F)
    val surfaceVariantLight = Color(0xFFE7E0EC)
    val onSurfaceVariantLight = Color(0xFF49454F)
    
    // Surface Colors (Dark Theme)
    val surfaceDark = sepiaBrownDark
    val onSurfaceDark = Color(0xFFE6E1E5)
    val surfaceVariantDark = Color(0xFF49454F)
    val onSurfaceVariantDark = Color(0xFFCAC4D0)
    
    // Background Colors
    val backgroundLight = Color(0xFFFFFBFF)
    val onBackgroundLight = Color(0xFF1C1B1F)
    val backgroundDark = sepiaBrown
    val onBackgroundDark = Color(0xFFE6E1E5)
    
    // Outline Colors
    val outline = Color(0xFF79747E)
    val outlineVariant = Color(0xFFCAC4D0)
    
    // State Colors
    val success = Color(0xFF4CAF50)
    val warning = Color(0xFFFF9800)
    val info = Color(0xFF2196F3)
    
    // Chart Colors (for distribution visualizations)
    val chartColor1 = hollywoodGold
    val chartColor2 = artDecoBronze
    val chartColor3 = hollywoodRed
    val chartColor4 = Color(0xFF6A4E23)
    val chartColor5 = Color(0xFFA67B5B)
    
    // Transparent overlays
    val scrimLight = Color(0x80000000)
    val scrimDark = Color(0xCC000000)
    
    // Special UI elements
    val divider = Color(0x1F000000)
    val dividerDark = Color(0x1FFFFFFF)
}
