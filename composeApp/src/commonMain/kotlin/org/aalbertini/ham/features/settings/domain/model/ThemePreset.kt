package org.aalbertini.ham.features.settings.domain.model

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

/**
 * Theme preset options for the app.
 * Each preset provides a unique color scheme for both light and dark modes.
 */
enum class ThemePreset(
    val displayName: String,
    val description: String
) {
    MIDNIGHT_PURPLE(
        displayName = "Midnight Purple",
        description = "Deep purples and lavender"
    ),
    HOLLYWOOD_CLASSIC(
        displayName = "Hollywood Classic",
        description = "Authentic colors from the game's Art Deco UI"
    ),
    OCEAN_BREEZE(
        displayName = "Ocean Breeze",
        description = "Cool blues and aqua accents"
    ),
    FOREST_WHISPER(
        displayName = "Forest Whisper",
        description = "Natural greens and earth tones"
    ),
    SUNSET_GLOW(
        displayName = "Sunset Glow",
        description = "Warm oranges and coral hues"
    );

    companion object {
        /**
         * Get theme preset from saved preference value
         */
        fun fromString(value: String?): ThemePreset? {
            return entries.find { it.name == value }
        }
    }
}

/**
 * Provides color schemes for each theme preset.
 * All themes are compatible with both light and dark modes.
 */
object ThemeColorSchemes {
    
    /**
     * Get the light color scheme for a given theme preset
     */
    fun getLightColorScheme(preset: ThemePreset): ColorScheme {
        return when (preset) {
            ThemePreset.HOLLYWOOD_CLASSIC -> hollywoodClassicLight()
            ThemePreset.OCEAN_BREEZE -> oceanBreezeLight()
            ThemePreset.FOREST_WHISPER -> forestWhisperLight()
            ThemePreset.SUNSET_GLOW -> sunsetGlowLight()
            ThemePreset.MIDNIGHT_PURPLE -> midnightPurpleLight()
        }
    }
    
    /**
     * Get the dark color scheme for a given theme preset
     */
    fun getDarkColorScheme(preset: ThemePreset): ColorScheme {
        return when (preset) {
            ThemePreset.HOLLYWOOD_CLASSIC -> hollywoodClassicDark()
            ThemePreset.OCEAN_BREEZE -> oceanBreezeDark()
            ThemePreset.FOREST_WHISPER -> forestWhisperDark()
            ThemePreset.SUNSET_GLOW -> sunsetGlowDark()
            ThemePreset.MIDNIGHT_PURPLE -> midnightPurpleDark()
        }
    }
    
    // Hollywood Classic Theme - Inspired by Hollywood Animal game UI
    private fun hollywoodClassicLight() = lightColorScheme(
        primary = Color(0xFFB8860B),
        onPrimary = Color.White,
        primaryContainer = Color(0xFFD4B896),
        onPrimaryContainer = Color(0xFF2A1810),
        secondary = Color(0xFF8B5A2B),
        onSecondary = Color.White,
        secondaryContainer = Color(0xFFE8D5C4),
        onSecondaryContainer = Color(0xFF2A1810),
        tertiary = Color(0xFFC41E3A),
        onTertiary = Color.White,
        tertiaryContainer = Color(0xFFFFDAD6),
        onTertiaryContainer = Color(0xFF410002),
        background = Color(0xFFFFFBFF),
        onBackground = Color(0xFF1C1B1F),
        surface = Color(0xFFFFFBFF),
        onSurface = Color(0xFF1C1B1F),
        surfaceVariant = Color(0xFFE8DED3),
        onSurfaceVariant = Color(0xFF3A3028),
        error = Color(0xFFB3261E),
        onError = Color.White,
    )
    
    private fun hollywoodClassicDark() = darkColorScheme(
        primary = Color(0xFFD4AF37),           // Classic gold from game UI
        onPrimary = Color(0xFF1A1512),         // Dark text on gold
        primaryContainer = Color(0xFF3E2F1A),  // Dark brown container (better contrast)
        onPrimaryContainer = Color(0xFFE8D4A0), // Light gold text on dark brown
        secondary = Color(0xFFCD7F32),         // Bronze accent
        onSecondary = Color(0xFF1A1512),       // Dark text on bronze
        secondaryContainer = Color(0xFF3A2416), // Dark bronze container
        onSecondaryContainer = Color(0xFFE8C9A8), // Light bronze text
        tertiary = Color(0xFF5DB3C8),          // Teal accent from game
        onTertiary = Color(0xFF003D47),        // Dark text on teal
        tertiaryContainer = Color(0xFF1A3238), // Dark teal container
        onTertiaryContainer = Color(0xFFB8E8F5), // Light teal text
        background = Color(0xFF0D0D0D),        // Very dark bg from game
        onBackground = Color(0xFFE8E1DC),      // Light text on dark bg
        surface = Color(0xFF1A1A1A),           // Slightly lighter surface
        onSurface = Color(0xFFE8E1DC),         // Light text on surface
        surfaceVariant = Color(0xFF2A2520),    // Dark brown surface variant
        onSurfaceVariant = Color(0xFFC4A574),  // Muted gold on dark brown (better contrast)
        error = Color(0xFFE8A040),             // Orange from expenses
        onError = Color(0xFF1A1512),           // Dark text on error
    )
    
    // Ocean Breeze Theme - Verified contrast
    private fun oceanBreezeLight() = lightColorScheme(
        primary = Color(0xFF006494),
        onPrimary = Color.White,
        primaryContainer = Color(0xFFC8E6FF),
        onPrimaryContainer = Color(0xFF001E30),
        secondary = Color(0xFF00687A),
        onSecondary = Color.White,
        secondaryContainer = Color(0xFFB3EBFA),
        onSecondaryContainer = Color(0xFF001F25),
        tertiary = Color(0xFF006874),
        onTertiary = Color.White,
        tertiaryContainer = Color(0xFFA6F0FF),
        onTertiaryContainer = Color(0xFF001F24),
        background = Color(0xFFFCFCFF),
        onBackground = Color(0xFF1A1C1E),
        surface = Color(0xFFFCFCFF),
        onSurface = Color(0xFF1A1C1E),
        surfaceVariant = Color(0xFFDDE3EA),
        onSurfaceVariant = Color(0xFF3F474D),
        error = Color(0xFFBA1A1A),
        onError = Color.White,
    )
    
    private fun oceanBreezeDark() = darkColorScheme(
        primary = Color(0xFF8CCEFF),
        onPrimary = Color(0xFF00344F),
        primaryContainer = Color(0xFF004C6F),
        onPrimaryContainer = Color(0xFFCAE6FF),
        secondary = Color(0xFF7DD5E6),
        onSecondary = Color(0xFF00363E),
        secondaryContainer = Color(0xFF004F5A),
        onSecondaryContainer = Color(0xFFB8ECFA),
        tertiary = Color(0xFF85D6E5),
        onTertiary = Color(0xFF00363D),
        tertiaryContainer = Color(0xFF004F58),
        onTertiaryContainer = Color(0xFFA6F0FF),
        background = Color(0xFF1A1C1E),
        onBackground = Color(0xFFE2E2E5),
        surface = Color(0xFF1A1C1E),
        onSurface = Color(0xFFE2E2E5),
        surfaceVariant = Color(0xFF41484D),
        onSurfaceVariant = Color(0xFFC1C7CE),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
    )
    
    // Forest Whisper Theme - Verified contrast
    private fun forestWhisperLight() = lightColorScheme(
        primary = Color(0xFF2D6B2F),
        onPrimary = Color.White,
        primaryContainer = Color(0xFFB4F1B6),
        onPrimaryContainer = Color(0xFF002105),
        secondary = Color(0xFF526350),
        onSecondary = Color.White,
        secondaryContainer = Color(0xFFD5E8D0),
        onSecondaryContainer = Color(0xFF0F1F10),
        tertiary = Color(0xFF006874),
        onTertiary = Color.White,
        tertiaryContainer = Color(0xFFA6F0FF),
        onTertiaryContainer = Color(0xFF001F24),
        background = Color(0xFFFCFDF7),
        onBackground = Color(0xFF1A1C19),
        surface = Color(0xFFFCFDF7),
        onSurface = Color(0xFF1A1C19),
        surfaceVariant = Color(0xFFDEE5D9),
        onSurfaceVariant = Color(0xFF3E4640),
        error = Color(0xFFBA1A1A),
        onError = Color.White,
    )
    
    private fun forestWhisperDark() = darkColorScheme(
        primary = Color(0xFF98D49A),
        onPrimary = Color(0xFF00390B),
        primaryContainer = Color(0xFF14511B),
        onPrimaryContainer = Color(0xFFB4F1B6),
        secondary = Color(0xFFB9CCB5),
        onSecondary = Color(0xFF243424),
        secondaryContainer = Color(0xFF3A4B3A),
        onSecondaryContainer = Color(0xFFD5E8D0),
        tertiary = Color(0xFF85D6E5),
        onTertiary = Color(0xFF00363D),
        tertiaryContainer = Color(0xFF004F58),
        onTertiaryContainer = Color(0xFFA6F0FF),
        background = Color(0xFF1A1C19),
        onBackground = Color(0xFFE2E3DD),
        surface = Color(0xFF1A1C19),
        onSurface = Color(0xFFE2E3DD),
        surfaceVariant = Color(0xFF424940),
        onSurfaceVariant = Color(0xFFC2C9BD),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
    )
    
    // Sunset Glow Theme - Verified contrast
    private fun sunsetGlowLight() = lightColorScheme(
        primary = Color(0xFFA63C10),
        onPrimary = Color.White,
        primaryContainer = Color(0xFFFFDACE),
        onPrimaryContainer = Color(0xFF3B0800),
        secondary = Color(0xFFB85E1C),
        onSecondary = Color.White,
        secondaryContainer = Color(0xFFFFDCC1),
        onSecondaryContainer = Color(0xFF2C1600),
        tertiary = Color(0xFFBE4C2E),
        onTertiary = Color.White,
        tertiaryContainer = Color(0xFFFFDAD1),
        onTertiaryContainer = Color(0xFF3A0900),
        background = Color(0xFFFFFBFF),
        onBackground = Color(0xFF201A18),
        surface = Color(0xFFFFFBFF),
        onSurface = Color(0xFF201A18),
        surfaceVariant = Color(0xFFF5DED6),
        onSurfaceVariant = Color(0xFF503F3A),
        error = Color(0xFFBA1A1A),
        onError = Color.White,
    )
    
    private fun sunsetGlowDark() = darkColorScheme(
        primary = Color(0xFFFFB59D),
        onPrimary = Color(0xFF5F1600),
        primaryContainer = Color(0xFF852B00),
        onPrimaryContainer = Color(0xFFFFDACE),
        secondary = Color(0xFFFFB787),
        onSecondary = Color(0xFF4A2800),
        secondaryContainer = Color(0xFF8E4A10),
        onSecondaryContainer = Color(0xFFFFDCC1),
        tertiary = Color(0xFFFFB4A0),
        onTertiary = Color(0xFF5E1600),
        tertiaryContainer = Color(0xFF9A3519),
        onTertiaryContainer = Color(0xFFFFDAD1),
        background = Color(0xFF201A18),
        onBackground = Color(0xFFEDE0DC),
        surface = Color(0xFF201A18),
        onSurface = Color(0xFFEDE0DC),
        surfaceVariant = Color(0xFF53433E),
        onSurfaceVariant = Color(0xFFD8C2B9),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
    )
    
    // Midnight Purple Theme
    private fun midnightPurpleLight() = lightColorScheme(
        primary = Color(0xFF6750A4),
        onPrimary = Color.White,
        primaryContainer = Color(0xFFEADDFF),
        onPrimaryContainer = Color(0xFF21005D),
        secondary = Color(0xFF625B71),
        onSecondary = Color.White,
        secondaryContainer = Color(0xFFE8DEF8),
        onSecondaryContainer = Color(0xFF1D192B),
        tertiary = Color(0xFF7D5260),
        onTertiary = Color.White,
        tertiaryContainer = Color(0xFFFFD8E4),
        onTertiaryContainer = Color(0xFF31111D),
        background = Color(0xFFFFFBFE),
        onBackground = Color(0xFF1C1B1F),
        surface = Color(0xFFFFFBFE),
        onSurface = Color(0xFF1C1B1F),
        surfaceVariant = Color(0xFFE7E0EC),
        onSurfaceVariant = Color(0xFF49454F),
        error = Color(0xFFB3261E),
        onError = Color.White,
    )
    
    private fun midnightPurpleDark() = darkColorScheme(
        primary = Color(0xFFD0BCFF),
        onPrimary = Color(0xFF381E72),
        primaryContainer = Color(0xFF4F378B),
        onPrimaryContainer = Color(0xFFEADDFF),
        secondary = Color(0xFFCCC2DC),
        onSecondary = Color(0xFF332D41),
        secondaryContainer = Color(0xFF4A4458),
        onSecondaryContainer = Color(0xFFE8DEF8),
        tertiary = Color(0xFFEFB8C8),
        onTertiary = Color(0xFF492532),
        tertiaryContainer = Color(0xFF633B48),
        onTertiaryContainer = Color(0xFFFFD8E4),
        background = Color(0xFF1C1B1F),
        onBackground = Color(0xFFE6E1E5),
        surface = Color(0xFF1C1B1F),
        onSurface = Color(0xFFE6E1E5),
        surfaceVariant = Color(0xFF49454F),
        onSurfaceVariant = Color(0xFFCAC4D0),
        error = Color(0xFFF2B8B5),
        onError = Color(0xFF601410),
    )
}
