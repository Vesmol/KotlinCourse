package org.example.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    val preferencesManager: IPreferencesManager = PreferencesManagerStub()
    Window(
        onCloseRequest = ::exitApplication,
        title = "Shopping List"
    ) {
        App(preferencesManager)
    }
}