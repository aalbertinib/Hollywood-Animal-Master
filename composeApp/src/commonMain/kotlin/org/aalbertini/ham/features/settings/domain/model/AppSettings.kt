package org.aalbertini.ham.features.settings.domain.model

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable

@Serializable
enum class ThemeMode { SYSTEM, LIGHT, DARK }

/**
 * Seed colors the whole Material 3 scheme is generated from (tonal palettes, both modes).
 * Named after the game's era so the picker reads as a theme, not a hex list.
 */
@Serializable
enum class SeedPalette(val seed: Long) {
    GOLDEN_AGE(0xFFC9A43A),
    RED_CARPET(0xFFB3202E),
    TECHNICOLOR(0xFF1F7A8C),
    NEON_MARQUEE(0xFF8E44AD),
    BACKLOT(0xFF4C7A34),
    FILM_NOIR(0xFF5C6B73);

    val color: Color get() = Color(seed)
}

/** Subset of MaterialKolor's palette styles that stay readable with any seed above. */
@Serializable
enum class PaletteVariant { TONAL_SPOT, VIBRANT, EXPRESSIVE, NEUTRAL, FIDELITY }

@Serializable
enum class ContrastLevel(val value: Double) { STANDARD(0.0), MEDIUM(0.5), HIGH(1.0) }

@Serializable
data class AppSettings(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val seed: SeedPalette = SeedPalette.GOLDEN_AGE,
    val variant: PaletteVariant = PaletteVariant.TONAL_SPOT,
    val contrast: ContrastLevel = ContrastLevel.STANDARD,
    /** Android 12+ only: take the scheme from the wallpaper instead of [seed]. */
    val useDynamicColor: Boolean = false,
)
