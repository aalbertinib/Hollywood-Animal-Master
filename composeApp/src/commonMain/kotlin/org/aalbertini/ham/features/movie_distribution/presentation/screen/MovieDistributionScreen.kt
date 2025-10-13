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
import org.aalbertini.ham.features.movie_distribution.domain.model.MovieResult
import org.aalbertini.ham.features.settings.domain.model.ThemePreset
import org.aalbertini.ham.features.movie_distribution.domain.calculator.MovieDistributionConstants
import org.aalbertini.ham.core.ui.components.AnimatedIcon
import org.aalbertini.ham.features.movie_distribution.presentation.components.ClearAllConfirmationDialog
import org.aalbertini.ham.features.movie_distribution.presentation.components.EditDialog
import org.aalbertini.ham.features.movie_distribution.presentation.components.ParameterComparisonDialog
import org.aalbertini.ham.features.movie_distribution.presentation.components.ParametersSection
import org.aalbertini.ham.features.movie_distribution.presentation.components.ParametersSectionHeader
import org.aalbertini.ham.features.movie_distribution.presentation.components.ResultsSection
import org.aalbertini.ham.core.ui.components.TextIcon
import org.aalbertini.ham.features.movie_distribution.presentation.components.ResultsSectionHeader
import org.aalbertini.ham.features.movie_distribution.presentation.components.SavedMovieResultsSection
import org.aalbertini.ham.features.movie_distribution.presentation.components.SavedMoviesSectionHeader
import org.aalbertini.ham.features.settings.presentation.components.SettingsDialog
import org.aalbertini.ham.core.ui.resources.UiConstants
import org.aalbertini.ham.core.ui.resources.UiStrings
import org.aalbertini.ham.core.ui.layout.AdaptiveMovieDistributionLayout
import org.aalbertini.ham.core.ui.layout.WindowSizeClass
import org.aalbertini.ham.core.ui.layout.rememberWindowSizeClass
import org.aalbertini.ham.features.movie_distribution.presentation.state.MovieDistributionUiEvent
import org.aalbertini.ham.features.movie_distribution.presentation.state.NotificationType
import org.aalbertini.ham.core.ui.theme.AnimatedBackground
import org.aalbertini.ham.features.movie_distribution.presentation.viewmodel.MovieDistributionViewModel
import org.aalbertini.ham.core.ui.resources.Strings
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieWeeklyDistributionCalculatorScreen(
    isDarkMode: Boolean = true,
    themeToggleOffset: Offset? = null,
    currentThemePreset: ThemePreset = ThemePreset.HOLLYWOOD_CLASSIC,
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
    
    // Show notifications
    LaunchedEffect(uiState.notification) {
        uiState.notification?.let { notification ->
            snackbarHostState.showSnackbar(notification.message)
            viewModel.onEvent(MovieDistributionUiEvent.DismissNotification)
        }
    }
    
    // Animated background outside Scaffold
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedBackground(
            targetState = isDarkMode,
            lightColor = Color(0xFFFFFBFE),
            darkColor = Color(0xFF1C1B1F),
            revealFrom = themeToggleOffset
        )
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        TextIcon(
                            text = stringResource(Strings.screenTitle),
                            icon = UiStrings.SCREEN_TITLE_ICON,
                            style = if (windowSizeClass == WindowSizeClass.EXPANDED) {
                                MaterialTheme.typography.titleLarge
                            } else {
                                MaterialTheme.typography.titleMedium
                            },
                            textAlign = TextAlign.Center
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
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
                        
                        TooltipBox(
                            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                            tooltip = {
                                PlainTooltip {
                                    Text(if (isDarkMode) stringResource(Strings.actionSwitchToLight) else stringResource(Strings.actionSwitchToDark))
                                }
                            },
                            state = rememberTooltipState(),
                            modifier = Modifier.onGloballyPositioned { coordinates ->
                                val position = coordinates.positionInWindow()
                                val size = coordinates.size
                                themeButtonPosition = Offset(
                                    position.x + size.width / 2f,
                                    position.y + size.height / 2f
                                )
                            }
                        ) {
                            IconButton(onClick = { onThemeToggle(themeButtonPosition) }) {
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
                        containerColor = when (uiState.notification?.type) {
                            NotificationType.SUCCESS -> MaterialTheme.colorScheme.primaryContainer
                            NotificationType.ERROR -> MaterialTheme.colorScheme.errorContainer
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        },
                        contentColor = when (uiState.notification?.type) {
                            NotificationType.SUCCESS -> MaterialTheme.colorScheme.onPrimaryContainer
                            NotificationType.ERROR -> MaterialTheme.colorScheme.onErrorContainer
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
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
            val hasUnsavedChanges = hasCurrentMovieResult && (
                uiState.editableTitle != uiState.originalTitle ||
                uiState.commercialScoreInput != uiState.originalCommercialScore ||
                uiState.availableScreeningsInput != uiState.originalAvailableScreenings
            )
            val isSavedWithoutChanges = hasCurrentMovieResult && commercialScoreValid && availableScreeningsValid && !hasUnsavedChanges
            
            // Adaptive layout based on window size
            AdaptiveMovieDistributionLayout(
                windowSizeClass = windowSizeClass,
                modifier = contentModifier,
                parametersHeader = {
                    ParametersSectionHeader(
                        hasCurrentMovieResult = hasCurrentMovieResult,
                        commercialScoreValid = commercialScoreValid,
                        availableScreeningsValid = availableScreeningsValid,
                        isSavedWithoutChanges = isSavedWithoutChanges,
                        expanded = true, // Parameters section is always expanded
                        onSaveClick = { viewModel.onEvent(MovieDistributionUiEvent.AutoSaveMovieResult) },
                        onNewClick = { viewModel.onEvent(MovieDistributionUiEvent.NewMovieResult) }
                    )
                },
                parametersContent = {
                    ParametersSection(
                        commercialScoreInput = uiState.commercialScoreInput,
                        availableScreeningsInput = uiState.availableScreeningsInput,
                        currentMovieResultTitle = uiState.currentMovieResultTitle,
                        editableTitle = uiState.editableTitle,
                        originalTitle = uiState.originalTitle,
                        originalCommercialScore = uiState.originalCommercialScore,
                        originalAvailableScreenings = uiState.originalAvailableScreenings,
                        onTitleChange = { viewModel.onEvent(MovieDistributionUiEvent.UpdateEditableTitle(it)) },
                        onCommercialScoreChange = { viewModel.onEvent(MovieDistributionUiEvent.UpdateCommercialScoreInput(it)) },
                        onAvailableScreeningsChange = { viewModel.onEvent(MovieDistributionUiEvent.UpdateAvailableScreeningsInput(it)) },
                        onRevertTitle = { viewModel.onEvent(MovieDistributionUiEvent.RevertTitle) },
                        onRevertCommercialScore = { viewModel.onEvent(MovieDistributionUiEvent.RevertCommercialScore) },
                        onRevertAvailableScreenings = { viewModel.onEvent(MovieDistributionUiEvent.RevertAvailableScreenings) }
                    )
                },
                resultsHeader = {
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

                    ResultsSectionHeader(
                        hasResults = uiState.resultsWithRounded.isNotEmpty(),
                        expanded = uiState.expandResults,
                        onCopyClick = {
                            clipboard.setText(AnnotatedString(copyText))
                            viewModel.onEvent(MovieDistributionUiEvent.CopyResults(uiState.resultsWithRounded))
                        },
                        onToggleExpand = { viewModel.onEvent(MovieDistributionUiEvent.ToggleResultsExpand) }
                    )
                },
                resultsContent = {
                    ResultsSection(
                        results = uiState.resultsWithRounded,
                        expanded = uiState.expandResults,
                        availableScreeningsValue = uiState.availableScreeningsInput.toDoubleOrNull() ?: 0.0,
                        availableScreeningsOverrideInputs = uiState.availableScreeningsOverrideInputs,
                        currentMovieResultId = uiState.currentMovieResultId,
                        onAvailableScreeningsOverrideChange = { weekIndex, value ->
                            viewModel.onEvent(MovieDistributionUiEvent.UpdateAvailableScreeningsOverride(weekIndex, value))
                        }
                    )
                },
                savedHeader = {
                    SavedMoviesSectionHeader(
                        movieCount = uiState.savedMovieResults.size,
                        expanded = uiState.expandSaved,
                        onClearAllClick = { showClearAllDialog = true },
                        onToggleExpand = { viewModel.onEvent(MovieDistributionUiEvent.ToggleSavedExpand) }
                    )
                },
                savedContent = {
                    SavedMovieResultsSection(
                        movieResults = uiState.savedMovieResults,
                        expanded = uiState.expandSaved,
                        onLoadClick = { movieResult ->
                            viewModel.onEvent(MovieDistributionUiEvent.LoadMovieResult(movieResult.id))
                        },
                        onEditClick = { movieResult ->
                            showEditDialog = movieResult
                        },
                        onDeleteClick = { movieResult ->
                            viewModel.onEvent(MovieDistributionUiEvent.DeleteMovieResult(movieResult.id))
                        }
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
            onDismiss = { viewModel.onEvent(MovieDistributionUiEvent.DismissParameterConflict) },
            onKeepExisting = { viewModel.onEvent(MovieDistributionUiEvent.KeepExistingMovie) },
            onOverwrite = { viewModel.onEvent(MovieDistributionUiEvent.OverwriteConflictingMovie) }
        )
    }
    
    showEditDialog?.let { movieResult ->
        EditDialog(
            movieResult = movieResult,
            onDismiss = { showEditDialog = null },
            onUpdate = { title, commercialScore, availableScreenings ->
                viewModel.onEvent(MovieDistributionUiEvent.UpdateMovieResult(movieResult.id, title, commercialScore, availableScreenings))
                showEditDialog = null
            }
        )
    }
    
    if (showClearAllDialog) {
        ClearAllConfirmationDialog(
            movieResultCount = uiState.savedMovieResults.size,
            onDismiss = { showClearAllDialog = false },
            onConfirm = {
                viewModel.onEvent(MovieDistributionUiEvent.ClearAllMovieResults)
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
