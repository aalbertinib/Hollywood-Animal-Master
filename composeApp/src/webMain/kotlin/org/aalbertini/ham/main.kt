package org.aalbertini.ham

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import org.aalbertini.ham.di.DI

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    // Initialize Dependency Injection
    DI.init()
    ComposeViewport {
        App()
    }
}