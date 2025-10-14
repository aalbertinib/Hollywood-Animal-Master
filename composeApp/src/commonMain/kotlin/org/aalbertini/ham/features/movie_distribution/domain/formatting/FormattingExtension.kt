package org.aalbertini.ham.features.movie_distribution.domain.formatting

/**
 * Formats a number with thousand separators (KMP-compatible)
 */
fun Long.formatNumberThousands(): String {
    val str = this.toString()
    return str.reversed().chunked(3).joinToString(",").reversed()
}

/**
 * Formats an integer with thousand separators (KMP-compatible)
 */
fun Int.formatNumberThousands(): String = this.toLong().formatNumberThousands()