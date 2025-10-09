package org.aalbertini.ham

import org.aalbertini.ham.util.filterNumericInput
import kotlin.test.Test
import kotlin.test.assertEquals

class MovieDistributionCalculatorTest {

    @Test
    fun calculateWeeklyResults_example() {
        val p1 = 1.5
        val p2 = 200.0
        val res = MovieDistributionCalculator.calculateWeeklyResults(p1, p2)
        assertEquals(8, res.size)
        assertEquals(2800.0, res[0], 1e-9)
        assertEquals(1300.0, res[1], 1e-9)
        assertEquals(1040.0, res[2], 1e-9)
        assertEquals(832.0, res[3], 1e-9)
        assertEquals(665.6, res[4], 1e-9)
        assertEquals(532.48, res[5], 1e-9)
        assertEquals(425.984, res[6], 1e-9)
        assertEquals(340.7872, res[7], 1e-9)
    }

    @Test
    fun calculateWeeklyResults_coerceNegativeToZero() {
        val p1 = 0.1
        val p2 = 500.0
        val res = MovieDistributionCalculator.calculateWeeklyResults(p1, p2)
        // Week 1: ((0.1 * 2 * 1000) - 500).coerceAtLeast(0.0) = (200 - 500).coerceAtLeast(0.0) = 0.0
        // Week 2: ((0.1 * 1000) - 500).coerceAtLeast(0.0) = (100 - 500).coerceAtLeast(0.0) = 0.0
        // Weeks 3-8: All based on week2, so all 0.0
        assertEquals(0.0, res[0], 1e-9)
        assertEquals(0.0, res[1], 1e-9)
        assertEquals(0.0, res[2], 1e-9)
        assertEquals(0.0, res[3], 1e-9)
    }

    @Test
    fun filterNumericInput_examples() {
        assertEquals("123.45", "12a3,45x".filterNumericInput())
        assertEquals("12.3456", "-12.34.56".filterNumericInput()) // minus removed, digits kept, single dot
        assertEquals("0.5", "-.5".filterNumericInput()) // minus removed, leading 0 added
        assertEquals("1.23", "1,2,3".filterNumericInput()) // only first dot kept
    }
}
