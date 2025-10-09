package org.aalbertini.ham

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import org.aalbertini.ham.ui.components.UiConstants
import org.aalbertini.ham.ui.components.UiStrings

fun main() = application {
    var alwaysOnTop by remember { mutableStateOf(false) }
    
    Window(
        onCloseRequest = ::exitApplication,
        title = UiStrings.SCREEN_TITLE,
        state = WindowState(size = DpSize(UiConstants.Window.desktopWidth, UiConstants.Window.desktopHeight)),
        alwaysOnTop = alwaysOnTop,
        icon = painterResource("app-icon.svg")
    ) {
        App(
            alwaysOnTop = alwaysOnTop,
            onAlwaysOnTopChange = { alwaysOnTop = it }
        )
    }
}