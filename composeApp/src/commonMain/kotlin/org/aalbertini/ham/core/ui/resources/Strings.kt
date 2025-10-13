package org.aalbertini.ham.core.ui.resources

import hollywoodanimalmaster.composeapp.generated.resources.Res
import hollywoodanimalmaster.composeapp.generated.resources.*
import org.jetbrains.compose.resources.StringResource

/**
 * Centralized string resources for the Hollywood Animal Master application.
 * 
 * This object provides type-safe access to all string resources using Compose Multiplatform's
 * generated resources API (1.9.0). All strings are defined in composeResources/values/strings.xml
 * and automatically generated at build time.
 * 
 * Usage:
 * ```
 * Text(text = stringResource(Strings.appName))
 * Text(text = stringResource(Strings.totalAnimal, count))
 * ```
 */
object Strings {
    // App Name
    val appName: StringResource get() = Res.string.app_name
    val appNameShort: StringResource get() = Res.string.app_name_short
    val screenTitle: StringResource get() = Res.string.screen_title
    
    // Screen Titles
    val movieDistributionTitle: StringResource get() = Res.string.movie_distribution_title
    val statisticsTitle: StringResource get() = Res.string.statistics_title
    val settingsTitle: StringResource get() = Res.string.settings_title
    val appearanceTitle: StringResource get() = Res.string.appearance_title
    val darkModeLabel: StringResource get() = Res.string.dark_mode_label
    val lightModeLabel: StringResource get() = Res.string.light_mode_label
    val switchToLightTheme: StringResource get() = Res.string.switch_to_light_theme
    val switchToDarkTheme: StringResource get() = Res.string.switch_to_dark_theme
    val themePresetsTitle: StringResource get() = Res.string.theme_presets_title
    val chooseColorTheme: StringResource get() = Res.string.choose_color_theme
    val platformSettingsTitle: StringResource get() = Res.string.platform_settings_title
    val platformSettingsDescription: StringResource get() = Res.string.platform_settings_description
    val enabled: StringResource get() = Res.string.enabled
    
    // Movie Distribution Screen
    val parametersSectionTitle: StringResource get() = Res.string.parameters_section_title
    val movieNameLabel: StringResource get() = Res.string.movie_name_label
    val movieNameHint: StringResource get() = Res.string.movie_name_hint
    val distributionLabel: StringResource get() = Res.string.distribution_label
    val distributionUniform: StringResource get() = Res.string.distribution_uniform
    val distributionNormal: StringResource get() = Res.string.distribution_normal
    val distributionExponential: StringResource get() = Res.string.distribution_exponential
    val distributionCustom: StringResource get() = Res.string.distribution_custom
    val resultsSectionTitle: StringResource get() = Res.string.results_section_title
    val labelCommercialScore: StringResource get() = Res.string.label_commercial_score
    val labelScreenings: StringResource get() = Res.string.label_screenings
    
    // Actions
    val calculateButton: StringResource get() = Res.string.calculate_button
    val resetButton: StringResource get() = Res.string.reset_button
    val saveButton: StringResource get() = Res.string.save_button
    val loadButton: StringResource get() = Res.string.load_button
    val exportButton: StringResource get() = Res.string.export_button
    val importButton: StringResource get() = Res.string.import_button
    val clearAllButton: StringResource get() = Res.string.clear_all_button
    val actionNewMovie: StringResource get() = Res.string.action_new_movie
    val actionSaveMovie: StringResource get() = Res.string.action_save_movie
    val actionRevertToOriginal: StringResource get() = Res.string.action_revert_to_original
    val actionCopyResults: StringResource get() = Res.string.action_copy_results
    val actionExpand: StringResource get() = Res.string.action_expand
    val actionCollapse: StringResource get() = Res.string.action_collapse
    val actionLoadMovie: StringResource get() = Res.string.action_load_movie
    val actionEditMovie: StringResource get() = Res.string.action_edit_movie
    val actionDeleteMovie: StringResource get() = Res.string.action_delete_movie
    val actionSwitchToLight: StringResource get() = Res.string.action_switch_to_light
    val actionSwitchToDark: StringResource get() = Res.string.action_switch_to_dark
    val actionEnableAlwaysOnTop: StringResource get() = Res.string.action_enable_always_on_top
    val actionDisableAlwaysOnTop: StringResource get() = Res.string.action_disable_always_on_top
    val action_clear_all_with_count: StringResource get() = Res.string.action_clear_all_with_count
    
    // Results Section
    val resultsTitle: StringResource get() = Res.string.results_title
    val noResults: StringResource get() = Res.string.no_results
    val averagePerScene: StringResource get() = Res.string.average_per_scene
    val scenesCount: StringResource get() = Res.string.scenes_count
    val movieResultsScreenings: StringResource get() = Res.string.movie_results_screenings
    val movieResultsYourScreeningsOverride: StringResource get() = Res.string.movie_results_your_screenings_override
    val pendingValidation: StringResource get() = Res.string.pending_validation
    val overrideLabel: StringResource get() = Res.string.override_label
    val defaultWithValue: StringResource get() = Res.string.default_with_value
    val clearOverride: StringResource get() = Res.string.clear_override
    val validationBetweenZeroAnd: StringResource get() = Res.string.validation_between_zero_and
    val enterAvailableScreeningsFirst: StringResource get() = Res.string.enter_available_screenings_first
    val weekNumber: StringResource get() = Res.string.week_number
    val weekResultsLine: StringResource get() = Res.string.week_results_line
    val messageEnterValidParameters: StringResource get() = Res.string.message_enter_valid_parameters
    
    // Saved Distributions
    val savedDistributionsTitle: StringResource get() = Res.string.saved_distributions_title
    val noSavedDistributions: StringResource get() = Res.string.no_saved_distributions
    val infoNoSavedMovies: StringResource get() = Res.string.info_no_saved_movies
    val deleteDistribution: StringResource get() = Res.string.delete_distribution
    val deleteConfirmation: StringResource get() = Res.string.delete_confirmation
    val savedMoviesCount: StringResource get() = Res.string.saved_movies_count
    
    // Errors
    val errorInvalidInput: StringResource get() = Res.string.error_invalid_input
    val errorSaveFailed: StringResource get() = Res.string.error_save_failed
    val errorLoadFailed: StringResource get() = Res.string.error_load_failed
    val errorNetwork: StringResource get() = Res.string.error_network
    val errorTitleExists: StringResource get() = Res.string.error_title_exists
    val warningCannotUndo: StringResource get() = Res.string.warning_cannot_undo
    val errorCommercialScoreRange: StringResource get() = Res.string.error_commercial_score_range
    val errorNumberOfScreeningsRange: StringResource get() = Res.string.error_number_of_screenings_range
    
    // Validation
    val validationRequiredField: StringResource get() = Res.string.validation_required_field
    val validationPositiveNumber: StringResource get() = Res.string.validation_positive_number
    val validationInvalidRange: StringResource get() = Res.string.validation_invalid_range
    
    // Common
    val ok: StringResource get() = Res.string.ok
    val cancel: StringResource get() = Res.string.cancel
    val yes: StringResource get() = Res.string.yes
    val no: StringResource get() = Res.string.no
    val confirm: StringResource get() = Res.string.confirm
    val loading: StringResource get() = Res.string.loading
    val retry: StringResource get() = Res.string.retry
    val unsavedText: StringResource get() = Res.string.unsaved
    val update: StringResource get() = Res.string.update
    val overwrite: StringResource get() = Res.string.overwrite
    // Save/Update/Overwrite Movie Dialogs
    val dialogTitleSaveMovie: StringResource get() = Res.string.dialog_title_save_movie
    val dialogTitleUpdateMovie: StringResource get() = Res.string.dialog_title_update_movie
    val dialogTitleEditMovie: StringResource get() = Res.string.dialog_title_edit_movie
    val dialogTitleOverwriteMovie: StringResource get() = Res.string.dialog_title_overwrite_movie
    val dialogTitleClearAll: StringResource get() = Res.string.dialog_title_clear_all
    val messageOverwriteMovie: StringResource get() = Res.string.message_overwrite_movie
    
    // Parameter Comparison Dialog
    val parameterConflict: StringResource get() = Res.string.parameter_conflict
    val messageTitleExistsWithDiffParams: StringResource get() = Res.string.message_title_exists_with_diff_params
    val savedParametersTitle: StringResource get() = Res.string.saved_parameters_title
    val currentParametersTitle: StringResource get() = Res.string.current_parameters_title
    val commercial_score_with_value: StringResource get() = Res.string.commercial_score_with_value
    val number_of_screenings_with_value: StringResource get() = Res.string.number_of_screenings_with_value
    val chooseKeepOrOverwrite: StringResource get() = Res.string.choose_keep_or_overwrite
    val keep_saved: StringResource get() = Res.string.keep_saved

    // Platform specific setting items
    val resetWindowSize: StringResource get() = Res.string.reset_window_size
    val restoreDefaultWindowDimensions: StringResource get() = Res.string.restore_default_window_dimensions
    
    // Tooltips & Help
    val tooltipDistributionType: StringResource get() = Res.string.tooltip_distribution_type
    val tooltipSave: StringResource get() = Res.string.tooltip_save
    val tooltipExport: StringResource get() = Res.string.tooltip_export
    
    // Accessibility
    val contentDescriptionLogo: StringResource get() = Res.string.content_description_logo
    val contentDescriptionMenu: StringResource get() = Res.string.content_description_menu
    val contentDescriptionClose: StringResource get() = Res.string.content_description_close
    val contentDescriptionBack: StringResource get() = Res.string.content_description_back
    val contentDescriptionExpand: StringResource get() = Res.string.content_description_expand
    val contentDescriptionCollapse: StringResource get() = Res.string.content_description_collapse
    val contentDescriptionClearAll: StringResource get() = Res.string.content_description_clear_all
    val contentDescriptionLightMode: StringResource get() = Res.string.content_description_light_mode
    val contentDescriptionDarkMode: StringResource get() = Res.string.content_description_dark_mode
    val contentDescriptionPinned: StringResource get() = Res.string.content_description_pinned
    val contentDescriptionUnpinned: StringResource get() = Res.string.content_description_unpinned
}
