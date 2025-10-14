package org.aalbertini.ham.core.ui.resources

import hollywoodanimalmaster.composeapp.generated.resources.Res
import hollywoodanimalmaster.composeapp.generated.resources.action_clear_all_with_count
import hollywoodanimalmaster.composeapp.generated.resources.action_collapse
import hollywoodanimalmaster.composeapp.generated.resources.action_copy_results
import hollywoodanimalmaster.composeapp.generated.resources.action_delete_movie
import hollywoodanimalmaster.composeapp.generated.resources.action_disable_always_on_top
import hollywoodanimalmaster.composeapp.generated.resources.action_edit_movie
import hollywoodanimalmaster.composeapp.generated.resources.action_enable_always_on_top
import hollywoodanimalmaster.composeapp.generated.resources.action_expand
import hollywoodanimalmaster.composeapp.generated.resources.action_load_movie
import hollywoodanimalmaster.composeapp.generated.resources.action_new_movie
import hollywoodanimalmaster.composeapp.generated.resources.action_revert_to_original
import hollywoodanimalmaster.composeapp.generated.resources.action_save_movie
import hollywoodanimalmaster.composeapp.generated.resources.action_switch_to_dark
import hollywoodanimalmaster.composeapp.generated.resources.action_switch_to_light
import hollywoodanimalmaster.composeapp.generated.resources.app_name
import hollywoodanimalmaster.composeapp.generated.resources.app_name_short
import hollywoodanimalmaster.composeapp.generated.resources.appearance_title
import hollywoodanimalmaster.composeapp.generated.resources.average_per_scene
import hollywoodanimalmaster.composeapp.generated.resources.calculate_button
import hollywoodanimalmaster.composeapp.generated.resources.cancel
import hollywoodanimalmaster.composeapp.generated.resources.choose_color_theme
import hollywoodanimalmaster.composeapp.generated.resources.choose_keep_or_overwrite
import hollywoodanimalmaster.composeapp.generated.resources.clear_all_button
import hollywoodanimalmaster.composeapp.generated.resources.clear_override
import hollywoodanimalmaster.composeapp.generated.resources.commercial_score_with_value
import hollywoodanimalmaster.composeapp.generated.resources.confirm
import hollywoodanimalmaster.composeapp.generated.resources.content_description_back
import hollywoodanimalmaster.composeapp.generated.resources.content_description_clear_all
import hollywoodanimalmaster.composeapp.generated.resources.content_description_close
import hollywoodanimalmaster.composeapp.generated.resources.content_description_collapse
import hollywoodanimalmaster.composeapp.generated.resources.content_description_dark_mode
import hollywoodanimalmaster.composeapp.generated.resources.content_description_expand
import hollywoodanimalmaster.composeapp.generated.resources.content_description_light_mode
import hollywoodanimalmaster.composeapp.generated.resources.content_description_logo
import hollywoodanimalmaster.composeapp.generated.resources.content_description_menu
import hollywoodanimalmaster.composeapp.generated.resources.content_description_pinned
import hollywoodanimalmaster.composeapp.generated.resources.content_description_unpinned
import hollywoodanimalmaster.composeapp.generated.resources.current_parameters_title
import hollywoodanimalmaster.composeapp.generated.resources.dark_mode_label
import hollywoodanimalmaster.composeapp.generated.resources.default_multiplier
import hollywoodanimalmaster.composeapp.generated.resources.default_with_value
import hollywoodanimalmaster.composeapp.generated.resources.delete_confirmation
import hollywoodanimalmaster.composeapp.generated.resources.delete_distribution
import hollywoodanimalmaster.composeapp.generated.resources.dialog_title_clear_all
import hollywoodanimalmaster.composeapp.generated.resources.dialog_title_edit_movie
import hollywoodanimalmaster.composeapp.generated.resources.dialog_title_overwrite_movie
import hollywoodanimalmaster.composeapp.generated.resources.dialog_title_save_movie
import hollywoodanimalmaster.composeapp.generated.resources.dialog_title_update_movie
import hollywoodanimalmaster.composeapp.generated.resources.distribution_custom
import hollywoodanimalmaster.composeapp.generated.resources.distribution_exponential
import hollywoodanimalmaster.composeapp.generated.resources.distribution_label
import hollywoodanimalmaster.composeapp.generated.resources.distribution_normal
import hollywoodanimalmaster.composeapp.generated.resources.distribution_uniform
import hollywoodanimalmaster.composeapp.generated.resources.enabled
import hollywoodanimalmaster.composeapp.generated.resources.enter_available_screenings_first
import hollywoodanimalmaster.composeapp.generated.resources.error_commercial_score_range
import hollywoodanimalmaster.composeapp.generated.resources.error_invalid_input
import hollywoodanimalmaster.composeapp.generated.resources.error_load_failed
import hollywoodanimalmaster.composeapp.generated.resources.error_network
import hollywoodanimalmaster.composeapp.generated.resources.error_number_of_screenings_range
import hollywoodanimalmaster.composeapp.generated.resources.error_save_failed
import hollywoodanimalmaster.composeapp.generated.resources.error_title_exists
import hollywoodanimalmaster.composeapp.generated.resources.export_button
import hollywoodanimalmaster.composeapp.generated.resources.import_button
import hollywoodanimalmaster.composeapp.generated.resources.info_no_saved_movies
import hollywoodanimalmaster.composeapp.generated.resources.keep_saved
import hollywoodanimalmaster.composeapp.generated.resources.label_commercial_score
import hollywoodanimalmaster.composeapp.generated.resources.label_screenings
import hollywoodanimalmaster.composeapp.generated.resources.light_mode_label
import hollywoodanimalmaster.composeapp.generated.resources.load_button
import hollywoodanimalmaster.composeapp.generated.resources.loading
import hollywoodanimalmaster.composeapp.generated.resources.message_enter_valid_parameters
import hollywoodanimalmaster.composeapp.generated.resources.message_overwrite_movie
import hollywoodanimalmaster.composeapp.generated.resources.message_title_exists_with_diff_params
import hollywoodanimalmaster.composeapp.generated.resources.movie_distribution_title
import hollywoodanimalmaster.composeapp.generated.resources.movie_name_hint
import hollywoodanimalmaster.composeapp.generated.resources.movie_name_label
import hollywoodanimalmaster.composeapp.generated.resources.movie_results_screenings
import hollywoodanimalmaster.composeapp.generated.resources.movie_results_week_multiplier
import hollywoodanimalmaster.composeapp.generated.resources.movie_results_week_multiplier_override
import hollywoodanimalmaster.composeapp.generated.resources.movie_results_your_screenings_override
import hollywoodanimalmaster.composeapp.generated.resources.no
import hollywoodanimalmaster.composeapp.generated.resources.no_results
import hollywoodanimalmaster.composeapp.generated.resources.no_saved_distributions
import hollywoodanimalmaster.composeapp.generated.resources.number_of_screenings_with_value
import hollywoodanimalmaster.composeapp.generated.resources.ok
import hollywoodanimalmaster.composeapp.generated.resources.override_label
import hollywoodanimalmaster.composeapp.generated.resources.overwrite
import hollywoodanimalmaster.composeapp.generated.resources.parameter_conflict
import hollywoodanimalmaster.composeapp.generated.resources.parameters_section_title
import hollywoodanimalmaster.composeapp.generated.resources.pending_validation
import hollywoodanimalmaster.composeapp.generated.resources.platform_settings_description
import hollywoodanimalmaster.composeapp.generated.resources.platform_settings_title
import hollywoodanimalmaster.composeapp.generated.resources.reset_button
import hollywoodanimalmaster.composeapp.generated.resources.reset_window_size
import hollywoodanimalmaster.composeapp.generated.resources.restore_default_window_dimensions
import hollywoodanimalmaster.composeapp.generated.resources.results_section_title
import hollywoodanimalmaster.composeapp.generated.resources.results_title
import hollywoodanimalmaster.composeapp.generated.resources.retry
import hollywoodanimalmaster.composeapp.generated.resources.save_button
import hollywoodanimalmaster.composeapp.generated.resources.saved_distributions_title
import hollywoodanimalmaster.composeapp.generated.resources.saved_movies_count
import hollywoodanimalmaster.composeapp.generated.resources.saved_parameters_title
import hollywoodanimalmaster.composeapp.generated.resources.scenes_count
import hollywoodanimalmaster.composeapp.generated.resources.screen_title
import hollywoodanimalmaster.composeapp.generated.resources.settings_title
import hollywoodanimalmaster.composeapp.generated.resources.statistics_title
import hollywoodanimalmaster.composeapp.generated.resources.switch_to_dark_theme
import hollywoodanimalmaster.composeapp.generated.resources.switch_to_light_theme
import hollywoodanimalmaster.composeapp.generated.resources.theme_presets_title
import hollywoodanimalmaster.composeapp.generated.resources.tooltip_distribution_type
import hollywoodanimalmaster.composeapp.generated.resources.tooltip_export
import hollywoodanimalmaster.composeapp.generated.resources.tooltip_save
import hollywoodanimalmaster.composeapp.generated.resources.unsaved
import hollywoodanimalmaster.composeapp.generated.resources.up_to_date
import hollywoodanimalmaster.composeapp.generated.resources.update
import hollywoodanimalmaster.composeapp.generated.resources.validation_between_zero_and
import hollywoodanimalmaster.composeapp.generated.resources.validation_invalid_range
import hollywoodanimalmaster.composeapp.generated.resources.validation_multiplier_range
import hollywoodanimalmaster.composeapp.generated.resources.validation_positive_number
import hollywoodanimalmaster.composeapp.generated.resources.validation_required_field
import hollywoodanimalmaster.composeapp.generated.resources.warning_cannot_undo
import hollywoodanimalmaster.composeapp.generated.resources.week_group_label
import hollywoodanimalmaster.composeapp.generated.resources.week_number
import hollywoodanimalmaster.composeapp.generated.resources.week_results_line
import hollywoodanimalmaster.composeapp.generated.resources.yes
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
    val movieResultsWeekMultiplier: StringResource get() = Res.string.movie_results_week_multiplier
    val movieResultsWeekMultiplierOverride: StringResource get() = Res.string.movie_results_week_multiplier_override
    val defaultMultiplier: StringResource get() = Res.string.default_multiplier
    val validationMultiplierRange: StringResource get() = Res.string.validation_multiplier_range
    val pendingValidation: StringResource get() = Res.string.pending_validation
    val overrideLabel: StringResource get() = Res.string.override_label
    val defaultWithValue: StringResource get() = Res.string.default_with_value
    val clearOverride: StringResource get() = Res.string.clear_override
    val validationBetweenZeroAnd: StringResource get() = Res.string.validation_between_zero_and
    val enterAvailableScreeningsFirst: StringResource get() = Res.string.enter_available_screenings_first
    val weekNumber: StringResource get() = Res.string.week_number
    val weekGroupLabel: StringResource get() = Res.string.week_group_label
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
    val upToDate: StringResource get() = Res.string.up_to_date
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
