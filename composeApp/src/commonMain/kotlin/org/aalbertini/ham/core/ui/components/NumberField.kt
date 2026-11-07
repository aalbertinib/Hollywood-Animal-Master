package org.aalbertini.ham.core.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import hollywoodanimalmaster.composeapp.generated.resources.Res
import hollywoodanimalmaster.composeapp.generated.resources.error_number
import hollywoodanimalmaster.composeapp.generated.resources.error_range
import org.aalbertini.ham.core.util.format.toPlainString
import org.jetbrains.compose.resources.stringResource

/**
 * Numeric text field that owns its text and only reports valid numbers.
 *
 * - `,` and `.` are both accepted as the decimal separator; grouping spaces/commas are dropped
 *   when [decimals] is 0, so pasting `3,200` from the game gives 3200 (V1 turned it into 3.2).
 * - Values loaded from outside are shown without a trailing `.0` (V1 showed `3200.0`, and the next
 *   keystroke through its integer filter became `32000`).
 * - Out-of-range or partial input stays visible with an error and is never reported;
 *   an empty field reports `null` when [allowEmpty] is true.
 */
@Composable
fun NumberField(
    value: Double?,
    onValueChange: (Double?) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    min: Double = 0.0,
    max: Double = Double.MAX_VALUE,
    decimals: Int = 0,
    allowEmpty: Boolean = false,
    supportingText: String? = null,
    placeholder: String? = null,
    suffix: String? = null,
    imeAction: ImeAction = ImeAction.Next,
) {
    var text by remember { mutableStateOf(value.format(decimals)) }

    // External change (load, reset, new movie): show it unless the text already means the same.
    LaunchedEffect(value) {
        if (parse(text) != value) text = value.format(decimals)
    }

    val parsed = parse(text)
    val error = when {
        text.isEmpty() -> if (allowEmpty) null else stringResource(Res.string.error_number)
        parsed == null -> stringResource(Res.string.error_number)
        parsed < min || parsed > max ->
            stringResource(Res.string.error_range, min.toPlainString(decimals), max.toPlainString(decimals))
        else -> null
    }

    OutlinedTextField(
        value = text,
        onValueChange = { input ->
            val filtered = filter(input, decimals)
            text = filtered
            val number = parse(filtered)
            when {
                filtered.isEmpty() && allowEmpty -> onValueChange(null)
                number != null && number in min..max -> onValueChange(number)
            }
        },
        modifier = modifier,
        label = { Text(label) },
        placeholder = placeholder?.let { { Text(it) } },
        suffix = suffix?.let { { Text(it) } },
        supportingText = (error ?: supportingText)?.let { { Text(it) } },
        isError = error != null,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (decimals > 0) KeyboardType.Decimal else KeyboardType.Number,
            imeAction = imeAction,
        ),
    )
}

private fun Double?.format(decimals: Int): String = this?.toPlainString(decimals).orEmpty()

private fun parse(text: String): Double? = text.takeIf { it.isNotEmpty() && it != "." }?.toDoubleOrNull()

/** Digits plus at most one decimal separator (normalized to '.'), limited to [decimals] places. */
internal fun filter(input: String, decimals: Int): String {
    val out = StringBuilder()
    var separatorSeen = false
    var fractionDigits = 0
    for (c in input) {
        when {
            c.isDigit() -> {
                if (!separatorSeen) out.append(c)
                else if (fractionDigits < decimals) {
                    out.append(c)
                    fractionDigits++
                }
            }
            (c == '.' || c == ',') && decimals > 0 && !separatorSeen -> {
                if (out.isEmpty()) out.append('0')
                out.append('.')
                separatorSeen = true
            }
        }
    }
    return out.toString()
}
