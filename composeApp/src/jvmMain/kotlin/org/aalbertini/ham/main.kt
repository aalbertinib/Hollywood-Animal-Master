package org.aalbertini.ham

import androidx.compose.runtime.LaunchedEffect
import java.awt.Dimension
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import hollywoodanimalmaster.composeapp.generated.resources.Res
import hollywoodanimalmaster.composeapp.generated.resources.app_icon
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import androidx.compose.ui.unit.dp
import hollywoodanimalmaster.composeapp.generated.resources.window_title
import org.aalbertini.ham.preferences.DesktopWindowSettings
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.aalbertini.ham.di.DI

@OptIn(FlowPreview::class)
fun main() = application {

    // Initialize Dependency Injection
    DI.init()

    // Initialize desktop window settings
    val windowSettings = remember { DesktopWindowSettings() }
    // Load saved window size or use default
    val defaultSize = DpSize(1280.dp, 860.dp)
    val initialSize = remember { windowSettings.loadWindowSize(defaultSize) }
    
    // Create window state with loaded size
    val windowState = remember { WindowState(size = initialSize) }
    
    var alwaysOnTop by remember { mutableStateOf(false) }
    
    // Track window size changes and save them
    LaunchedEffect(windowState) {
        snapshotFlow { windowState.size }
            .debounce(500) // Debounce to avoid saving too frequently during resize
            .collect { size ->
                windowSettings.saveWindowSize(size)
            }
    }
    
    // Reset window size to default
    val resetWindowSize: () -> Unit = {
        windowSettings.clearWindowSettings()
        windowState.size = defaultSize
    }
    
    Window(
        onCloseRequest = ::exitApplication,
        title = stringResource(Res.string.window_title),
        state = windowState,
        alwaysOnTop = alwaysOnTop,
        icon = painterResource(Res.drawable.app_icon)
    ) {
        // Below phone width the layout has nothing left to give.
        LaunchedEffect(Unit) { window.minimumSize = Dimension(360, 560) }
        App(
            alwaysOnTop = alwaysOnTop,
            onAlwaysOnTopChange = { alwaysOnTop = it },
            onResetWindowSize = resetWindowSize
        )
    }
}