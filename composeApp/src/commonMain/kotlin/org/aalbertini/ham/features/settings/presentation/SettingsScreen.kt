package org.aalbertini.ham.features.settings.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import hollywoodanimalmaster.composeapp.generated.resources.Res
import hollywoodanimalmaster.composeapp.generated.resources.about_source
import hollywoodanimalmaster.composeapp.generated.resources.about_steam
import hollywoodanimalmaster.composeapp.generated.resources.contrast_high
import hollywoodanimalmaster.composeapp.generated.resources.contrast_medium
import hollywoodanimalmaster.composeapp.generated.resources.contrast_standard
import hollywoodanimalmaster.composeapp.generated.resources.guide_disclaimer
import hollywoodanimalmaster.composeapp.generated.resources.mode_dark
import hollywoodanimalmaster.composeapp.generated.resources.mode_light
import hollywoodanimalmaster.composeapp.generated.resources.mode_system
import hollywoodanimalmaster.composeapp.generated.resources.seed_backlot
import hollywoodanimalmaster.composeapp.generated.resources.seed_film_noir
import hollywoodanimalmaster.composeapp.generated.resources.seed_golden_age
import hollywoodanimalmaster.composeapp.generated.resources.seed_neon_marquee
import hollywoodanimalmaster.composeapp.generated.resources.seed_red_carpet
import hollywoodanimalmaster.composeapp.generated.resources.seed_technicolor
import hollywoodanimalmaster.composeapp.generated.resources.settings_about
import hollywoodanimalmaster.composeapp.generated.resources.settings_always_on_top
import hollywoodanimalmaster.composeapp.generated.resources.settings_always_on_top_desc
import hollywoodanimalmaster.composeapp.generated.resources.settings_appearance
import hollywoodanimalmaster.composeapp.generated.resources.settings_contrast
import hollywoodanimalmaster.composeapp.generated.resources.settings_dynamic
import hollywoodanimalmaster.composeapp.generated.resources.settings_dynamic_desc
import hollywoodanimalmaster.composeapp.generated.resources.settings_palette
import hollywoodanimalmaster.composeapp.generated.resources.settings_reset_window
import hollywoodanimalmaster.composeapp.generated.resources.settings_reset_window_desc
import hollywoodanimalmaster.composeapp.generated.resources.settings_style
import hollywoodanimalmaster.composeapp.generated.resources.settings_theme_mode
import hollywoodanimalmaster.composeapp.generated.resources.settings_window
import hollywoodanimalmaster.composeapp.generated.resources.style_expressive
import hollywoodanimalmaster.composeapp.generated.resources.style_fidelity
import hollywoodanimalmaster.composeapp.generated.resources.style_neutral
import hollywoodanimalmaster.composeapp.generated.resources.style_tonal_spot
import hollywoodanimalmaster.composeapp.generated.resources.style_vibrant
import org.aalbertini.ham.core.ui.components.SectionCard
import org.aalbertini.ham.core.ui.theme.supportsDynamicColor
import org.aalbertini.ham.features.settings.domain.model.AppSettings
import org.aalbertini.ham.features.settings.domain.model.ContrastLevel
import org.aalbertini.ham.features.settings.domain.model.PaletteVariant
import org.aalbertini.ham.features.settings.domain.model.SeedPalette
import org.aalbertini.ham.features.settings.domain.model.ThemeMode
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/** Desktop-only window controls; `null` hides the section on other platforms. */
data class WindowControls(
    val alwaysOnTop: Boolean,
    val onAlwaysOnTopChange: (Boolean) -> Unit,
    val onResetWindowSize: () -> Unit,
)

@Composable
fun SettingsScreen(
    settings: AppSettings,
    onUpdate: ((AppSettings) -> AppSettings) -> Unit,
    windowControls: WindowControls?,
    modifier: Modifier = Modifier,
) {
    val itemModifier = Modifier.widthIn(max = 720.dp)
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item { AppearanceCard(settings, onUpdate, itemModifier) }
        windowControls?.let { controls -> item { WindowCard(controls, itemModifier) } }
        item { AboutCard(itemModifier) }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun AppearanceCard(settings: AppSettings, onUpdate: ((AppSettings) -> AppSettings) -> Unit, modifier: Modifier) {
    SectionCard(stringResource(Res.string.settings_appearance), modifier) {
        Label(stringResource(Res.string.settings_theme_mode))
        val modes = listOf(
            ThemeMode.SYSTEM to Res.string.mode_system,
            ThemeMode.LIGHT to Res.string.mode_light,
            ThemeMode.DARK to Res.string.mode_dark,
        )
        SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
            modes.forEachIndexed { index, (mode, label) ->
                SegmentedButton(
                    selected = settings.themeMode == mode,
                    onClick = { onUpdate { it.copy(themeMode = mode) } },
                    shape = SegmentedButtonDefaults.itemShape(index, modes.size),
                ) { Text(stringResource(label)) }
            }
        }

        if (supportsDynamicColor) {
            ToggleRow(
                title = stringResource(Res.string.settings_dynamic),
                description = stringResource(Res.string.settings_dynamic_desc),
                checked = settings.useDynamicColor,
                onCheckedChange = { checked -> onUpdate { it.copy(useDynamicColor = checked) } },
            )
        }

        val seedsEnabled = !(supportsDynamicColor && settings.useDynamicColor)
        Label(stringResource(Res.string.settings_palette))
        FlowRow(
            modifier = Modifier.selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SeedPalette.entries.forEach { seed ->
                SeedSwatch(
                    seed = seed,
                    selected = settings.seed == seed,
                    enabled = seedsEnabled,
                    onClick = { onUpdate { it.copy(seed = seed) } },
                )
            }
        }

        Label(stringResource(Res.string.settings_style))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            PaletteVariant.entries.forEach { variant ->
                FilterChip(
                    selected = settings.variant == variant,
                    onClick = { onUpdate { it.copy(variant = variant) } },
                    label = { Text(stringResource(variant.labelRes)) },
                    enabled = seedsEnabled,
                )
            }
        }

        Label(stringResource(Res.string.settings_contrast))
        val levels = listOf(
            ContrastLevel.STANDARD to Res.string.contrast_standard,
            ContrastLevel.MEDIUM to Res.string.contrast_medium,
            ContrastLevel.HIGH to Res.string.contrast_high,
        )
        SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
            levels.forEachIndexed { index, (level, label) ->
                SegmentedButton(
                    selected = settings.contrast == level,
                    onClick = { onUpdate { it.copy(contrast = level) } },
                    shape = SegmentedButtonDefaults.itemShape(index, levels.size),
                    enabled = seedsEnabled,
                ) { Text(stringResource(label)) }
            }
        }
    }
}

@Composable
private fun SeedSwatch(seed: SeedPalette, selected: Boolean, enabled: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(72.dp)
            .selectable(selected = selected, enabled = enabled, role = Role.RadioButton, onClick = onClick)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        val ring = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
        Surface(
            shape = CircleShape,
            color = seed.color.copy(alpha = if (enabled) 1f else 0.38f),
            modifier = Modifier.size(48.dp).border(if (selected) 3.dp else 1.dp, ring, CircleShape),
        ) {
            if (selected) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Outlined.Check,
                        contentDescription = null,
                        tint = if (seed.color.luminance() > 0.4f) MaterialTheme.colorScheme.scrim else MaterialTheme.colorScheme.surface,
                    )
                }
            }
        }
        Text(
            stringResource(seed.labelRes),
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            color = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun WindowCard(controls: WindowControls, modifier: Modifier) {
    SectionCard(stringResource(Res.string.settings_window), modifier) {
        ToggleRow(
            title = stringResource(Res.string.settings_always_on_top),
            description = stringResource(Res.string.settings_always_on_top_desc),
            checked = controls.alwaysOnTop,
            onCheckedChange = controls.onAlwaysOnTopChange,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(stringResource(Res.string.settings_reset_window), style = MaterialTheme.typography.bodyLarge)
                Text(
                    stringResource(Res.string.settings_reset_window_desc),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            TextButton(onClick = controls.onResetWindowSize) {
                Icon(Icons.Outlined.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                Text(stringResource(Res.string.settings_reset_window), modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}

@Composable
private fun AboutCard(modifier: Modifier) {
    val uriHandler = LocalUriHandler.current
    SectionCard(stringResource(Res.string.settings_about), modifier) {
        listOf(
            Res.string.about_source to "https://github.com/aalbertinib/Hollywood-Animal-Master",
            Res.string.about_steam to "https://store.steampowered.com/app/2680550/Hollywood_Animal/",
        ).forEach { (label, url) ->
            TextButton(onClick = { uriHandler.openUri(url) }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Outlined.Link, contentDescription = null, modifier = Modifier.size(18.dp))
                Text(stringResource(label), modifier = Modifier.weight(1f).padding(start = 8.dp))
            }
        }
        Text(
            stringResource(Res.string.guide_disclaimer),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ToggleRow(title: String, description: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .toggleable(value = checked, role = Role.Switch, onValueChange = onCheckedChange)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f).padding(end = 16.dp)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = checked, onCheckedChange = null)
    }
}

@Composable
private fun Label(text: String) {
    Text(text, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
}

private val SeedPalette.labelRes: StringResource
    get() = when (this) {
        SeedPalette.GOLDEN_AGE -> Res.string.seed_golden_age
        SeedPalette.RED_CARPET -> Res.string.seed_red_carpet
        SeedPalette.TECHNICOLOR -> Res.string.seed_technicolor
        SeedPalette.NEON_MARQUEE -> Res.string.seed_neon_marquee
        SeedPalette.BACKLOT -> Res.string.seed_backlot
        SeedPalette.FILM_NOIR -> Res.string.seed_film_noir
    }

private val PaletteVariant.labelRes: StringResource
    get() = when (this) {
        PaletteVariant.TONAL_SPOT -> Res.string.style_tonal_spot
        PaletteVariant.VIBRANT -> Res.string.style_vibrant
        PaletteVariant.EXPRESSIVE -> Res.string.style_expressive
        PaletteVariant.NEUTRAL -> Res.string.style_neutral
        PaletteVariant.FIDELITY -> Res.string.style_fidelity
    }
