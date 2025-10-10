package org.aalbertini.ham.ui.components

import org.aalbertini.ham.MovieDistributionConstants

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
    const val SCREEN_TITLE = "Hollywood Animal - Movie Weekly Distribution Calculator"
    val SCREEN_TITLE_ICON = AppEmojiIcons.CLAPPER_BOARD

    // Section titles
    const val SECTION_PARAMETERS = "Movie Parameters"
    val SECTION_PARAMETERS_ICON = AppEmojiIcons.CAMERA_ROLL
    const val SECTION_RESULTS = "Total of other Screenings Per Week to buy"
    val SECTION_RESULTS_ICON = AppEmojiIcons.BAR_CHART

    // Actions
    const val ACTION_NEW_MOVIE = "New movie"
    const val ACTION_SAVE_MOVIE = "Save movie"
    const val ACTION_REVERT_TO_ORIGINAL = "Revert to original title"
    const val ACTION_COPY_RESULTS = "Copy results"
    const val ACTION_EXPAND = "Expand"
    const val ACTION_COLLAPSE = "Collapse"
    const val ACTION_LOAD_MOVIE = "Load Movie Parameters"
    const val ACTION_EDIT_MOVIE = "Edit Movie Parameters"
    const val ACTION_DELETE_MOVIE = "Delete Movie Parameters"
    
    /**
     * Clear all action text with item count
     */
    fun actionClearAll(count: Int) = "Clear All ($count)"

    // Theme
    const val ACTION_SWITCH_TO_LIGHT = "Switch to Light Mode"
    const val ACTION_SWITCH_TO_DARK = "Switch to Dark Mode"
    const val ACTION_ENABLE_ALWAYS_ON_TOP = "Enable Always on Top"
    const val ACTION_DISABLE_ALWAYS_ON_TOP = "Disable Always on Top"

    // Labels
    const val LABEL_MOVIE_NAME = "Movie Name (only for save)"
    val LABEL_MOVIE_NAME_ICON = AppEmojiIcons.LABEL
    const val LABEL_COMMERCIAL_SCORE = "Commercial score"
    val LABEL_COMMERCIAL_SCORE_ICON = AppEmojiIcons.STAR
    const val LABEL_SCREENINGS = "Your screenings"
    val LABEL_SCREENINGS_ICON = AppEmojiIcons.FILM_FRAMES

    /**
     * Commercial score helper text with validation range
     */
    fun helperCommercialScore(
        min: Double = MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MIN,
        max: Double = MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MAX
    ): String = "$min–${max.toInt()}"
    
    /**
     * Number of screenings helper text with validation range
     */
    fun helperNumberOfScreenings(
        min: Double = MovieDistributionConstants.Validation.SCREENINGS_MIN,
        max: Double = MovieDistributionConstants.Validation.SCREENINGS_MAX
    ): String {
        val minFormatted = if (min == 0.0) "0" else min.toInt().toString()
        val maxFormatted = formatNumber(max.toInt())
        return "$minFormatted–$maxFormatted"
    }

    // Error messages - Dynamic based on validation constants
    /**
     * Commercial score error message with validation range from constants
     */
    fun errorCommercialScore(
        min: Double = MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MIN,
        max: Double = MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MAX
    ): String = "Enter a number between $min and ${max.toInt()}"

    /**
     * Number of screenings error message with validation range from constants
     */
    fun errorNumberOfScreenings(
        min: Double = MovieDistributionConstants.Validation.SCREENINGS_MIN,
        max: Double = MovieDistributionConstants.Validation.SCREENINGS_MAX
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
    val DIALOG_TITLE_SAVE_MOVIE_ICON = AppEmojiIcons.FLOPPY_DISK
    const val DIALOG_TITLE_UPDATE_MOVIE = "Update Movie"
    val DIALOG_TITLE_UPDATE_MOVIE_ICON = AppEmojiIcons.FLOPPY_DISK
    const val DIALOG_TITLE_EDIT_MOVIE = "Edit Movie"
    val DIALOG_TITLE_EDIT_MOVIE_ICON = AppEmojiIcons.PENCIL
    const val DIALOG_TITLE_OVERWRITE_MOVIE = "Overwrite Existing Movie?"
    val DIALOG_TITLE_OVERWRITE_MOVIE_ICON = AppEmojiIcons.WARNING
    const val DIALOG_TITLE_CLEAR_ALL = "Clear All Movies?"
    val DIALOG_TITLE_CLEAR_ALL_ICON = AppEmojiIcons.WASTEBASKET
    
    // Field labels
    const val LABEL_TITLE = "Title"
    const val LABEL_COMMERCIAL_SCORE_SHORT = "Commercial score"
    const val LABEL_SCREENINGS_SHORT = "Your number of screenings"
    
    // Error and warning messages
    const val ERROR_TITLE_EXISTS = "A movie with this title already exists"
    val ERROR_TITLE_EXISTS_ICON = AppEmojiIcons.WARNING
    const val WARNING_CANNOT_UNDO = "This action cannot be undone."
    val WARNING_ICON = AppEmojiIcons.WARNING
    
    // Info messages
    const val INFO_NO_SAVED_MOVIES = "No saved movies yet. Save your first movie using the save button above."
    val INFO_NO_SAVED_MOVIES_ICON = AppEmojiIcons.INFO
    
    // Content descriptions (describe what the icon IS, not what it DOES)
    const val CONTENT_DESC_EXPAND = "Expand"
    const val CONTENT_DESC_COLLAPSE = "Collapse"
    const val CONTENT_DESC_CLEAR_ALL = "Clear all"
    const val CONTENT_DESC_LIGHT_MODE = "Light mode"
    const val CONTENT_DESC_DARK_MODE = "Dark mode"
    const val CONTENT_DESC_PINNED = "Pinned"
    const val CONTENT_DESC_UNPINNED = "Unpinned"

    // Movie Results
    const val MOVIE_RESULTS_SCREENINGS = "Other Screenings to buy"
    val MOVIE_RESULTS_SCREENINGS_ICON = AppEmojiIcons.FILM_FRAMES
    const val MOVIE_RESULTS_YOUR_SCREENINGS_OVERRIDE = "Your Screenings override"

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
        return "This will permanently delete all $count saved movie$plural."
    }
    
    /**
     * Results placeholder message with validation ranges
     */
    fun messageEnterValidParameters(
        minScore: Double = MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MIN,
        maxScore: Double = MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MAX,
        minScreenings: Double = MovieDistributionConstants.Validation.SCREENINGS_MIN
    ): String = "Enter valid Commercial score ($minScore–${maxScore.toInt()}) and your number of screenings (>= ${minScreenings.toInt()}) to see results."
    
    /**
     * Week number label
     */
    fun weekNumber(week: Int) = "Week $week"
    val WEEK_NUMBER_ICON = AppEmojiIcons.CALENDAR
    
    /**
     * MovieResult details (commercial score and screenings)
     */
    fun movieResultCommercialScore(commercialScore: Double) =
        "Commercial: $commercialScore"
    val MOVIE_RESULT_COMMERCIAL_SCORE_ICON = AppEmojiIcons.STAR

    fun movieResultNumberOfScreening(numberOfScreenings: Double) =
        "Screenings: ${formatNumber(numberOfScreenings.toInt())}"
    val MOVIE_RESULT_SCREENINGS_ICON = AppEmojiIcons.FILM_FRAMES

    // Saved movie results
    fun savedMoviesCount(count: Int) = "Saved Movies ($count)"
    val SAVED_MOVIES_COUNT_ICON = AppEmojiIcons.FLOPPY_DISK
    
    // Error icons
    val ERROR_ICON = AppEmojiIcons.CROSS_MARK
}

