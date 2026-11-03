package org.aalbertini.ham

import org.aalbertini.ham.core.ui.components.filter
import kotlin.test.Test
import kotlin.test.assertEquals

class NumberFieldFilterTest {

    @Test
    fun integers_dropGroupingAndSeparators() {
        // V1 turned a pasted "3,200" into 3.2 screenings.
        assertEquals("3200", filter("3,200", decimals = 0))
        assertEquals("3200", filter("3 200", decimals = 0))
        assertEquals("1000000", filter("1.000.000", decimals = 0))
    }

    @Test
    fun decimals_acceptCommaOrDot_andLimitPlaces() {
        assertEquals("6.5", filter("6,5", decimals = 1))
        assertEquals("6.5", filter("6.58", decimals = 1))
        assertEquals("0.5", filter(".5", decimals = 1))
        assertEquals("12.3", filter("-1a2.3.4", decimals = 1))
    }
}
