package org.aalbertini.ham

import androidx.compose.ui.window.ComposeUIViewController
import org.aalbertini.ham.di.DI

fun MainViewController() = ComposeUIViewController {
    // Initialize Dependency Injection
    DI.init()
    App()
}