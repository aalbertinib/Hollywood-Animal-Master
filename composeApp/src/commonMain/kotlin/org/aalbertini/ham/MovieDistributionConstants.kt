package org.aalbertini.ham

/**
 * Business logic constants for calculations.
 * These constants are used for computing weekly results and validation.
 * 
 * Keep these separate from UI constants to maintain proper separation of concerns.
 */
object MovieDistributionConstants {
    /**
     * Multipliers used in weekly calculations
     */
    object Multipliers {
        /** Multiplier for week 1 calculation (commercialScore * WEEK_ONE * BASE) */
        const val WEEK_ONE = 2
        
        /** Multiplier for week 2 calculation (commercialScore * WEEK_TWO * BASE) */
        const val WEEK_TWO = 1
        
        /** Base multiplier applied to all calculations */
        const val BASE = 1000
    }
    
    /**
     * Weekly calculation parameters
     */
    object WeeklyCalculation {
        /** Total number of weeks to calculate */
        const val NUMBER_OF_WEEKS = 8
        
        /** Weekly reduction rate (20% = 0.8 remaining) */
        const val WEEKLY_REDUCTION_RATE = 0.8
        
        /** Starting week for reduction calculations (week 3 is index 2) */
        const val REDUCTION_START_INDEX = 2
    }
    
    /**
     * Rounding rules for weekly results
     */
    object Rounding {
        /** Index boundary: weeks 1-4 (indices 0-3) round up, weeks 5-8 (indices 4-7) round down */
        const val ROUND_UP_UNTIL_INDEX = 4
    }
    
    /**
     * Validation ranges for input parameters
     */
    object Validation {
        /** Minimum commercial score value */
        const val COMMERCIAL_SCORE_MIN = 0.1
        
        /** Maximum commercial score value */
        const val COMMERCIAL_SCORE_MAX = 10.0
        
        /** Minimum number of seats value */
        const val SEATS_MIN = 0.0
        
        /** Maximum number of seats value */
        const val SEATS_MAX = 1_000_000.0
    }
}
