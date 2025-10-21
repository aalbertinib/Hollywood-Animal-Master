package org.aalbertini.ham.features.movie_distribution.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.aalbertini.ham.core.ui.components.AnimatedIcon
import org.aalbertini.ham.core.ui.components.TextIcon
import org.aalbertini.ham.core.ui.layout.AdaptiveMovieDistributionLayout
import org.aalbertini.ham.core.ui.layout.WindowSizeClass
import org.aalbertini.ham.core.ui.layout.rememberWindowSizeClass
import org.aalbertini.ham.core.ui.resources.Strings
import org.aalbertini.ham.core.ui.resources.UiConstants
import org.aalbertini.ham.core.ui.resources.UiIcons
import org.aalbertini.ham.core.ui.theme.AnimatedBackground
import org.aalbertini.ham.features.movie_distribution.domain.calculator.MovieDistributionConstants
import org.aalbertini.ham.features.movie_distribution.domain.model.MovieResult
import org.aalbertini.ham.features.movie_distribution.presentation.components.dialog.ClearAllConfirmationDialog
import org.aalbertini.ham.features.movie_distribution.presentation.components.dialog.EditDialog
import org.aalbertini.ham.features.movie_distribution.presentation.components.dialog.ParameterComparisonDialog
import org.aalbertini.ham.features.movie_distribution.presentation.components.section.ParametersSection
import org.aalbertini.ham.features.movie_distribution.presentation.components.section.SavedMovieResultsSection
import org.aalbertini.ham.features.movie_distribution.presentation.components.section.results.ResultsSection
import org.aalbertini.ham.features.movie_distribution.presentation.state.NotificationType
import org.aalbertini.ham.features.movie_distribution.presentation.state.parameters.ParametersUiEvent
import org.aalbertini.ham.features.movie_distribution.presentation.state.results.ResultsUiEvent
import org.aalbertini.ham.features.movie_distribution.presentation.state.saved_movies.SavedMoviesUiEvent
import org.aalbertini.ham.features.movie_distribution.presentation.viewmodel.MovieDistributionViewModel
import org.aalbertini.ham.features.settings.domain.model.ThemeColorSchemes
import org.aalbertini.ham.features.settings.domain.model.ThemePreset
import org.aalbertini.ham.features.settings.presentation.components.SettingsDialog
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieWeeklyDistributionCalculatorScreen(
    isDarkMode: Boolean = true,
    themeToggleOffset: Offset? = null,
    currentThemePreset: ThemePreset = ThemePreset.D01_PURPLE,
    onThemeToggle: (Offset) -> Unit = {},
    onThemePresetChange: (ThemePreset) -> Unit = {},
    alwaysOnTop: Boolean = false,
    onAlwaysOnTopChange: ((Boolean) -> Unit)? = null,
    onResetWindowSize: (() -> Unit)? = null,
    viewModel: MovieDistributionViewModel = viewModel { MovieDistributionViewModel() }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    @Suppress("DEPRECATION")
    val clipboard = LocalClipboardManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val windowSizeClass = rememberWindowSizeClass()
    
    var showEditDialog by remember { mutableStateOf<MovieResult?>(null) }
    var showClearAllDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var themeButtonPosition by remember { mutableStateOf(Offset.Zero) }
    
    // Track last notification type for Snackbar styling
    var lastNotificationType by remember { mutableStateOf<NotificationType?>(null) }
    
    // Show notifications
    LaunchedEffect(uiState.notification) {
        uiState.notification?.let { notification ->
            lastNotificationType = notification.type
            snackbarHostState.showSnackbar(notification.message)
            viewModel.savedMoviesViewModel.onEvent(SavedMoviesUiEvent.DismissNotification)
        }
    }
    
    // Animated background outside Scaffold with explicit preset colors to avoid inversion during switch
    val lightScheme = remember(currentThemePreset) { ThemeColorSchemes.getLightColorScheme(currentThemePreset) }
    val darkScheme = remember(currentThemePreset) { ThemeColorSchemes.getDarkColorScheme(currentThemePreset) }
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedBackground(
            targetState = isDarkMode,
            lightColor = lightScheme.surface,
            darkColor = darkScheme.surface,
            revealFrom = themeToggleOffset
        )
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        TextIcon(
                            text = stringResource(Strings.screenTitle),
                            icon = UiIcons.SCREEN_TITLE,
                            style = if (windowSizeClass == WindowSizeClass.EXPANDED) {
                                MaterialTheme.typography.headlineSmall
                            } else {
                                MaterialTheme.typography.titleLarge
                            },
                            textAlign = TextAlign.Center
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                        actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    actions = {
                        // Settings button
                        TooltipBox(
                            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                            tooltip = {
                                PlainTooltip {
                                    Text(stringResource(Strings.settingsTitle))
                                }
                            },
                            state = rememberTooltipState()
                        ) {
                            IconButton(onClick = { showSettingsDialog = true }) {
                                AnimatedIcon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = stringResource(Strings.settingsTitle)
                                )
                            }
                        }
                        
                        // Always on Top toggle (only visible on JVM)
                        onAlwaysOnTopChange?.let { onChange ->
                            TooltipBox(
                                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                                tooltip = {
                                    PlainTooltip {
                                        Text(
                                            if (alwaysOnTop) stringResource(Strings.actionDisableAlwaysOnTop)
                                            else stringResource(Strings.actionEnableAlwaysOnTop)
                                        )
                                    }
                                },
                                state = rememberTooltipState()
                            ) {
                                IconButton(onClick = { onChange(!alwaysOnTop) }) {
                                    AnimatedIcon(
                                        imageVector = if (alwaysOnTop) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                                        contentDescription = if (alwaysOnTop) stringResource(Strings.contentDescriptionPinned) else stringResource(Strings.contentDescriptionUnpinned)
                                    )
                                }
                            }
                        }
                        
                        // Theme toggle with position tracking for circular reveal animation
                        TooltipBox(
                            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                            tooltip = {
                                PlainTooltip {
                                    Text(if (isDarkMode) stringResource(Strings.actionSwitchToLight) else stringResource(Strings.actionSwitchToDark))
                                }
                            },
                            state = rememberTooltipState(),
                            modifier = Modifier.onGloballyPositioned { coordinates ->
                                // Track button position for circular reveal animation origin
                                val position = coordinates.positionInWindow()
                                val size = coordinates.size
                                themeButtonPosition = Offset(
                                    position.x + size.width / 2f,
                                    position.y + size.height / 2f
                                )
                            }
                        ) {
                            // Use a stable lambda that reads current position
                            val handleThemeToggle = remember<() -> Unit> {
                                { onThemeToggle(themeButtonPosition) }
                            }
                            IconButton(onClick = handleThemeToggle) {
                                AnimatedIcon(
                                    imageVector = if (isDarkMode) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                                    contentDescription = if (isDarkMode) stringResource(Strings.contentDescriptionLightMode) else stringResource(Strings.contentDescriptionDarkMode)
                                )
                            }
                        }
                    }
                )
            },
            snackbarHost = {
                SnackbarHost(snackbarHostState) { data ->
                    Snackbar(
                        snackbarData = data,
                        shape = MaterialTheme.shapes.medium,
                        containerColor = when (lastNotificationType) {
                            NotificationType.SUCCESS -> MaterialTheme.colorScheme.tertiaryContainer
                            NotificationType.ERROR -> MaterialTheme.colorScheme.errorContainer
                            else -> MaterialTheme.colorScheme.surfaceContainerHighest
                        },
                        contentColor = when (lastNotificationType) {
                            NotificationType.SUCCESS -> MaterialTheme.colorScheme.onTertiaryContainer
                            NotificationType.ERROR -> MaterialTheme.colorScheme.onErrorContainer
                            else -> MaterialTheme.colorScheme.onSurface
                        }
                    )
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize()) {
                val contentModifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = UiConstants.Padding.contentStandard)
                    .padding(top = UiConstants.Padding.contentStandard, bottom = UiConstants.Padding.contentStandard)
            
            // Calculate parameter validation state for headers
            val commercialScore = uiState.commercialScoreInput.toDoubleOrNull()
            val commercialScoreValid = commercialScore != null && 
                commercialScore >= MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MIN && 
                commercialScore <= MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MAX
            val availableScreenings = uiState.availableScreeningsInput.toDoubleOrNull()
            val availableScreeningsValid = availableScreenings != null && 
                availableScreenings >= MovieDistributionConstants.Validation.SCREENINGS_MIN && 
                availableScreenings <= MovieDistributionConstants.Validation.SCREENINGS_MAX
            val hasCurrentMovieResult = uiState.currentMovieResultTitle != null
            val hasUnsavedChanges = hasCurrentMovieResult && uiState.hasUnsavedChanges()
            val isSavedWithoutChanges = hasCurrentMovieResult && commercialScoreValid && availableScreeningsValid && !hasUnsavedChanges
            
            // Check if there's a saved movie with the same title as the editable title
            val titleExistsInSavedMovies = uiState.savedMovieResults.any { it.title.equals(uiState.editableTitle, ignoreCase = true) }
            val showNewButton = hasCurrentMovieResult || (uiState.editableTitle.isNotBlank() && !titleExistsInSavedMovies)
            
            // Save button should show when:
            // 1. Existing movie with unsaved changes, OR
            // 2. New movie with valid data and title
            val showSaveButton = commercialScoreValid && availableScreeningsValid && (
                (hasCurrentMovieResult && !isSavedWithoutChanges) ||
                (!hasCurrentMovieResult && uiState.editableTitle.isNotBlank())
            )
            
            // Adaptive layout based on window size
            AdaptiveMovieDistributionLayout(
                windowSizeClass = windowSizeClass,
                modifier = contentModifier,
                parametersHeader = { /* Empty - header is now inside card */ },
                parametersContent = {
                    ParametersSection(
                        commercialScoreInput = uiState.commercialScoreInput,
                        availableScreeningsInput = uiState.availableScreeningsInput,
                        currentMovieResultTitle = uiState.currentMovieResultTitle,
                        editableTitle = uiState.editableTitle,
                        originalTitle = uiState.originalTitle,
                        originalCommercialScore = uiState.originalCommercialScore,
                        originalAvailableScreenings = uiState.originalAvailableScreenings,
                        hasCurrentMovieResult = hasCurrentMovieResult,
                        commercialScoreValid = commercialScoreValid,
                        availableScreeningsValid = availableScreeningsValid,
                        isSavedWithoutChanges = isSavedWithoutChanges,
                        showSaveButton = showSaveButton,
                        showNewButton = showNewButton,
                        onTitleChange = { viewModel.parametersViewModel.onEvent(ParametersUiEvent.UpdateEditableTitle(it)) },
                        onCommercialScoreChange = { viewModel.parametersViewModel.onEvent(ParametersUiEvent.UpdateCommercialScoreInput(it)) },
                        onAvailableScreeningsChange = { viewModel.parametersViewModel.onEvent(ParametersUiEvent.UpdateAvailableScreeningsInput(it)) },
                        onRevertTitle = { viewModel.parametersViewModel.onEvent(ParametersUiEvent.RevertTitle) },
                        onRevertCommercialScore = { viewModel.parametersViewModel.onEvent(ParametersUiEvent.RevertCommercialScore) },
                        onRevertAvailableScreenings = { viewModel.parametersViewModel.onEvent(ParametersUiEvent.RevertAvailableScreenings) },
                        onSaveClick = { viewModel.autoSaveCurrentMovie() },
                        onNewClick = { viewModel.parametersViewModel.onEvent(ParametersUiEvent.NewMovieResult) }
                    )
                },
                resultsHeader = { /* Empty - header is now inside card */ },
                resultsContent = {
                    val resultsTitleText = stringResource(Strings.resultsTitle)
                    val copyText = buildString {
                        append(resultsTitleText)
                        append("\n")
                        uiState.resultsWithRounded.forEachIndexed { idx, v ->
                            append(
                                stringResource(
                                    Strings.weekResultsLine,
                                    idx + 1,
                                    v.toString()
                                )
                            )
                            append("\n")
                        }
                    }.trimEnd()
                    
                    ResultsSection(
                        results = uiState.resultsWithRounded,
                        expanded = uiState.expandResults,
                        hasResults = uiState.resultsWithRounded.isNotEmpty(),
                        availableScreeningsValue = uiState.availableScreeningsInput.toDoubleOrNull() ?: 0.0,
                        availableScreeningsOverrideInputs = uiState.availableScreeningsOverrideInputs,
                        weekMultiplierOverrideInputs = uiState.weekMultiplierOverrideInputs,
                        currentMovieResultId = uiState.currentMovieResultId,
                        onAvailableScreeningsOverrideChange = { weekIndex, value ->
                            viewModel.resultsViewModel.onEvent(ResultsUiEvent.UpdateAvailableScreeningsOverride(weekIndex, value))
                        },
                        onWeekMultiplierOverrideChange = { weekIndex, value ->
                            viewModel.resultsViewModel.onEvent(ResultsUiEvent.UpdateWeekMultiplierOverride(weekIndex, value))
                        },
                        onCopyClick = {
                            clipboard.setText(AnnotatedString(copyText))
                            viewModel.resultsViewModel.onEvent(ResultsUiEvent.CopyResults(uiState.resultsWithRounded))
                        },
                        onToggleExpand = { viewModel.resultsViewModel.onEvent(ResultsUiEvent.ToggleResultsExpand) }
                    )
                },
                savedHeader = { /* Empty - header is now inside card */ },
                savedContent = {
                    SavedMovieResultsSection(
                        movieResults = uiState.savedMovieResults,
                        expanded = uiState.expandSaved,
                        movieCount = uiState.savedMovieResults.size,
                        onLoadClick = { movieResult ->
                            viewModel.savedMoviesViewModel.onEvent(SavedMoviesUiEvent.LoadMovieResult(movieResult.id))
                        },
                        onEditClick = { movieResult ->
                            showEditDialog = movieResult
                        },
                        onDeleteClick = { movieResult ->
                            viewModel.savedMoviesViewModel.onEvent(SavedMoviesUiEvent.DeleteMovieResult(movieResult.id))
                        },
                        onClearAllClick = { showClearAllDialog = true },
                        onToggleExpand = { viewModel.savedMoviesViewModel.onEvent(SavedMoviesUiEvent.ToggleSavedExpand) }
                    )
                }
            )
            }
        }
    }
    
    // Dialogs
    uiState.parameterConflict?.let { conflict ->
        ParameterComparisonDialog(
            movieTitle = conflict.existingMovie.title,
            existingCommercialScore = conflict.existingMovie.commercialScore,
            existingScreenings = conflict.existingMovie.numberOfScreenings,
            newCommercialScore = conflict.newCommercialScore,
            newScreenings = conflict.newScreenings,
            onDismiss = { viewModel.savedMoviesViewModel.onEvent(SavedMoviesUiEvent.DismissParameterConflict) },
            onKeepExisting = { viewModel.savedMoviesViewModel.onEvent(SavedMoviesUiEvent.KeepExistingMovie) },
            onOverwrite = { viewModel.savedMoviesViewModel.onEvent(SavedMoviesUiEvent.OverwriteConflictingMovie) }
        )
    }
    
    showEditDialog?.let { movieResult ->
        EditDialog(
            movieResult = movieResult,
            onDismiss = { showEditDialog = null },
            onUpdate = { title, commercialScore, availableScreenings ->
                viewModel.savedMoviesViewModel.onEvent(SavedMoviesUiEvent.UpdateMovieResult(movieResult.id, title, commercialScore, availableScreenings))
                showEditDialog = null
            }
        )
    }
    
    if (showClearAllDialog) {
        ClearAllConfirmationDialog(
            movieResultCount = uiState.savedMovieResults.size,
            onDismiss = { showClearAllDialog = false },
            onConfirm = {
                viewModel.savedMoviesViewModel.onEvent(SavedMoviesUiEvent.ClearAllMovieResults)
                showClearAllDialog = false
            }
        )
    }
    
    if (showSettingsDialog) {
        SettingsDialog(
            currentThemePreset = currentThemePreset,
            isDarkMode = isDarkMode,
            onThemePresetChange = onThemePresetChange,
            onDarkModeChange = { newDarkMode ->
                // Use the theme toggle button position for the circular reveal animation
                onThemeToggle(themeButtonPosition)
            },
            onResetWindowSize = onResetWindowSize,
            onDismiss = { showSettingsDialog = false }
        )
    }
}
