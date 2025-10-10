package org.aalbertini.ham.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CameraRoll
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Theaters
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents an emoji-like icon with a Material Icon and color.
 * This provides consistent cross-platform rendering including web.
 */
data class EmojiIcon(
    val icon: ImageVector,
    val color: Color,
    val contentDescription: String
)

/**
 * Renders an EmojiIcon inline with text styling.
 * @param tint Optional color override. If provided, uses this color instead of the icon's default color.
 */
@Composable
fun EmojiIcon(
    emojiIcon: EmojiIcon,
    modifier: Modifier = Modifier,
    tint: Color? = null
) {
    Icon(
        imageVector = emojiIcon.icon,
        contentDescription = emojiIcon.contentDescription,
        tint = tint ?: emojiIcon.color,
        modifier = modifier
    )
}

/**
 * Centralized emoji-like icons using Material Icons Extended with colors.
 * These render consistently across all platforms including web.
 */
object AppEmojiIcons {
    val CLAPPER_BOARD = EmojiIcon(
        icon = Icons.Filled.Movie,
        color = Color(0xFF2196F3), // Blue
        contentDescription = "Movie"
    )
    
    val DART = EmojiIcon(
        icon = Icons.Filled.GpsFixed,
        color = Color(0xFFE91E63), // Pink/Red
        contentDescription = "Target"
    )

    val CAMERA_ROLL = EmojiIcon(
        icon = Icons.Filled.CameraRoll,
        color = Color(0xFFE91E63), // Pink/Red
        contentDescription = "Camera"
    )

    val BAR_CHART = EmojiIcon(
        icon = Icons.Filled.BarChart,
        color = Color(0xFF4CAF50), // Green
        contentDescription = "Chart"
    )
    
    val LABEL = EmojiIcon(
        icon = Icons.AutoMirrored.Filled.Label,
        color = Color(0xFF9C27B0), // Purple
        contentDescription = "Label"
    )
    
    val STAR = EmojiIcon(
        icon = Icons.Filled.Star,
        color = Color(0xFFFFEB3B), // Yellow
        contentDescription = "Star"
    )
    
    val FILM_FRAMES = EmojiIcon(
        icon = Icons.Filled.Theaters,
        color = Color(0xFF795548), // Brown
        contentDescription = "Film"
    )
    
    val CROSS_MARK = EmojiIcon(
        icon = Icons.Filled.Cancel,
        color = Color(0xFFF44336), // Red
        contentDescription = "Error"
    )
    
    val FLOPPY_DISK = EmojiIcon(
        icon = Icons.Filled.Save,
        color = Color(0xFF2196F3), // Blue
        contentDescription = "Save"
    )
    
    val PENCIL = EmojiIcon(
        icon = Icons.Filled.Edit,
        color = Color(0xFF607D8B), // Blue Grey
        contentDescription = "Edit"
    )
    
    val WARNING = EmojiIcon(
        icon = Icons.Filled.Warning,
        color = Color(0xFFFF9800), // Orange
        contentDescription = "Warning"
    )
    
    val WASTEBASKET = EmojiIcon(
        icon = Icons.Filled.Delete,
        color = Color(0xFF9E9E9E), // Grey
        contentDescription = "Delete"
    )
    
    val INFO = EmojiIcon(
        icon = Icons.Filled.Info,
        color = Color(0xFF03A9F4), // Light Blue
        contentDescription = "Information"
    )
    
    val CALENDAR = EmojiIcon(
        icon = Icons.Filled.CalendarToday,
        color = Color(0xFFF44336), // Red
        contentDescription = "Calendar"
    )
}
