package org.aalbertini.ham.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.viewmodel.compose.viewModel
import org.aalbertini.ham.model.MovieResult
import org.aalbertini.ham.ui.components.ClearAllConfirmationDialog
import org.aalbertini.ham.ui.components.EditDialog
import org.aalbertini.ham.ui.components.ParametersSection
import org.aalbertini.ham.ui.components.ResultsSection
import org.aalbertini.ham.ui.components.SaveDialog
import org.aalbertini.ham.ui.components.SavedMovieResultsSection
import org.aalbertini.ham.ui.components.UiConstants
import org.aalbertini.ham.ui.components.UiStrings
import org.aalbertini.ham.ui.layout.AdaptiveCalculatorLayout
import org.aalbertini.ham.ui.layout.rememberWindowSizeClass
import org.aalbertini.ham.ui.state.CalculatorUiEvent
import org.aalbertini.ham.ui.state.NotificationType
import org.aalbertini.ham.viewmodel.CalculatorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieWeeklyDistributionCalculatorScreen(
    isDarkMode: Boolean = true,
    onThemeToggle: () -> Unit = {},
    alwaysOnTop: Boolean = false,
    onAlwaysOnTopChange: ((Boolean) -> Unit)? = null,
    viewModel: CalculatorViewModel = viewModel { CalculatorViewModel() }
) {
    val uiState by viewModel.uiState.collectAsState()
    val clipboard = LocalClipboardManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val windowSizeClass = rememberWindowSizeClass()
    
    var showSaveDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf<MovieResult?>(null) }
    var showClearAllDialog by remember { mutableStateOf(false) }
    
    // Show notifications
    LaunchedEffect(uiState.notification) {
        uiState.notification?.let { notification ->
            snackbarHostState.showSnackbar(notification.message)
            viewModel.onEvent(CalculatorUiEvent.DismissNotification)
        }
    }
    
    // Animated background outside Scaffold
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
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
                                    Icon(
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
                            state = rememberTooltipState()
                        ) {
                            IconButton(onClick = onThemeToggle) {
                                Icon(
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
                                p1Input = uiState.p1Input,
                                p2Input = uiState.p2Input,
                                currentMovieResultTitle = uiState.currentMovieResultTitle,
                                editableTitle = uiState.editableTitle,
                                originalTitle = uiState.originalTitle,
                                onTitleChange = { viewModel.onEvent(CalculatorUiEvent.UpdateEditableTitle(it)) },
                                onP1Change = { viewModel.onEvent(CalculatorUiEvent.UpdateP1Input(it)) },
                                onP2Change = { viewModel.onEvent(CalculatorUiEvent.UpdateP2Input(it)) },
                                onSaveClick = { showSaveDialog = true },
                                onNewClick = { viewModel.onEvent(CalculatorUiEvent.NewMovieResult) },
                                onRevertTitle = { viewModel.onEvent(CalculatorUiEvent.RevertTitle) }
                            )
                        },
                        resultsSection = {
                            ResultsSection(
                                results = uiState.resultsWithRounded,
                                expanded = uiState.expandResults,
                                onToggleExpand = { viewModel.onEvent(CalculatorUiEvent.ToggleResultsExpand) },
                                onCopyClick = {
                                    val text = buildString {
                                        append("Results\n")
                                        uiState.resultsWithRounded.forEachIndexed { idx, v ->
                                            append("Week ${idx + 1}: $v\n")
                                        }
                                    }
                                    clipboard.setText(AnnotatedString(text.trimEnd()))
                                    viewModel.onEvent(CalculatorUiEvent.CopyResults(uiState.resultsWithRounded))
                                }
                            )
                        },
                        savedSection = {
                            SavedMovieResultsSection(
                                movieResults = uiState.savedMovieResults,
                                expanded = uiState.expandSaved,
                                onToggleExpand = { viewModel.onEvent(CalculatorUiEvent.ToggleSavedExpand) },
                                onLoadClick = { movieResult ->
                                    viewModel.onEvent(CalculatorUiEvent.LoadMovieResult(movieResult.id))
                                },
                                onEditClick = { movieResult ->
                                    showEditDialog = movieResult
                                },
                                onDeleteClick = { movieResult ->
                                    viewModel.onEvent(CalculatorUiEvent.DeleteMovieResult(movieResult.id))
                                },
                                onClearAllClick = { showClearAllDialog = true }
                            )
                        }
                    )
            }
        }
    }
    
    // Dialogs
    if (showSaveDialog) {
        SaveDialog(
            initialTitle = uiState.editableTitle.ifBlank { "Unsaved" },
            existingTitles = uiState.savedMovieResults.map { it.title },
            currentTitle = uiState.currentMovieResultTitle,
            onDismiss = { showSaveDialog = false },
            onSave = { title ->
                viewModel.onEvent(CalculatorUiEvent.SaveMovieResult(title))
                showSaveDialog = false
            },
            isUpdate = uiState.currentMovieResultId != null
        )
    }
    
    showEditDialog?.let { movieResult ->
        EditDialog(
            movieResult = movieResult,
            onDismiss = { showEditDialog = null },
            onUpdate = { title, p1, p2 ->
                viewModel.onEvent(CalculatorUiEvent.UpdateMovieResult(movieResult.id, title, p1, p2))
                showEditDialog = null
            }
        )
    }
    
    if (showClearAllDialog) {
        ClearAllConfirmationDialog(
            movieResultCount = uiState.savedMovieResults.size,
            onDismiss = { showClearAllDialog = false },
            onConfirm = {
                viewModel.onEvent(CalculatorUiEvent.ClearAllMovieResults)
                showClearAllDialog = false
            }
        )
    }
}
