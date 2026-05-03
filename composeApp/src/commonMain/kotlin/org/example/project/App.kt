package org.example.project

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*

enum class Screen {
    HOME,
    SECOND
}

@Composable
fun App() {
    var currentScreen by remember { mutableStateOf(Screen.HOME) }
    var sharedData by remember { mutableStateOf("") }

    MaterialTheme {
        when (currentScreen) {
            Screen.HOME -> HomeScreen(
                onNavigateToSecond = { data ->
                    sharedData = data
                    currentScreen = Screen.SECOND
                }
            )
            Screen.SECOND -> SecondScreen(
                onBack = { currentScreen = Screen.HOME }
            )
        }
    }
}