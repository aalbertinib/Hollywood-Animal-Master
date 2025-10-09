package org.aalbertini.ham.ui.components

import org.aalbertini.ham.CalculationConstants

/**
 * UI String constants to avoid duplication.
 * Functions that take parameters ensure strings stay synchronized with business logic constants.
 */
object UiStrings {
    /**
     * Formats a number with thousand separators (KMP-compatible)
     */
    fun formatNumber(number: Long): String {
        val str = number.toString()
        return str.reversed().chunked(3).joinToString(",").reversed()
    }
    
    /**
     * Formats an integer with thousand separators (KMP-compatible)
     */
    fun formatNumber(number: Int): String = formatNumber(number.toLong())
    // Screen titles
    const val SCREEN_TITLE = "Movie Weekly Distribution Calculator"

    // Section titles
    const val SECTION_PARAMETERS = "Movie Parameters"
    const val SECTION_RESULTS = "Total of other Seats Per Week to buy"

    // Actions
    const val ACTION_NEW_MOVIE = "New movie"
    const val ACTION_SAVE_MOVIE = "Save movie"
    const val ACTION_REVERT_TO_ORIGINAL = "Revert to original title"
    const val ACTION_COPY_RESULTS = "Copy results"
    const val ACTION_EXPAND = "Expand"
    const val ACTION_COLLAPSE = "Collapse"
    const val ACTION_CLEAR_ALL = "Clear all saved movies"
    const val ACTION_LOAD_MOVIE = "Load Movie Parameters"
    const val ACTION_EDIT_MOVIE = "Edit Movie Parameters"
    const val ACTION_DELETE_MOVIE = "Delete Movie Parameters"

    // Theme
    const val ACTION_SWITCH_TO_LIGHT = "Switch to Light Mode"
    const val ACTION_SWITCH_TO_DARK = "Switch to Dark Mode"
    const val ACTION_ENABLE_ALWAYS_ON_TOP = "Enable Always on Top"
    const val ACTION_DISABLE_ALWAYS_ON_TOP = "Disable Always on Top"

    // Labels
    const val LABEL_MOVIE_NAME = "Movie Name (only for save)"

    /**
     * Commercial score label with validation range from constants
     */
    fun labelCommercialScore(
        min: Double = CalculationConstants.Validation.COMMERCIAL_SCORE_MIN,
        max: Double = CalculationConstants.Validation.COMMERCIAL_SCORE_MAX
    ): String = "Commercial score ($min – ${max.toInt()})"

    /**
     * Number of seats label with validation range from constants
     */
    fun labelNumberOfSeats(
        min: Double = CalculationConstants.Validation.SEATS_MIN,
        max: Double = CalculationConstants.Validation.SEATS_MAX
    ): String {
        val minFormatted = if (min == 0.0) "0" else min.toInt().toString()
        val maxFormatted = formatNumber(max.toInt())
        return "Your number of seats ($minFormatted – $maxFormatted)"
    }

    // Error messages - Dynamic based on validation constants
    /**
     * Commercial score error message with validation range from constants
     */
    fun errorCommercialScore(
        min: Double = CalculationConstants.Validation.COMMERCIAL_SCORE_MIN,
        max: Double = CalculationConstants.Validation.COMMERCIAL_SCORE_MAX
    ): String = "Enter a number between $min and ${max.toInt()}"

    /**
     * Number of seats error message with validation range from constants
     */
    fun errorNumberOfSeats(
        min: Double = CalculationConstants.Validation.SEATS_MIN,
        max: Double = CalculationConstants.Validation.SEATS_MAX
    ): String {
        val minFormatted = if (min == 0.0) "0" else min.toInt().toString()
        val maxFormatted = formatNumber(max.toInt())
        return "Enter a number between $minFormatted and $maxFormatted"
    }

    // Placeholders
    const val PLACEHOLDER_UNSAVED = "Unsaved"
    
    // Button labels
    const val BUTTON_CANCEL = "Cancel"
    const val BUTTON_SAVE = "Save"
    const val BUTTON_UPDATE = "Update"
    const val BUTTON_OVERWRITE = "Overwrite"
    const val BUTTON_CLEAR_ALL = "Clear All"
    
    // Dialog titles
    const val DIALOG_TITLE_SAVE_MOVIE = "Save Movie"
    const val DIALOG_TITLE_UPDATE_MOVIE = "Update Movie"
    const val DIALOG_TITLE_EDIT_MOVIE = "Edit Movie"
    const val DIALOG_TITLE_OVERWRITE_MOVIE = "Overwrite Existing Movie?"
    const val DIALOG_TITLE_CLEAR_ALL = "Clear All Movies?"
    
    // Field labels
    const val LABEL_TITLE = "Title"
    const val LABEL_COMMERCIAL_SCORE_SHORT = "Commercial score"
    const val LABEL_SEATS_SHORT = "Your number of seats"
    
    // Error and warning messages
    const val ERROR_TITLE_EXISTS = "A movie with this title already exists"
    const val WARNING_CANNOT_UNDO = "This action cannot be undone."
    
    // Info messages
    const val INFO_NO_SAVED_MOVIES = "No saved movies yet. Save your first movie using the save button above."
    
    // Content descriptions (describe what the icon IS, not what it DOES)
    const val CONTENT_DESC_EXPAND = "Expand"
    const val CONTENT_DESC_COLLAPSE = "Collapse"
    const val CONTENT_DESC_CLEAR_ALL = "Clear all"
    const val CONTENT_DESC_LIGHT_MODE = "Light mode"
    const val CONTENT_DESC_DARK_MODE = "Dark mode"
    const val CONTENT_DESC_PINNED = "Pinned"
    const val CONTENT_DESC_UNPINNED = "Unpinned"
    
    // Functions with parameters
    /**
     * Overwrite confirmation message with movie title
     */
    fun messageOverwriteMovie(title: String) = "A movie with the title \"$title\" already exists. Do you want to overwrite it?"
    
    /**
     * Clear all confirmation message with count
     */
    fun messageClearAll(count: Int): String {
        val plural = if (count != 1) "s" else ""
        return "⚠️ This will permanently delete all $count saved movie$plural."
    }
    
    /**
     * Results placeholder message with validation ranges
     */
    fun messageEnterValidParameters(
        minScore: Double = CalculationConstants.Validation.COMMERCIAL_SCORE_MIN,
        maxScore: Double = CalculationConstants.Validation.COMMERCIAL_SCORE_MAX,
        minSeats: Double = CalculationConstants.Validation.SEATS_MIN
    ): String = "Enter valid Commercial score ($minScore–${maxScore.toInt()}) and your number of seats (>= ${minSeats.toInt()}) to see results."
    
    /**
     * Week number label
     */
    fun weekNumber(week: Int) = "Week $week"
    
    /**
     * MovieResult details (commercial score and seats)
     */
    fun movieResultDetails(commercialScore: Double, numberOfSeats: Double) = 
        "Commercial: $commercialScore, Seats: ${formatNumber(numberOfSeats.toInt())}"

    // Saved movie results
    fun savedMoviesCount(count: Int) = "Saved Movies ($count)"
}