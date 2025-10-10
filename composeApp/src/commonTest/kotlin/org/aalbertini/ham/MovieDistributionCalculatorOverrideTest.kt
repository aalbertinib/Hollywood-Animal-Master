package org.aalbertini.ham

import kotlin.test.Test
import kotlin.test.assertEquals

class MovieDistributionCalculatorOverrideTest {

    @Test
    fun calculateWeeklyResultsWithOverrides_noOverrides_shouldMatchDefaultBehavior() {
        val commercialScore = 1.5
        val availableScreenings = 200.0
        val overrides = emptyMap<Int, Double>()
        
        val resultWithOverrides = MovieDistributionCalculator.calculateWeeklyResultsWithOverrides(
            commercialScore, availableScreenings, overrides
        )
        val resultDefault = MovieDistributionCalculator.calculateWeeklyResults(
            commercialScore, availableScreenings
        )
        
        assertEquals(8, resultWithOverrides.size)
        assertEquals(resultDefault, resultWithOverrides)
    }

    @Test
    fun calculateWeeklyResultsWithOverrides_overrideWeek1Only() {
        val commercialScore = 1.5
        val availableScreenings = 200.0
        val overrides = mapOf(0 to 1.0)
        
        val result = MovieDistributionCalculator.calculateWeeklyResultsWithOverrides(
            commercialScore, availableScreenings, overrides
        )
        
        // Week 1 with override: ((1.5 * 2 * 1000) - 1).coerceAtLeast(0.0) = 2999.0
        assertEquals(2999.0, result[0], 1e-9)
        
        // Week 2 should use default: ((1.5 * 1000) - 200).coerceAtLeast(0.0) = 1300.0
        assertEquals(1300.0, result[1], 1e-9)
        
        // Weeks 3-8 should follow 20% reduction from week 2
        assertEquals(1040.0, result[2], 1e-9) // 1300 * 0.8
        assertEquals(832.0, result[3], 1e-9)  // 1040 * 0.8
    }

    @Test
    fun calculateWeeklyResultsWithOverrides_overrideWeek2Only() {
        val commercialScore = 1.5
        val availableScreenings = 200.0
        val overrides = mapOf(1 to 1.0)
        
        val result = MovieDistributionCalculator.calculateWeeklyResultsWithOverrides(
            commercialScore, availableScreenings, overrides
        )
        
        // Week 1 should use default: ((1.5 * 2 * 1000) - 200).coerceAtLeast(0.0) = 2800.0
        assertEquals(2800.0, result[0], 1e-9)
        
        // Week 2 with override: ((1.5 * 1000) - 1).coerceAtLeast(0.0) = 1499.0
        assertEquals(1499.0, result[1], 1e-9)
        
        // Weeks 3-8 should follow 20% reduction from week 2 (using the overridden week 2)
        // The reduction chain continues from the overridden week 2 value
        assertEquals(1199.2, result[2], 1e-9) // Based on overridden week2 (1499) * 0.8
    }

    @Test
    fun calculateWeeklyResultsWithOverrides_overrideWeek3_appliesReduction() {
        val commercialScore = 1.5
        val availableScreenings = 200.0
        val overrides = mapOf(2 to 1.0)
        
        val result = MovieDistributionCalculator.calculateWeeklyResultsWithOverrides(
            commercialScore, availableScreenings, overrides
        )
        
        // Weeks 1-2 should use defaults
        assertEquals(2800.0, result[0], 1e-9)
        assertEquals(1300.0, result[1], 1e-9)
        
        // Week 3 with override: Revenue is (1.5 * 1000) * 0.8 = 1200
        // Result: (1200 - 1).coerceAtLeast(0.0) = 1199.0
        assertEquals(1199.0, result[2], 1e-9)
        
        // Week 4 uses default reduction chain: 1300 * 0.8 * 0.8 = 832.0
        assertEquals(832.0, result[3], 1e-9)
    }

    @Test
    fun calculateWeeklyResultsWithOverrides_consecutiveOverrides_eachAppliesReduction() {
        val commercialScore = 1.5
        val availableScreenings = 4200.0
        val overrides = mapOf(
            5 to 1.0,  // Week 6
            6 to 1.0,  // Week 7
            7 to 1.0   // Week 8
        )
        
        val result = MovieDistributionCalculator.calculateWeeklyResultsWithOverrides(
            commercialScore, availableScreenings, overrides
        )
        
        // Week 5 default: Commercial 1.5, screenings 4200
        // Week 2 base: (1.5 * 1000) - 4200 = -2700 -> 0
        // Reduction chain: 0 * 0.8 * 0.8 * 0.8 = 0
        assertEquals(0.0, result[4], 1e-9)
        
        // Week 6 override with 20% * 20% * 20% * 20% reduction applied
        // Revenue multiplier: 1.0 * 0.8 * 0.8 * 0.8 * 0.8 = 0.4096
        // Result: ((1.5 * 1000) * 0.4096 - 1).coerceAtLeast(0.0) = 613.4 - 1 = 612.4
        assertEquals(613.4, result[5], 1e-9)
        
        // Week 7 override with one more 20% reduction
        // Revenue multiplier: 0.4096 * 0.8 = 0.32768
        // Result: ((1.5 * 1000) * 0.32768 - 1).coerceAtLeast(0.0) = 491.52 - 1 = 490.52
        assertEquals(490.52, result[6], 1e-9)
        
        // Week 8 override with one more 20% reduction
        // Revenue multiplier: 0.32768 * 0.8 = 0.262144
        // Result: ((1.5 * 1000) * 0.262144 - 1).coerceAtLeast(0.0) = 393.216 - 1 = 392.216
        assertEquals(392.216, result[7], 1e-9)
    }

    @Test
    fun calculateWeeklyResultsWithOverrides_mixedOverridesAndDefaults() {
        val commercialScore = 2.0
        val availableScreenings = 100.0
        val overrides = mapOf(
            0 to 50.0,   // Week 1 override
            2 to 75.0,   // Week 3 override
            4 to 25.0,   // Week 5 override
            6 to 10.0    // Week 7 override
        )
        
        val result = MovieDistributionCalculator.calculateWeeklyResultsWithOverrides(
            commercialScore, availableScreenings, overrides
        )
        
        // Week 1 override: ((2.0 * 2 * 1000) - 50).coerceAtLeast(0.0) = 3950.0
        assertEquals(3950.0, result[0], 1e-9)
        
        // Week 2 default: ((2.0 * 1000) - 100).coerceAtLeast(0.0) = 1900.0
        assertEquals(1900.0, result[1], 1e-9)
        
        // Week 3 override: Revenue = 2.0 * 1000 * 0.8 = 1600
        // Result: (1600 - 75).coerceAtLeast(0.0) = 1525.0
        assertEquals(1525.0, result[2], 1e-9)
        
        // Week 4 default: 1900 * 0.8 * 0.8 = 1216.0
        assertEquals(1216.0, result[3], 1e-9)
        
        // Week 5 override: Revenue = 2.0 * 1000 * 0.8 * 0.8 * 0.8 = 1024
        // Result: (1024 - 25).coerceAtLeast(0.0) = 999.0
        assertEquals(999.0, result[4], 1e-9)
        
        // Week 6 default: 1900 * 0.8 * 0.8 * 0.8 * 0.8 = 778.24
        assertEquals(778.24, result[5], 1e-9)
        
        // Week 7 override: Revenue = 2.0 * 1000 * 0.8^5 = 655.36
        // Result: (655.36 - 10).coerceAtLeast(0.0) = 645.36
        assertEquals(645.36, result[6], 1e-9)
        
        // Week 8 default: 1900 * 0.8^6 = 498.0736
        assertEquals(498.0736, result[7], 1e-9)
    }

    @Test
    fun calculateWeeklyResultsWithOverrides_allWeeksOverridden() {
        val commercialScore = 1.0
        val availableScreenings = 500.0
        val overrides = mapOf(
            0 to 100.0,
            1 to 200.0,
            2 to 150.0,
            3 to 125.0,
            4 to 100.0,
            5 to 75.0,
            6 to 50.0,
            7 to 25.0
        )
        
        val result = MovieDistributionCalculator.calculateWeeklyResultsWithOverrides(
            commercialScore, availableScreenings, overrides
        )
        
        // Week 1: ((1.0 * 2 * 1000) - 100).coerceAtLeast(0.0) = 1900.0
        assertEquals(1900.0, result[0], 1e-9)
        
        // Week 2: ((1.0 * 1000) - 200).coerceAtLeast(0.0) = 800.0
        assertEquals(800.0, result[1], 1e-9)
        
        // Week 3: Revenue = 1.0 * 1000 * 0.8 = 800
        // Result: (800 - 150).coerceAtLeast(0.0) = 650.0
        assertEquals(650.0, result[2], 1e-9)
        
        // Week 4: Revenue = 1.0 * 1000 * 0.8 * 0.8 = 640
        // Result: (640 - 125).coerceAtLeast(0.0) = 515.0
        assertEquals(515.0, result[3], 1e-9)
    }

    @Test
    fun calculateWeeklyResultsWithOverrides_negativeResultsCoercedToZero() {
        val commercialScore = 0.5
        val availableScreenings = 100.0
        val overrides = mapOf(
            2 to 800.0,  // Week 3 - very high override to force negative
            5 to 500.0   // Week 6 - also high
        )
        
        val result = MovieDistributionCalculator.calculateWeeklyResultsWithOverrides(
            commercialScore, availableScreenings, overrides
        )
        
        // Week 3: Revenue = 0.5 * 1000 * 0.8 = 400
        // Result: (400 - 800).coerceAtLeast(0.0) = 0.0
        assertEquals(0.0, result[2], 1e-9)
        
        // Week 6: Revenue = 0.5 * 1000 * 0.8^4 = 204.8
        // Result: (204.8 - 500).coerceAtLeast(0.0) = 0.0
        assertEquals(0.0, result[5], 1e-9)
    }

    @Test
    fun calculateWeeklyResultsWithOverrides_overrideDoesNotAffectReductionChain() {
        val commercialScore = 3.0
        val availableScreenings = 500.0
        val overrides = mapOf(3 to 1.0) // Week 4 override
        
        val result = MovieDistributionCalculator.calculateWeeklyResultsWithOverrides(
            commercialScore, availableScreenings, overrides
        )
        
        // Week 2 default: (3.0 * 1000 - 500) = 2500.0
        assertEquals(2500.0, result[1], 1e-9)
        
        // Week 3 default (no override): 2500 * 0.8 = 2000.0
        assertEquals(2000.0, result[2], 1e-9)
        
        // Week 4 with override, but reduction chain continues
        // Revenue multiplier: 1.0 * 0.8 * 0.8 = 0.64
        // Result: (3.0 * 1000 * 0.64 - 1) = 1919.0
        assertEquals(1919.0, result[3], 1e-9)
        
        // Week 5 default: Should continue from normal reduction chain
        // 2500 * 0.8 * 0.8 * 0.8 = 1280.0
        assertEquals(1280.0, result[4], 1e-9)
        
        // Week 6 default: 2500 * 0.8^4 = 1024.0
        assertEquals(1024.0, result[5], 1e-9)
    }
}
