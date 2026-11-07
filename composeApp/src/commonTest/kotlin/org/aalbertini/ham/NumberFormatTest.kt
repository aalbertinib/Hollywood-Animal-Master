package org.aalbertini.ham

import org.aalbertini.ham.core.util.format.grouped
import org.aalbertini.ham.core.util.format.signedGrouped
import org.aalbertini.ham.core.util.format.toPlainString
import kotlin.test.Test
import kotlin.test.assertEquals

class NumberFormatTest {

    @Test
    fun grouped_handlesSignAndSize() {
        assertEquals("0", 0L.grouped())
        assertEquals("999", 999L.grouped())
        assertEquals("12,345", 12345L.grouped())
        assertEquals("-1,200", (-1200L).grouped())
    }

    @Test
    fun signedGrouped_showsDirection() {
        assertEquals("+2,400", 2400L.signedGrouped())
        assertEquals("−1,200", (-1200L).signedGrouped())
        assertEquals("0", 0L.signedGrouped())
    }

    @Test
    fun plainString_neverUsesExponentOrTrailingZero() {
        assertEquals("3200", 3200.0.toPlainString())
        assertEquals("6.5", 6.5.toPlainString())
        assertEquals("10000000", 1.0e7.toPlainString())
        assertEquals("0.33", (1.0 / 3).toPlainString())
        assertEquals("0", (-0.001).toPlainString())
    }
}
