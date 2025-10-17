package org.aalbertini.ham.features.settings.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.aalbertini.ham.core.ui.resources.Dimensions
import org.aalbertini.ham.core.ui.resources.Strings
import org.aalbertini.ham.core.ui.resources.UiConstants
import org.aalbertini.ham.core.util.platform.getPlatform
import org.aalbertini.ham.features.settings.domain.model.ThemePreset
import org.aalbertini.ham.features.settings.presentation.platform.getPlatformSettings
import org.aalbertini.ham.features.settings.presentation.platform.getSettingsDialogPadding
import org.jetbrains.compose.resources.stringResource

/**
 * Settings dialog for app configuration.
 *
 * Features:
 * - Theme preset selection
 * - Light/Dark mode toggle
 * - Platform-specific settings (automatically shown based on platform)
 * - Responsive layout: Dialog on desktop, fullscreen on mobile
 *
 * Behavior:
 * - Desktop (large window): Dialog with rounded corners and padding (68dp), dismissible by clicking outside
 *   - Width: Half of window width, with minimum of 600dp (ensures readability)
 * - Desktop (small window < 600dp width or < 800dp height): Switches to fullscreen mode
 *   - No padding, rounded corners, optimized for limited space
 * - Mobile: Fullscreen view (0dp padding, no rounded corners), not dismissible by clicking outside
 * - Both: Dismissible with back button/escape key, close button in top-right
 *
 * @param currentThemePreset Currently selected theme preset
 * @param isDarkMode Current dark mode state
 * @param onThemePresetChange Callback when theme preset is changed
 * @param onDarkModeChange Callback when dark mode is toggled
 * @param onResetWindowSize Callback for platform-specific actions (e.g., window size reset on desktop)
 * @param onDismiss Callback when dialog is dismissed
 */
@Composable
fun SettingsDialog(
    currentThemePreset: ThemePreset,
    isDarkMode: Boolean,
    onThemePresetChange: (ThemePreset) -> Unit,
    onDarkModeChange: (Boolean) -> Unit,
    onResetWindowSize: (() -> Unit)? = null,
    onDismiss: () -> Unit
) {
    var selectedPreset by remember(currentThemePreset) {
        mutableStateOf(currentThemePreset)
    }
    var darkModeEnabled by remember(isDarkMode) { mutableStateOf(isDarkMode) }
    var themesExpanded by remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()
    val platform = remember { getPlatform() }
    val isMobile = platform.isMobile
    val platformDialogPadding = getSettingsDialogPadding()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = !isMobile,
            dismissOnBackPress = true,
            usePlatformDefaultWidth = false
        )
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            // Check if window is smaller than default dimensions (desktop only)
            val isWindowTooSmall by remember(
                maxWidth,
                maxHeight,
                isMobile
            ) {
                derivedStateOf {
                    !isMobile && (maxWidth < UiConstants.Dialog.defaultDialogWidth
                            || maxHeight < UiConstants.Dialog.defaultDialogHeight)
                }
            }

            // Calculate actual padding: remove padding if window is too small on desktop
            val dialogPadding by remember(
                isWindowTooSmall,
                platformDialogPadding,
                isMobile
            ) {
                derivedStateOf {
                    if (isMobile || isWindowTooSmall) 0.dp else platformDialogPadding
                }
            }

            // Calculate dialog width
            val dialogWidth by remember(
                maxWidth,
                isMobile,
                isWindowTooSmall
            ) {
                derivedStateOf {
                    if (isMobile || isWindowTooSmall) {
                        maxWidth
                    } else {
                        maxOf(UiConstants.Dialog.defaultDialogWidth, maxWidth / 2)
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (!isMobile) {
                            // Add clickable background for desktop to handle dismissOnClickOutside
                            Modifier
                                .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.32f))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = onDismiss
                                )
                        } else {
                            Modifier
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {} // Prevent clicks from propagating to background
                        )
                        .fillMaxSize()
                        .then(
                            if (!isMobile && !isWindowTooSmall) Modifier.width(dialogWidth)
                            else Modifier
                        )
                        .padding(dialogPadding),
                    shape = RoundedCornerShape(
                        if (dialogPadding > 0.dp) Dimensions.cornerRadiusXLarge else Dimensions.cornerRadiusSmall
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Header
                        SettingsDialogHeader(onDismiss = onDismiss)

                        // Scrollable content
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .verticalScroll(scrollState)
                                .padding(Dimensions.Padding.extraLarge)
                        ) {
                            // Dark Mode Toggle Section
                            Text(
                                text = stringResource(Strings.appearanceTitle),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(Dimensions.Spacing.medium))

                            DarkModeToggle(
                                isDarkMode = darkModeEnabled,
                                onToggle = {
                                    darkModeEnabled = !darkModeEnabled
                                    onDarkModeChange(darkModeEnabled)
                                }
                            )

                            Spacer(modifier = Modifier.height(Dimensions.Spacing.extraLarge))

                            // Theme Selection Section
                            ThemePresetSection(
                                selectedPreset = selectedPreset,
                                isExpanded = themesExpanded,
                                onToggleExpanded = { themesExpanded = !themesExpanded },
                                onPresetSelected = { preset ->
                                    selectedPreset = preset
                                    onThemePresetChange(preset)
                                }
                            )

                            Spacer(modifier = Modifier.height(Dimensions.Spacing.extraLarge))

                            // Platform-specific settings
                            val platformSettings = remember(onResetWindowSize) {
                                getPlatformSettings(onResetWindowSize)
                            }

                            PlatformSettingsSection(platformSettings = platformSettings)
                        }
                    }
                }
            }
        }
    }
}
