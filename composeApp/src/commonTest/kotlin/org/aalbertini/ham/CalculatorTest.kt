package org.aalbertini.ham

import kotlin.test.Test
import kotlin.test.assertEquals

class CalculatorTest {

    @Test
    fun calculateWeeklyResults_example() {
        val p1 = 1.5
        val p2 = 200.0
        val res = calculateWeeklyResults(p1, p2)
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
    fun calculateWeeklyResults_abs_applied() {
        val p1 = 0.1
        val p2 = 500.0
        val res = calculateWeeklyResults(p1, p2)
        // Week 1: abs((0.1 * 2 * 1000) - 500) = abs(200 - 500) = 300
        // Week 2: abs((0.1 * 1000) - 500) = abs(100 - 500) = 400
        assertEquals(300.0, res[0], 1e-9)
        assertEquals(400.0, res[1], 1e-9)
        assertEquals(320.0, res[2], 1e-9)
    }

    @Test
    fun filterNumericInput_examples() {
        assertEquals("123.45", filterNumericInput("12a3,45x"))
        assertEquals("12.3456", filterNumericInput("-12.34.56")) // minus removed, digits kept, single dot
        assertEquals("0.5", filterNumericInput("-.5")) // minus removed, leading 0 added
        assertEquals("1.23", filterNumericInput("1,2,3")) // only first dot kept
    }
}
