package org.aalbertini.ham.features.settings.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.aalbertini.ham.core.ui.resources.Dimensions
import org.aalbertini.ham.core.ui.resources.Strings
import org.aalbertini.ham.features.settings.presentation.platform.PlatformSettingItem
import org.jetbrains.compose.resources.stringResource

/**
 * Reusable platform-specific settings section.
 * Displays platform-specific actions (e.g., window size reset on desktop).
 *
 * @param platformSettings List of platform-specific settings to display
 */
@Composable
fun PlatformSettingsSection(
    platformSettings: List<PlatformSettingItem>
) {
    if (platformSettings.isEmpty()) return

    HorizontalDivider()
    Spacer(modifier = Modifier.height(Dimensions.Spacing.extraLarge))

    Text(
        text = stringResource(Strings.platformSettingsTitle),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )

    Spacer(modifier = Modifier.height(Dimensions.Spacing.medium))

    Text(
        text = stringResource(Strings.platformSettingsDescription),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(Dimensions.Spacing.medium))

    // Display each platform-specific setting
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Dimensions.Spacing.medium)
    ) {
        platformSettings.forEach { setting ->
            PlatformSettingItemCard(setting = setting)
        }
    }

    Spacer(modifier = Modifier.height(Dimensions.Spacing.large))
}

/**
 * Individual platform setting item.
 *
 * @param setting The platform setting to display
 */
@Composable
private fun PlatformSettingItemCard(
    setting: PlatformSettingItem
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = setting.action),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(Dimensions.cornerRadiusMedium)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.Padding.large),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = setting.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(end = Dimensions.Spacing.medium)
                )
                Column {
                    Text(
                        text = stringResource(setting.titleRes),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(setting.descriptionRes),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                            alpha = 0.8f
                        )
                    )
                }
            }
        }
    }
}
