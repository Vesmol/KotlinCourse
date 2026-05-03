package org.example.project

import androidx.compose.runtime.Composable

@Composable
expect fun rememberLocationHelper(
    onLocation: (Double, Double) -> Unit
)