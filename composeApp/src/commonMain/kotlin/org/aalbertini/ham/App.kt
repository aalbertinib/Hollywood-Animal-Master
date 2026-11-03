package org.aalbertini.ham

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldLayout
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import hollywoodanimalmaster.composeapp.generated.resources.Res
import hollywoodanimalmaster.composeapp.generated.resources.action_cancel
import hollywoodanimalmaster.composeapp.generated.resources.action_copy
import hollywoodanimalmaster.composeapp.generated.resources.action_delete_all
import hollywoodanimalmaster.composeapp.generated.resources.action_export
import hollywoodanimalmaster.composeapp.generated.resources.action_import
import hollywoodanimalmaster.composeapp.generated.resources.action_more
import hollywoodanimalmaster.composeapp.generated.resources.action_new
import hollywoodanimalmaster.composeapp.generated.resources.action_save
import hollywoodanimalmaster.composeapp.generated.resources.action_undo
import hollywoodanimalmaster.composeapp.generated.resources.copy_header
import hollywoodanimalmaster.composeapp.generated.resources.copy_line
import hollywoodanimalmaster.composeapp.generated.resources.copy_suffix
import hollywoodanimalmaster.composeapp.generated.resources.copy_total
import hollywoodanimalmaster.composeapp.generated.resources.guide_title
import hollywoodanimalmaster.composeapp.generated.resources.library_clear_body
import hollywoodanimalmaster.composeapp.generated.resources.library_clear_title
import hollywoodanimalmaster.composeapp.generated.resources.library_title
import hollywoodanimalmaster.composeapp.generated.resources.msg_copied
import hollywoodanimalmaster.composeapp.generated.resources.msg_deleted
import hollywoodanimalmaster.composeapp.generated.resources.msg_duplicated
import hollywoodanimalmaster.composeapp.generated.resources.msg_exported
import hollywoodanimalmaster.composeapp.generated.resources.msg_import_failed
import hollywoodanimalmaster.composeapp.generated.resources.msg_import_unreadable
import hollywoodanimalmaster.composeapp.generated.resources.msg_imported
import hollywoodanimalmaster.composeapp.generated.resources.msg_library_cleared
import hollywoodanimalmaster.composeapp.generated.resources.msg_loaded
import hollywoodanimalmaster.composeapp.generated.resources.msg_new
import hollywoodanimalmaster.composeapp.generated.resources.msg_new_discarded
import hollywoodanimalmaster.composeapp.generated.resources.msg_saved
import hollywoodanimalmaster.composeapp.generated.resources.nav_guide
import hollywoodanimalmaster.composeapp.generated.resources.nav_library
import hollywoodanimalmaster.composeapp.generated.resources.nav_plan
import hollywoodanimalmaster.composeapp.generated.resources.nav_settings
import hollywoodanimalmaster.composeapp.generated.resources.plan_title
import hollywoodanimalmaster.composeapp.generated.resources.settings_title
import hollywoodanimalmaster.composeapp.generated.resources.untitled_movie
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.aalbertini.ham.core.ui.theme.HamTheme
import org.aalbertini.ham.core.util.format.grouped
import org.aalbertini.ham.core.util.format.toPlainString
import org.aalbertini.ham.features.guide.GuideScreen
import org.aalbertini.ham.features.movie_distribution.data.repository.ImportResult
import org.aalbertini.ham.features.movie_distribution.presentation.PlannerMessage
import org.aalbertini.ham.features.movie_distribution.presentation.PlannerUiState
import org.aalbertini.ham.features.movie_distribution.presentation.PlannerViewModel
import org.aalbertini.ham.features.movie_distribution.presentation.components.ImportDialog
import org.aalbertini.ham.features.movie_distribution.presentation.components.titleRes
import org.aalbertini.ham.features.movie_distribution.presentation.screen.LibraryScreen
import org.aalbertini.ham.features.movie_distribution.presentation.screen.PlanScreen
import org.aalbertini.ham.features.settings.presentation.SettingsScreen
import org.aalbertini.ham.features.settings.presentation.SettingsViewModel
import org.aalbertini.ham.features.settings.presentation.WindowControls
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getPluralString
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

private enum class Destination(val label: StringResource, val icon: ImageVector, val selectedIcon: ImageVector) {
    PLAN(Res.string.nav_plan, Icons.Outlined.Movie, Icons.Filled.Movie),
    LIBRARY(Res.string.nav_library, Icons.Outlined.VideoLibrary, Icons.Filled.VideoLibrary),
    GUIDE(Res.string.nav_guide, Icons.Outlined.AutoStories, Icons.Filled.AutoStories),
    SETTINGS(Res.string.nav_settings, Icons.Outlined.Settings, Icons.Filled.Settings),
}

/**
 * Root composable shared by every platform.
 * Desktop passes its window callbacks; other platforms leave them null and the section is hidden.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    alwaysOnTop: Boolean = false,
    onAlwaysOnTopChange: ((Boolean) -> Unit)? = null,
    onResetWindowSize: (() -> Unit)? = null,
) {
    val settingsViewModel = koinViewModel<SettingsViewModel>()
    val settings by settingsViewModel.settings.collectAsState()
    HamTheme(settings) {
        val planner = koinViewModel<PlannerViewModel>()
        val state by planner.state.collectAsState()
        var destination by rememberSaveable { mutableStateOf(Destination.PLAN) }
        var confirmClear by remember { mutableStateOf(false) }
        var importOpen by remember { mutableStateOf(false) }
        val snackbar = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        @Suppress("DEPRECATION") // LocalClipboard's ClipEntry has no common text constructor yet.
        val clipboard = LocalClipboardManager.current

        LaunchedEffect(planner) {
            // collectLatest: a new message cancels (dismisses) the snackbar still on screen.
            planner.messages.collectLatest { message ->
                val text = when (message) {
                    is PlannerMessage.Saved -> getString(Res.string.msg_saved, message.title.orUntitled())
                    is PlannerMessage.Opened -> getString(Res.string.msg_loaded, message.title.orUntitled())
                    is PlannerMessage.Deleted -> getString(Res.string.msg_deleted, message.movie.title.orUntitled())
                    is PlannerMessage.Duplicated -> getString(Res.string.msg_duplicated, message.title)
                    PlannerMessage.LibraryCleared -> getString(Res.string.msg_library_cleared)
                    is PlannerMessage.NewMovie -> getString(
                        if (message.discarded != null) Res.string.msg_new_discarded else Res.string.msg_new,
                    )
                    is PlannerMessage.Imported -> importText(message.result)
                    PlannerMessage.ImportFailed -> getString(Res.string.msg_import_failed)
                }
                // Anything that dropped work can be undone from the snackbar.
                val undo: (() -> Unit)? = when (message) {
                    is PlannerMessage.Deleted -> { { planner.restore(message.movie) } }
                    is PlannerMessage.NewMovie -> message.discarded?.let { draft -> { planner.restoreDraft(draft) } }
                    is PlannerMessage.Opened -> message.discarded?.let { draft -> { planner.restoreDraft(draft) } }
                    else -> null
                }
                val result = snackbar.showSnackbar(
                    message = text,
                    actionLabel = if (undo != null) getString(Res.string.action_undo) else null,
                    duration = if (undo != null) SnackbarDuration.Long else SnackbarDuration.Short,
                )
                if (result == SnackbarResult.ActionPerformed) undo?.invoke()
            }
        }

        val navigationType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo())
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        NavigationSuiteScaffoldLayout(
            navigationSuite = {
                AppNavigation(
                    type = navigationType,
                    current = destination,
                    onSelect = { destination = it },
                )
            },
            navigationSuiteType = navigationType,
        ) {
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    AppTopBar(
                        destination = destination,
                        state = state,
                        scrollBehavior = scrollBehavior,
                        onSave = planner::save,
                        onNew = planner::newMovie,
                        onCopy = {
                            scope.launch {
                                clipboard.setText(AnnotatedString(planText(state)))
                                snackbar.showSnackbar(getString(Res.string.msg_copied))
                            }
                        },
                        onClearLibrary = { confirmClear = true },
                        onExport = {
                            scope.launch {
                                val count = state.library.size
                                clipboard.setText(AnnotatedString(planner.exportText()))
                                snackbar.showSnackbar(getPluralString(Res.plurals.msg_exported, count, count))
                            }
                        },
                        onImport = { importOpen = true },
                    )
                },
                snackbarHost = { SnackbarHost(snackbar) },
                contentWindowInsets = WindowInsets(0),
            ) { padding ->
                val contentModifier = Modifier.padding(padding)
                when (destination) {
                    Destination.PLAN -> PlanScreen(state, planner::edit, contentModifier)
                    Destination.LIBRARY -> LibraryScreen(
                        movies = state.library,
                        currentId = state.movie.id,
                        onOpen = { id ->
                            planner.open(id)
                            destination = Destination.PLAN
                        },
                        onDuplicate = { id ->
                            scope.launch {
                                val suffix = getString(Res.string.copy_suffix, "%s")
                                planner.duplicate(id) { title -> suffix.replace("%s", title) }
                            }
                        },
                        onDelete = planner::delete,
                        onImport = { importOpen = true },
                        modifier = contentModifier,
                    )
                    Destination.GUIDE -> GuideScreen(contentModifier)
                    Destination.SETTINGS -> SettingsScreen(
                        settings = settings,
                        onUpdate = settingsViewModel::update,
                        windowControls = if (onAlwaysOnTopChange != null && onResetWindowSize != null) {
                            WindowControls(alwaysOnTop, onAlwaysOnTopChange, onResetWindowSize)
                        } else null,
                        modifier = contentModifier,
                    )
                }
            }
        }

        if (confirmClear) {
            AlertDialog(
                onDismissRequest = { confirmClear = false },
                title = { Text(stringResource(Res.string.library_clear_title)) },
                text = { Text(pluralStringResource(Res.plurals.library_clear_body, state.library.size, state.library.size)) },
                confirmButton = {
                    TextButton(onClick = {
                        confirmClear = false
                        planner.clearLibrary()
                    }) { Text(stringResource(Res.string.action_delete_all), color = MaterialTheme.colorScheme.error) }
                },
                dismissButton = {
                    TextButton(onClick = { confirmClear = false }) { Text(stringResource(Res.string.action_cancel)) }
                },
            )
        }

        if (importOpen) {
            ImportDialog(
                onImport = { text ->
                    importOpen = false
                    planner.import(text)
                },
                onDismiss = { importOpen = false },
            )
        }
    }
}

/**
 * Bottom bar on compact widths, rail otherwise (M3 adaptive navigation).
 * Built by hand rather than with NavigationSuiteScaffold so the rail gets top spacing.
 */
@Composable
private fun AppNavigation(type: NavigationSuiteType, current: Destination, onSelect: (Destination) -> Unit) {
    if (type == NavigationSuiteType.NavigationBar) {
        NavigationBar {
            Destination.entries.forEach { item ->
                NavigationBarItem(
                    selected = current == item,
                    onClick = { onSelect(item) },
                    icon = { Icon(if (current == item) item.selectedIcon else item.icon, contentDescription = null) },
                    label = { Text(stringResource(item.label)) },
                )
            }
        }
    } else {
        NavigationRail(
            containerColor = MaterialTheme.colorScheme.background,
            header = { Spacer(Modifier.height(8.dp)) },
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Destination.entries.forEach { item ->
                    NavigationRailItem(
                        selected = current == item,
                        onClick = { onSelect(item) },
                        icon = { Icon(if (current == item) item.selectedIcon else item.icon, contentDescription = null) },
                        label = { Text(stringResource(item.label)) },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppTopBar(
    destination: Destination,
    state: PlannerUiState,
    scrollBehavior: TopAppBarScrollBehavior,
    onSave: () -> Unit,
    onNew: () -> Unit,
    onCopy: () -> Unit,
    onClearLibrary: () -> Unit,
    onExport: () -> Unit,
    onImport: () -> Unit,
) {
    TopAppBar(
        title = {
            val title = when (destination) {
                Destination.PLAN -> state.movie.title.ifBlank { stringResource(Res.string.plan_title) }
                Destination.LIBRARY -> stringResource(Res.string.library_title)
                Destination.GUIDE -> stringResource(Res.string.guide_title)
                Destination.SETTINGS -> stringResource(Res.string.settings_title)
            }
            Text(title, maxLines = 1, overflow = TextOverflow.Ellipsis)
        },
        // Same container as the scaffold at rest; tinted once content scrolls under it.
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        scrollBehavior = scrollBehavior,
        actions = {
            when (destination) {
                Destination.PLAN -> {
                    ActionIcon(Icons.Outlined.ContentCopy, stringResource(Res.string.action_copy), onCopy)
                    ActionIcon(Icons.Outlined.Add, stringResource(Res.string.action_new), onNew)
                    if (state.isSaved) {
                        ActionIcon(Icons.Outlined.Save, stringResource(Res.string.action_save), onSave)
                    } else {
                        FilledTonalButton(onClick = onSave) { Text(stringResource(Res.string.action_save)) }
                    }
                }
                Destination.LIBRARY -> LibraryMenu(
                    hasMovies = state.library.isNotEmpty(),
                    onExport = onExport,
                    onImport = onImport,
                    onClearLibrary = onClearLibrary,
                )
                Destination.GUIDE, Destination.SETTINGS -> Unit
            }
        },
    )
}

/** Icon button with a tooltip that doubles as its accessible label. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActionIcon(icon: ImageVector, label: String, onClick: () -> Unit) {
    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = { PlainTooltip { Text(label) } },
        state = rememberTooltipState(),
    ) {
        IconButton(onClick = onClick) { Icon(icon, contentDescription = label) }
    }
}

/** Backup and bulk actions; rare enough to live in one overflow menu. */
@Composable
private fun LibraryMenu(hasMovies: Boolean, onExport: () -> Unit, onImport: () -> Unit, onClearLibrary: () -> Unit) {
    Box {
        var open by remember { mutableStateOf(false) }
        ActionIcon(Icons.Outlined.MoreVert, stringResource(Res.string.action_more)) { open = true }
        DropdownMenu(expanded = open, onDismissRequest = { open = false }) {
            DropdownMenuItem(
                text = { Text(stringResource(Res.string.action_export)) },
                leadingIcon = { Icon(Icons.Outlined.Upload, contentDescription = null) },
                enabled = hasMovies,
                onClick = { open = false; onExport() },
            )
            DropdownMenuItem(
                text = { Text(stringResource(Res.string.action_import)) },
                leadingIcon = { Icon(Icons.Outlined.Download, contentDescription = null) },
                onClick = { open = false; onImport() },
            )
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text(stringResource(Res.string.action_delete_all), color = MaterialTheme.colorScheme.error) },
                leadingIcon = {
                    Icon(Icons.Outlined.DeleteSweep, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                },
                enabled = hasMovies,
                onClick = { open = false; onClearLibrary() },
            )
        }
    }
}

private suspend fun importText(result: ImportResult): String {
    val summary = getString(Res.string.msg_imported, result.added, result.updated)
    if (result.unreadable == 0) return summary
    return summary + " · " + getString(Res.string.msg_import_unreadable, result.unreadable)
}

private suspend fun String.orUntitled(): String = ifBlank { getString(Res.string.untitled_movie) }

private suspend fun planText(state: PlannerUiState): String {
    val movie = state.movie
    val lines = buildList {
        add(
            getString(
                Res.string.copy_header,
                movie.title.orUntitled(),
                getString(movie.method.titleRes),
                movie.commercialScore.toPlainString(1),
            )
        )
        state.plan.weeks.forEach { week ->
            add(
                getString(
                    Res.string.copy_line,
                    week.number,
                    week.toRent.grouped(),
                    week.demand.grouped(),
                    week.ownScreenings.grouped(),
                )
            )
        }
        add(getString(Res.string.copy_total, state.plan.totalRent.grouped()))
    }
    return lines.joinToString("\n")
}
