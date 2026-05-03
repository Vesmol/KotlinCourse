package org.example.project

import androidx.compose.runtime.Composable

@Composable
actual fun rememberLocationHelper(
    onLocation: (Double, Double) -> Unit
) {
    onLocation(51.4018, 39.1238)
}