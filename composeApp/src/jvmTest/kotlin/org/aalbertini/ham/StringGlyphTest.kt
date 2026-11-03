package org.aalbertini.ham

import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * The web build draws text with Compose's bundled font, which has no fallback.
 * Characters outside it render as a box (seen with ⅓, → and ≈), so strings must stick to
 * ASCII plus the few typographic characters checked on screen.
 */
class StringGlyphTest {

    private val allowed = setOf('×', '·', '–', '−', '’', '“', '”')

    @Test
    fun strings_onlyUseGlyphsTheWebFontHas() {
        val strings = File("src/commonMain/composeResources/values/strings.xml").readText()
        val offenders = strings.filter { it.code > 127 && it !in allowed }.toSet()
        assertTrue(offenders.isEmpty(), "Unsupported characters in strings.xml: $offenders")
    }
}
