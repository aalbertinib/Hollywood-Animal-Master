package org.aalbertini.ham.features.settings.domain.model

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

/**
 * Theme preset options for the app.
 * Each preset provides a unique color scheme for both light and dark modes.
 * Based on Material3 Dynamic theme examples.
 */
enum class ThemePreset(
    val displayName: String,
    val description: String
) {
    D01_PURPLE(
        displayName = "Purple",
        description = "Material3 purple theme"
    ),
    D02_CADMIUM_GREEN(
        displayName = "Cadmium Green",
        description = "Material3 cadmium green theme"
    ),
    D03_PORCUPINE(
        displayName = "Porcupine",
        description = "Material3 warm yellow-brown theme"
    ),
    D04_MAGENTA(
        displayName = "Magenta",
        description = "Material3 magenta theme"
    ),
    D05_BROWN(
        displayName = "Brown",
        description = "Material3 brown theme"
    ),
    D06_TEAL_BLUE(
        displayName = "Teal Blue",
        description = "Material3 teal blue theme"
    ),
    D07_DARK_OLIVE_GREEN(
        displayName = "Dark Olive Green",
        description = "Material3 dark olive green theme"
    ),
    D08_RED(
        displayName = "Red",
        description = "Material3 red theme"
    ),
    D09_INDIGO_BLUE(
        displayName = "Indigo Blue",
        description = "Material3 indigo blue theme"
    ),
    D10_GREEN(
        displayName = "Green",
        description = "Material3 green theme"
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
            ThemePreset.D01_PURPLE -> d01PurpleLight()
            ThemePreset.D02_CADMIUM_GREEN -> d02CadmiumGreenLight()
            ThemePreset.D03_PORCUPINE -> d03PorcupineLight()
            ThemePreset.D04_MAGENTA -> d04MagentaLight()
            ThemePreset.D05_BROWN -> d05BrownLight()
            ThemePreset.D06_TEAL_BLUE -> d06TealBlueLight()
            ThemePreset.D07_DARK_OLIVE_GREEN -> d07DarkOliveGreenLight()
            ThemePreset.D08_RED -> d08RedLight()
            ThemePreset.D09_INDIGO_BLUE -> d09IndigoBlueLight()
            ThemePreset.D10_GREEN -> d10GreenLight()
        }
    }
    
    /**
     * Get the dark color scheme for a given theme preset
     */
    fun getDarkColorScheme(preset: ThemePreset): ColorScheme {
        return when (preset) {
            ThemePreset.D01_PURPLE -> d01PurpleDark()
            ThemePreset.D02_CADMIUM_GREEN -> d02CadmiumGreenDark()
            ThemePreset.D03_PORCUPINE -> d03PorcupineDark()
            ThemePreset.D04_MAGENTA -> d04MagentaDark()
            ThemePreset.D05_BROWN -> d05BrownDark()
            ThemePreset.D06_TEAL_BLUE -> d06TealBlueDark()
            ThemePreset.D07_DARK_OLIVE_GREEN -> d07DarkOliveGreenDark()
            ThemePreset.D08_RED -> d08RedDark()
            ThemePreset.D09_INDIGO_BLUE -> d09IndigoBlueDark()
            ThemePreset.D10_GREEN -> d10GreenDark()
        }
    }
    
    // D01 - Purple Theme
    private fun d01PurpleLight() = lightColorScheme(
        primary = Color(0xFF6750A4),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFEADDFF),
        onPrimaryContainer = Color(0xFF21005D),
        secondary = Color(0xFF625B71),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFE8DEF8),
        onSecondaryContainer = Color(0xFF1D192B),
        tertiary = Color(0xFF7D5260),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFFFD8E4),
        onTertiaryContainer = Color(0xFF31111D),
        error = Color(0xFFBA1A1A),
        onError = Color(0xFFFFFFFF),
        background = Color(0xFFFFFBFE),
        onBackground = Color(0xFF1C1B1F),
        surface = Color(0xFFFFFBFE),
        onSurface = Color(0xFF1C1B1F),
        surfaceVariant = Color(0xFFE7E0EC),
        onSurfaceVariant = Color(0xFF49454F),
    )
    
    private fun d01PurpleDark() = darkColorScheme(
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
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        background = Color(0xFF1C1B1F),
        onBackground = Color(0xFFE6E1E5),
        surface = Color(0xFF1C1B1F),
        onSurface = Color(0xFFE6E1E5),
        surfaceVariant = Color(0xFF49454F),
        onSurfaceVariant = Color(0xFFCAC4D0),
    )
    
    // D02 - Cadmium Green Theme
    private fun d02CadmiumGreenLight() = lightColorScheme(
        primary = Color(0xFF006E26),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFF96F990),
        onPrimaryContainer = Color(0xFF002106),
        secondary = Color(0xFF526350),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFD5E8D0),
        onSecondaryContainer = Color(0xFF101F10),
        tertiary = Color(0xFF39656D),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFBCEBF4),
        onTertiaryContainer = Color(0xFF001F24),
        error = Color(0xFFBA1A1A),
        onError = Color(0xFFFFFFFF),
        background = Color(0xFFFCFDF7),
        onBackground = Color(0xFF1A1C19),
        surface = Color(0xFFFCFDF7),
        onSurface = Color(0xFF1A1C19),
        surfaceVariant = Color(0xFFDEE5D9),
        onSurfaceVariant = Color(0xFF424940),
    )
    
    private fun d02CadmiumGreenDark() = darkColorScheme(
        primary = Color(0xFF7ADC76),
        onPrimary = Color(0xFF00390F),
        primaryContainer = Color(0xFF00531B),
        onPrimaryContainer = Color(0xFF96F990),
        secondary = Color(0xFFB9CCB5),
        onSecondary = Color(0xFF243424),
        secondaryContainer = Color(0xFF3A4B39),
        onSecondaryContainer = Color(0xFFD5E8D0),
        tertiary = Color(0xFFA1CED7),
        onTertiary = Color(0xFF00363D),
        tertiaryContainer = Color(0xFF1F4D54),
        onTertiaryContainer = Color(0xFFBCEBF4),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        background = Color(0xFF1A1C19),
        onBackground = Color(0xFFE2E3DD),
        surface = Color(0xFF1A1C19),
        onSurface = Color(0xFFE2E3DD),
        surfaceVariant = Color(0xFF424940),
        onSurfaceVariant = Color(0xFFC2C9BD),
    )
    
    // D03 - Porcupine Theme
    private fun d03PorcupineLight() = lightColorScheme(
        primary = Color(0xFF6C5E10),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFF6E388),
        onPrimaryContainer = Color(0xFF211B00),
        secondary = Color(0xFF655F40),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFECE3BC),
        onSecondaryContainer = Color(0xFF201C04),
        tertiary = Color(0xFF43664E),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFC5ECCD),
        onTertiaryContainer = Color(0xFF00210F),
        error = Color(0xFFBA1A1A),
        onError = Color(0xFFFFFFFF),
        background = Color(0xFFFFFBFF),
        onBackground = Color(0xFF1D1C16),
        surface = Color(0xFFFFFBFF),
        onSurface = Color(0xFF1D1C16),
        surfaceVariant = Color(0xFFE8E2D0),
        onSurfaceVariant = Color(0xFF4A4739),
    )
    
    private fun d03PorcupineDark() = darkColorScheme(
        primary = Color(0xFFD9C66F),
        onPrimary = Color(0xFF383000),
        primaryContainer = Color(0xFF514600),
        onPrimaryContainer = Color(0xFFF6E388),
        secondary = Color(0xFFCFC7A2),
        onSecondary = Color(0xFF353117),
        secondaryContainer = Color(0xFF4C472B),
        onSecondaryContainer = Color(0xFFECE3BC),
        tertiary = Color(0xFFA9D0B2),
        onTertiary = Color(0xFF153723),
        tertiaryContainer = Color(0xFF2C4E38),
        onTertiaryContainer = Color(0xFFC5ECCD),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        background = Color(0xFF1D1C16),
        onBackground = Color(0xFFE6E2D9),
        surface = Color(0xFF1D1C16),
        onSurface = Color(0xFFE6E2D9),
        surfaceVariant = Color(0xFF4A4739),
        onSurfaceVariant = Color(0xFFCCC6B5),
    )
    
    // D04 - Magenta Theme
    private fun d04MagentaLight() = lightColorScheme(
        primary = Color(0xFF984061),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFFFD9E2),
        onPrimaryContainer = Color(0xFF3E001D),
        secondary = Color(0xFF75565F),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFFFD9E2),
        onSecondaryContainer = Color(0xFF2B151C),
        tertiary = Color(0xFF7C5635),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFFFDCC1),
        onTertiaryContainer = Color(0xFF2E1500),
        error = Color(0xFFBA1A1A),
        onError = Color(0xFFFFFFFF),
        background = Color(0xFFFFFBFF),
        onBackground = Color(0xFF201A1B),
        surface = Color(0xFFFFFBFF),
        onSurface = Color(0xFF201A1B),
        surfaceVariant = Color(0xFFF2DDE2),
        onSurfaceVariant = Color(0xFF514347),
    )
    
    private fun d04MagentaDark() = darkColorScheme(
        primary = Color(0xFFFFB1C8),
        onPrimary = Color(0xFF5E1133),
        primaryContainer = Color(0xFF7B2949),
        onPrimaryContainer = Color(0xFFFFD9E2),
        secondary = Color(0xFFE3BDC6),
        onSecondary = Color(0xFF442A31),
        secondaryContainer = Color(0xFF5A3F47),
        onSecondaryContainer = Color(0xFFFFD9E2),
        tertiary = Color(0xFFEFBD94),
        onTertiary = Color(0xFF48290C),
        tertiaryContainer = Color(0xFF623F20),
        onTertiaryContainer = Color(0xFFFFDCC1),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        background = Color(0xFF201A1B),
        onBackground = Color(0xFFECE0E1),
        surface = Color(0xFF201A1B),
        onSurface = Color(0xFFECE0E1),
        surfaceVariant = Color(0xFF514347),
        onSurfaceVariant = Color(0xFFD5C2C6),
    )
    
    // D05 - Brown Theme
    private fun d05BrownLight() = lightColorScheme(
        primary = Color(0xFF825500),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFFFDDB3),
        onPrimaryContainer = Color(0xFF291800),
        secondary = Color(0xFF6F5B40),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFFBDEBC),
        onSecondaryContainer = Color(0xFF271904),
        tertiary = Color(0xFF51643F),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFD4EABB),
        onTertiaryContainer = Color(0xFF102004),
        error = Color(0xFFBA1A1A),
        onError = Color(0xFFFFFFFF),
        background = Color(0xFFFFFBFF),
        onBackground = Color(0xFF1F1B16),
        surface = Color(0xFFFFFBFF),
        onSurface = Color(0xFF1F1B16),
        surfaceVariant = Color(0xFFF0E0CF),
        onSurfaceVariant = Color(0xFF4F4539),
    )
    
    private fun d05BrownDark() = darkColorScheme(
        primary = Color(0xFFFFB951),
        onPrimary = Color(0xFF452B00),
        primaryContainer = Color(0xFF633F00),
        onPrimaryContainer = Color(0xFFFFDDB3),
        secondary = Color(0xFFDDC2A1),
        onSecondary = Color(0xFF3E2D16),
        secondaryContainer = Color(0xFF56442A),
        onSecondaryContainer = Color(0xFFFBDEBC),
        tertiary = Color(0xFFB8CEA1),
        onTertiary = Color(0xFF243515),
        tertiaryContainer = Color(0xFF3A4C2A),
        onTertiaryContainer = Color(0xFFD4EABB),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        background = Color(0xFF1F1B16),
        onBackground = Color(0xFFEAE1D9),
        surface = Color(0xFF1F1B16),
        onSurface = Color(0xFFEAE1D9),
        surfaceVariant = Color(0xFF4F4539),
        onSurfaceVariant = Color(0xFFD3C4B4),
    )
    
    // D06 - Teal Blue Theme
    private fun d06TealBlueLight() = lightColorScheme(
        primary = Color(0xFF006A6A),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFF6FF7F7),
        onPrimaryContainer = Color(0xFF002020),
        secondary = Color(0xFF4A6363),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFCCE8E7),
        onSecondaryContainer = Color(0xFF051F1F),
        tertiary = Color(0xFF4B607C),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFD3E4FF),
        onTertiaryContainer = Color(0xFF041C35),
        error = Color(0xFFBA1A1A),
        onError = Color(0xFFFFFFFF),
        background = Color(0xFFFAFDFD),
        onBackground = Color(0xFF191C1C),
        surface = Color(0xFFFAFDFD),
        onSurface = Color(0xFF191C1C),
        surfaceVariant = Color(0xFFDAE5E4),
        onSurfaceVariant = Color(0xFF3F4948),
    )
    
    private fun d06TealBlueDark() = darkColorScheme(
        primary = Color(0xFF4DDADA),
        onPrimary = Color(0xFF003737),
        primaryContainer = Color(0xFF004F4F),
        onPrimaryContainer = Color(0xFF6FF7F7),
        secondary = Color(0xFFB0CCCB),
        onSecondary = Color(0xFF1B3534),
        secondaryContainer = Color(0xFF324B4B),
        onSecondaryContainer = Color(0xFFCCE8E7),
        tertiary = Color(0xFFB3C8E8),
        onTertiary = Color(0xFF1C314B),
        tertiaryContainer = Color(0xFF334863),
        onTertiaryContainer = Color(0xFFD3E4FF),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        background = Color(0xFF191C1C),
        onBackground = Color(0xFFE0E3E3),
        surface = Color(0xFF191C1C),
        onSurface = Color(0xFFE0E3E3),
        surfaceVariant = Color(0xFF3F4948),
        onSurfaceVariant = Color(0xFFBEC9C8),
    )
    
    // D07 - Dark Olive Green Theme
    private fun d07DarkOliveGreenLight() = lightColorScheme(
        primary = Color(0xFF4F6600),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFD0EF7D),
        onPrimaryContainer = Color(0xFF151F00),
        secondary = Color(0xFF5C6147),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFE0E6C4),
        onSecondaryContainer = Color(0xFF1A1D09),
        tertiary = Color(0xFF3C665B),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFBEECDE),
        onTertiaryContainer = Color(0xFF002119),
        error = Color(0xFFBA1A1A),
        onError = Color(0xFFFFFFFF),
        background = Color(0xFFFEFCF5),
        onBackground = Color(0xFF1B1C17),
        surface = Color(0xFFFEFCF5),
        onSurface = Color(0xFF1B1C17),
        surfaceVariant = Color(0xFFE3E4D3),
        onSurfaceVariant = Color(0xFF46483D),
    )
    
    private fun d07DarkOliveGreenDark() = darkColorScheme(
        primary = Color(0xFFB4D264),
        onPrimary = Color(0xFF263500),
        primaryContainer = Color(0xFF3A4D00),
        onPrimaryContainer = Color(0xFFD0EF7D),
        secondary = Color(0xFFC4CAAA),
        onSecondary = Color(0xFF2E321C),
        secondaryContainer = Color(0xFF444931),
        onSecondaryContainer = Color(0xFFE0E6C4),
        tertiary = Color(0xFFA3D0C2),
        onTertiary = Color(0xFF07372D),
        tertiaryContainer = Color(0xFF244E43),
        onTertiaryContainer = Color(0xFFBEECDE),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        background = Color(0xFF1B1C17),
        onBackground = Color(0xFFE4E3DB),
        surface = Color(0xFF1B1C17),
        onSurface = Color(0xFFE4E3DB),
        surfaceVariant = Color(0xFF46483D),
        onSurfaceVariant = Color(0xFFC7C8B8),
    )
    
    // D08 - Red Theme
    private fun d08RedLight() = lightColorScheme(
        primary = Color(0xFFB02020),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFFFDAD4),
        onPrimaryContainer = Color(0xFF410000),
        secondary = Color(0xFF775651),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFFFDAD4),
        onSecondaryContainer = Color(0xFF2C1512),
        tertiary = Color(0xFF6F5C2E),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFFBDFA6),
        onTertiaryContainer = Color(0xFF251A00),
        error = Color(0xFFBA1A1A),
        onError = Color(0xFFFFFFFF),
        background = Color(0xFFFFFBFF),
        onBackground = Color(0xFF201A19),
        surface = Color(0xFFFFFBFF),
        onSurface = Color(0xFF201A19),
        surfaceVariant = Color(0xFFF5DDD9),
        onSurfaceVariant = Color(0xFF534341),
    )
    
    private fun d08RedDark() = darkColorScheme(
        primary = Color(0xFFFFB4A8),
        onPrimary = Color(0xFF680003),
        primaryContainer = Color(0xFF920008),
        onPrimaryContainer = Color(0xFFFFDAD4),
        secondary = Color(0xFFE7BDB6),
        onSecondary = Color(0xFF442A25),
        secondaryContainer = Color(0xFF5D3F3B),
        onSecondaryContainer = Color(0xFFFFDAD4),
        tertiary = Color(0xFFDDC38C),
        onTertiary = Color(0xFF3D2E04),
        tertiaryContainer = Color(0xFF554419),
        onTertiaryContainer = Color(0xFFFBDFA6),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        background = Color(0xFF201A19),
        onBackground = Color(0xFFEDE0DD),
        surface = Color(0xFF201A19),
        onSurface = Color(0xFFEDE0DD),
        surfaceVariant = Color(0xFF534341),
        onSurfaceVariant = Color(0xFFD8C2BE),
    )
    
    // D09 - Indigo Blue Theme
    private fun d09IndigoBlueLight() = lightColorScheme(
        primary = Color(0xFF415F91),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFD6E3FF),
        onPrimaryContainer = Color(0xFF001B3E),
        secondary = Color(0xFF565F71),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFDAE2F9),
        onSecondaryContainer = Color(0xFF131C2B),
        tertiary = Color(0xFF705575),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFFAD8FD),
        onTertiaryContainer = Color(0xFF28132E),
        error = Color(0xFFBA1A1A),
        onError = Color(0xFFFFFFFF),
        background = Color(0xFFFDFBFF),
        onBackground = Color(0xFF1A1C1E),
        surface = Color(0xFFFDFBFF),
        onSurface = Color(0xFF1A1C1E),
        surfaceVariant = Color(0xFFE0E2EC),
        onSurfaceVariant = Color(0xFF44474E),
    )
    
    private fun d09IndigoBlueDark() = darkColorScheme(
        primary = Color(0xFFA8C8FF),
        onPrimary = Color(0xFF003060),
        primaryContainer = Color(0xFF244778),
        onPrimaryContainer = Color(0xFFD6E3FF),
        secondary = Color(0xFFBEC6DC),
        onSecondary = Color(0xFF283141),
        secondaryContainer = Color(0xFF3F4759),
        onSecondaryContainer = Color(0xFFDAE2F9),
        tertiary = Color(0xFFDDBCE0),
        onTertiary = Color(0xFF3F2844),
        tertiaryContainer = Color(0xFF573E5C),
        onTertiaryContainer = Color(0xFFFAD8FD),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        background = Color(0xFF1A1C1E),
        onBackground = Color(0xFFE2E2E5),
        surface = Color(0xFF1A1C1E),
        onSurface = Color(0xFFE2E2E5),
        surfaceVariant = Color(0xFF44474E),
        onSurfaceVariant = Color(0xFFC4C6D0),
    )
    
    // D10 - Green Theme
    private fun d10GreenLight() = lightColorScheme(
        primary = Color(0xFF006E1C),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFF96F990),
        onPrimaryContainer = Color(0xFF002204),
        secondary = Color(0xFF526350),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFD5E8CF),
        onSecondaryContainer = Color(0xFF101F10),
        tertiary = Color(0xFF39656F),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFBCEBF6),
        onTertiaryContainer = Color(0xFF001F26),
        error = Color(0xFFBA1A1A),
        onError = Color(0xFFFFFFFF),
        background = Color(0xFFFCFDF6),
        onBackground = Color(0xFF1A1C19),
        surface = Color(0xFFFCFDF6),
        onSurface = Color(0xFF1A1C19),
        surfaceVariant = Color(0xFFDEE5D8),
        onSurfaceVariant = Color(0xFF424940),
    )
    
    private fun d10GreenDark() = darkColorScheme(
        primary = Color(0xFF7ADC77),
        onPrimary = Color(0xFF00390A),
        primaryContainer = Color(0xFF005313),
        onPrimaryContainer = Color(0xFF96F990),
        secondary = Color(0xFFB9CCB4),
        onSecondary = Color(0xFF243424),
        secondaryContainer = Color(0xFF3A4B39),
        onSecondaryContainer = Color(0xFFD5E8CF),
        tertiary = Color(0xFFA0CFD9),
        onTertiary = Color(0xFF00363F),
        tertiaryContainer = Color(0xFF1F4D56),
        onTertiaryContainer = Color(0xFFBCEBF6),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        background = Color(0xFF1A1C19),
        onBackground = Color(0xFFE2E3DC),
        surface = Color(0xFF1A1C19),
        onSurface = Color(0xFFE2E3DC),
        surfaceVariant = Color(0xFF424940),
        onSurfaceVariant = Color(0xFFC2C9BD),
    )
}
