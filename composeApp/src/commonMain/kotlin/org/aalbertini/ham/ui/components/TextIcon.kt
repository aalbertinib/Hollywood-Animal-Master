package org.aalbertini.ham.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * Renders text with an optional leading icon.
 * Useful for displaying emoji-like icons inline with text.
 */
@Composable
fun TextIcon(
    text: String,
    modifier: Modifier = Modifier,
    icon: EmojiIcon? = null,
    color: Color = Color.Unspecified,
    style: TextStyle = LocalTextStyle.current,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {

    if (icon != null) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            EmojiIcon(
                emojiIcon = icon,
                modifier = Modifier,
                tint = if (color != Color.Unspecified) color else null
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                color = color,
                style = style,
                fontWeight = fontWeight,
                textAlign = textAlign,
                softWrap = softWrap,
                maxLines = maxLines,
                overflow = overflow
            )
        }
    } else {
        Text(
            text = text,
            modifier = modifier,
            color = color,
            style = style,
            fontWeight = fontWeight,
            textAlign = textAlign,
            softWrap = softWrap,
            maxLines = maxLines,
            overflow = overflow
        )
    }
}
