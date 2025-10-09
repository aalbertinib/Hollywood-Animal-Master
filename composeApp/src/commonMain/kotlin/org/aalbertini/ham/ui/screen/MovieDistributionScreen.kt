package org.aalbertini.ham.ui.screen

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.aalbertini.ham.model.MovieResult
import org.aalbertini.ham.model.ThemePreset
import org.aalbertini.ham.ui.components.AnimatedIcon
import org.aalbertini.ham.ui.components.ClearAllConfirmationDialog
import org.aalbertini.ham.ui.components.EditDialog
import org.aalbertini.ham.ui.components.ParameterComparisonDialog
import org.aalbertini.ham.ui.components.ParametersSection
import org.aalbertini.ham.ui.components.ResultsSection
import org.aalbertini.ham.ui.components.SavedMovieResultsSection
import org.aalbertini.ham.ui.components.SettingsDialog
import org.aalbertini.ham.ui.components.UiConstants
import org.aalbertini.ham.ui.components.UiStrings
import org.aalbertini.ham.ui.layout.AdaptiveCalculatorLayout
import org.aalbertini.ham.ui.layout.rememberWindowSizeClass
import org.aalbertini.ham.ui.state.MovieDistributionUiEvent
import org.aalbertini.ham.ui.state.NotificationType
import org.aalbertini.ham.ui.theme.AnimatedBackground
import org.aalbertini.ham.viewmodel.MovieDistributionViewModel

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
                        Text(
                            UiStrings.SCREEN_TITLE,
                            style = MaterialTheme.typography.titleLarge
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
                                    Text("Settings")
                                }
                            },
                            state = rememberTooltipState()
                        ) {
                            IconButton(onClick = { showSettingsDialog = true }) {
                                AnimatedIcon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Settings"
                                )
                            }
                        }
                        
                        // Always on Top toggle (only visible on JVM)
                        onAlwaysOnTopChange?.let { onChange ->
                            TooltipBox(
                                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                                tooltip = {
                                    PlainTooltip {
                                        Text(if (alwaysOnTop) UiStrings.ACTION_DISABLE_ALWAYS_ON_TOP else UiStrings.ACTION_ENABLE_ALWAYS_ON_TOP)
                                    }
                                },
                                state = rememberTooltipState()
                            ) {
                                IconButton(onClick = { onChange(!alwaysOnTop) }) {
                                    AnimatedIcon(
                                        imageVector = if (alwaysOnTop) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                                        contentDescription = if (alwaysOnTop) UiStrings.CONTENT_DESC_PINNED else UiStrings.CONTENT_DESC_UNPINNED
                                    )
                                }
                            }
                        }
                        
                        TooltipBox(
                            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                            tooltip = {
                                PlainTooltip {
                                    Text(if (isDarkMode) UiStrings.ACTION_SWITCH_TO_LIGHT else UiStrings.ACTION_SWITCH_TO_DARK)
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
                                    contentDescription = if (isDarkMode) UiStrings.CONTENT_DESC_LIGHT_MODE else UiStrings.CONTENT_DESC_DARK_MODE
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
            
            // Adaptive layout based on window size
            AdaptiveCalculatorLayout(
                windowSizeClass = windowSizeClass,
                modifier = contentModifier,
                inputSection = {
                            ParametersSection(
                                commercialScoreInput = uiState.commercialScoreInput,
                                availableSeatsInput = uiState.availableSeatsInput,
                                currentMovieResultTitle = uiState.currentMovieResultTitle,
                                editableTitle = uiState.editableTitle,
                                originalTitle = uiState.originalTitle,
                                originalCommercialScore = uiState.originalCommercialScore,
                                originalAvailableSeats = uiState.originalAvailableSeats,
                                onTitleChange = { viewModel.onEvent(MovieDistributionUiEvent.UpdateEditableTitle(it)) },
                                onCommercialScoreChange = { viewModel.onEvent(MovieDistributionUiEvent.UpdateCommercialScoreInput(it)) },
                                onAvailableSeatsChange = { viewModel.onEvent(MovieDistributionUiEvent.UpdateAvailableSeatsInput(it)) },
                                onSaveClick = { viewModel.onEvent(MovieDistributionUiEvent.AutoSaveMovieResult) },
                                onNewClick = { viewModel.onEvent(MovieDistributionUiEvent.NewMovieResult) },
                                onRevertTitle = { viewModel.onEvent(MovieDistributionUiEvent.RevertTitle) },
                                onRevertCommercialScore = { viewModel.onEvent(MovieDistributionUiEvent.RevertCommercialScore) },
                                onRevertAvailableSeats = { viewModel.onEvent(MovieDistributionUiEvent.RevertAvailableSeats) }
                            )
                        },
                        resultsSection = {
                            ResultsSection(
                                results = uiState.resultsWithRounded,
                                expanded = uiState.expandResults,
                                availableSeatsValue = uiState.availableSeatsInput.toDoubleOrNull() ?: 0.0,
                                availableSeatsOverrideInputs = uiState.availableSeatsOverrideInputs,
                                onToggleExpand = { viewModel.onEvent(MovieDistributionUiEvent.ToggleResultsExpand) },
                                onCopyClick = {
                                    val text = buildString {
                                        append("Results\n")
                                        uiState.resultsWithRounded.forEachIndexed { idx, v ->
                                            append("Week ${idx + 1}: $v\n")
                                        }
                                    }
                                    clipboard.setText(AnnotatedString(text.trimEnd()))
                                    viewModel.onEvent(MovieDistributionUiEvent.CopyResults(uiState.resultsWithRounded))
                                },
                                onAvailableSeatsOverrideChange = { weekIndex, value ->
                                    viewModel.onEvent(MovieDistributionUiEvent.UpdateAvailableSeatsOverride(weekIndex, value))
                                }
                            )
                        },
                        savedSection = {
                            SavedMovieResultsSection(
                                movieResults = uiState.savedMovieResults,
                                expanded = uiState.expandSaved,
                                onToggleExpand = { viewModel.onEvent(MovieDistributionUiEvent.ToggleSavedExpand) },
                                onLoadClick = { movieResult ->
                                    viewModel.onEvent(MovieDistributionUiEvent.LoadMovieResult(movieResult.id))
                                },
                                onEditClick = { movieResult ->
                                    showEditDialog = movieResult
                                },
                                onDeleteClick = { movieResult ->
                                    viewModel.onEvent(MovieDistributionUiEvent.DeleteMovieResult(movieResult.id))
                                },
                                onClearAllClick = { showClearAllDialog = true }
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
            existingSeats = conflict.existingMovie.numberOfSeats,
            newCommercialScore = conflict.newCommercialScore,
            newSeats = conflict.newSeats,
            onDismiss = { viewModel.onEvent(MovieDistributionUiEvent.DismissParameterConflict) },
            onKeepExisting = { viewModel.onEvent(MovieDistributionUiEvent.KeepExistingMovie) },
            onOverwrite = { viewModel.onEvent(MovieDistributionUiEvent.OverwriteConflictingMovie) }
        )
    }
    
    showEditDialog?.let { movieResult ->
        EditDialog(
            movieResult = movieResult,
            onDismiss = { showEditDialog = null },
            onUpdate = { title, commercialScore, availableSeats ->
                viewModel.onEvent(MovieDistributionUiEvent.UpdateMovieResult(movieResult.id, title, commercialScore, availableSeats))
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
            onDismiss = { showSettingsDialog = false }
        )
    }
}
