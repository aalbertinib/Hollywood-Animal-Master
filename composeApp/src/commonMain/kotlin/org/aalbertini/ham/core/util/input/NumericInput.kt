package org.aalbertini.ham.core.util.input

private val numericPattern = Regex("^\\d*\\.?\\d*$")

/**
 * Remove common thousands separators and spaces to keep numeric input canonical.
 */
fun sanitizeNumeric(raw: String): String = raw.replace(",", "").replace(" ", "")

/**
 * True if the string contains only digits and at most one decimal point.
 */
fun isNumeric(raw: String): Boolean = numericPattern.matches(raw)
