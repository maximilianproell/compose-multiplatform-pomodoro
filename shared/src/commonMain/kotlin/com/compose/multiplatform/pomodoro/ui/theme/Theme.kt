package com.compose.multiplatform.pomodoro.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

val darkPomodoroColorScheme = darkColorScheme(
    // TODO: add colors
)

val lightPomodoroColorScheme = lightColorScheme(
    // TODO: add colors
)

@Composable
fun PomodoroAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) darkPomodoroColorScheme else lightPomodoroColorScheme
    ) {
        content()
    }
}