package org.example.project

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import kotlinx.coroutines.launch

enum class Screen {
    HOME,
    SECOND
}

@Composable
fun App(preferencesManager: IPreferencesManager) {
    var currentScreen by remember { mutableStateOf(Screen.HOME) }
    var sharedData by remember { mutableStateOf("") }
    var initialPostId by remember { mutableStateOf(1) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        preferencesManager.getLastPostIdFlow().collect { id ->
            initialPostId = id
        }
    }

    MaterialTheme {
        when (currentScreen) {
            Screen.HOME -> HomeScreen(
                onNavigateToSecond = { data ->
                    sharedData = data
                    currentScreen = Screen.SECOND
                }
            )
            Screen.SECOND -> SecondScreen(
                onBack = { currentScreen = Screen.HOME },
                initialPostId = initialPostId,
                onSavePostId = { id ->
                    scope.launch {
                        preferencesManager.saveLastPostId(id)
                    }
                }
            )
        }
    }
}