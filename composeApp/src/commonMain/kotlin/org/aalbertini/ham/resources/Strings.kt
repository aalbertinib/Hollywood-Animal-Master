package org.aalbertini.ham.resources

import hollywoodanimalsmaster.composeapp.generated.resources.*
import org.jetbrains.compose.resources.StringResource

/**
 * Centralized string resources for the Hollywood Animals Master application.
 * 
 * This object provides type-safe access to all string resources using Compose Multiplatform's
 * generated resources API (1.9.0). All strings are defined in composeResources/values/strings.xml
 * and automatically generated at build time.
 * 
 * Usage:
 * ```
 * Text(text = stringResource(Strings.appName))
 * Text(text = stringResource(Strings.totalAnimals, count))
 * ```
 */
object Strings {
    // App Name
    val appName: StringResource get() = Res.string.app_name
    val appNameShort: StringResource get() = Res.string.app_name_short
    
    // Screen Titles
    val movieDistributionTitle: StringResource get() = Res.string.movie_distribution_title
    val statisticsTitle: StringResource get() = Res.string.statistics_title
    val settingsTitle: StringResource get() = Res.string.settings_title
    
    // Movie Distribution Screen
    val parametersSectionTitle: StringResource get() = Res.string.parameters_section_title
    val movieNameLabel: StringResource get() = Res.string.movie_name_label
    val movieNameHint: StringResource get() = Res.string.movie_name_hint
    val numberOfAnimalsLabel: StringResource get() = Res.string.number_of_animals_label
    val distributionLabel: StringResource get() = Res.string.distribution_label
    val distributionUniform: StringResource get() = Res.string.distribution_uniform
    val distributionNormal: StringResource get() = Res.string.distribution_normal
    val distributionExponential: StringResource get() = Res.string.distribution_exponential
    val distributionCustom: StringResource get() = Res.string.distribution_custom
    
    // Actions
    val calculateButton: StringResource get() = Res.string.calculate_button
    val resetButton: StringResource get() = Res.string.reset_button
    val saveButton: StringResource get() = Res.string.save_button
    val loadButton: StringResource get() = Res.string.load_button
    val exportButton: StringResource get() = Res.string.export_button
    val importButton: StringResource get() = Res.string.import_button
    val clearAllButton: StringResource get() = Res.string.clear_all_button
    
    // Results Section
    val resultsTitle: StringResource get() = Res.string.results_title
    val noResults: StringResource get() = Res.string.no_results
    val totalAnimals: StringResource get() = Res.string.total_animals
    val averagePerScene: StringResource get() = Res.string.average_per_scene
    val scenesCount: StringResource get() = Res.string.scenes_count
    
    // Saved Distributions
    val savedDistributionsTitle: StringResource get() = Res.string.saved_distributions_title
    val noSavedDistributions: StringResource get() = Res.string.no_saved_distributions
    val deleteDistribution: StringResource get() = Res.string.delete_distribution
    val deleteConfirmation: StringResource get() = Res.string.delete_confirmation
    
    // Errors
    val errorInvalidInput: StringResource get() = Res.string.error_invalid_input
    val errorSaveFailed: StringResource get() = Res.string.error_save_failed
    val errorLoadFailed: StringResource get() = Res.string.error_load_failed
    val errorNetwork: StringResource get() = Res.string.error_network
    
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
    
    // Tooltips & Help
    val tooltipDistributionType: StringResource get() = Res.string.tooltip_distribution_type
    val tooltipSave: StringResource get() = Res.string.tooltip_save
    val tooltipExport: StringResource get() = Res.string.tooltip_export
    
    // Accessibility
    val contentDescriptionLogo: StringResource get() = Res.string.content_description_logo
    val contentDescriptionMenu: StringResource get() = Res.string.content_description_menu
    val contentDescriptionClose: StringResource get() = Res.string.content_description_close
    val contentDescriptionBack: StringResource get() = Res.string.content_description_back
}
