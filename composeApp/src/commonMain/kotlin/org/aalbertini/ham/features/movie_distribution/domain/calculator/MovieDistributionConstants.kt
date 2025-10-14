package org.aalbertini.ham.features.movie_distribution.domain.calculator

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
        
        /**
         * Default week multipliers for all 8 weeks.
         * Week 1: 2.0 (WEEK_ONE)
         * Week 2: 1.0 (WEEK_TWO)
         * Week 3-8: Each week is 80% of previous (0.8, 0.64, 0.512, 0.4096, 0.32768, 0.262144)
         */
        val DEFAULT_WEEK_MULTIPLIERS = listOf(
            WEEK_ONE.toDouble(),     // Week 1: 2.0
            WEEK_TWO.toDouble(),     // Week 2: 1.0
            0.8,                      // Week 3: 1.0 * 0.8
            0.64,                     // Week 4: 0.8 * 0.8
            0.512,                    // Week 5: 0.64 * 0.8
            0.4096,                   // Week 6: 0.512 * 0.8
            0.32768,                  // Week 7: 0.4096 * 0.8
            0.262144                  // Week 8: 0.32768 * 0.8
        )
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
        
        /** Minimum number of screenings value */
        const val SCREENINGS_MIN = 0.0
        
        /** Maximum number of screenings value */
        const val SCREENINGS_MAX = 1_000_000.0
    }
    
    /**
     * Default values for new movie calculations
     */
    object Defaults {
        /** Default commercial score for new movies */
        const val COMMERCIAL_SCORE = 5.0
        
        /** Default number of available screenings for new movies */
        const val AVAILABLE_SCREENINGS = 3_200
    }
}
