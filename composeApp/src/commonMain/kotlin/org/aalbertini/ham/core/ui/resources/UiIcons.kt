package org.aalbertini.ham.core.ui.resources

import org.aalbertini.ham.core.ui.components.AppEmojiIcons

/**
 * UI Icon constants to avoid duplication.
 * Centralizes all emoji icon definitions used throughout the application.
 * 
 * Thread-safe singleton object following best practices.
 */
object UiIcons {
    // Screen
    val SCREEN_TITLE = AppEmojiIcons.CLAPPER_BOARD

    // Sections
    val SECTION_PARAMETERS = AppEmojiIcons.CAMERA_ROLL
    val SECTION_RESULTS = AppEmojiIcons.BAR_CHART

    // Labels
    val LABEL_MOVIE_NAME = AppEmojiIcons.LABEL
    val LABEL_COMMERCIAL_SCORE = AppEmojiIcons.STAR
    val LABEL_SCREENINGS = AppEmojiIcons.FILM_FRAMES

    // Dialogs
    val DIALOG_SAVE_MOVIE = AppEmojiIcons.FLOPPY_DISK
    val DIALOG_UPDATE_MOVIE = AppEmojiIcons.FLOPPY_DISK
    val DIALOG_EDIT_MOVIE = AppEmojiIcons.PENCIL
    val DIALOG_OVERWRITE_MOVIE = AppEmojiIcons.WARNING
    val DIALOG_CLEAR_ALL = AppEmojiIcons.WASTEBASKET

    // Status and messages
    val ERROR = AppEmojiIcons.CROSS_MARK
    val WARNING = AppEmojiIcons.WARNING
    val INFO = AppEmojiIcons.INFO

    // Movie results
    val MOVIE_RESULTS_SCREENINGS = AppEmojiIcons.FILM_FRAMES
    val MOVIE_RESULT_COMMERCIAL_SCORE = AppEmojiIcons.STAR
    val MOVIE_RESULT_SCREENINGS = AppEmojiIcons.FILM_FRAMES
    val SAVED_MOVIES_COUNT = AppEmojiIcons.FLOPPY_DISK

    // Calendar and time
    val WEEK_NUMBER = AppEmojiIcons.CALENDAR
}
