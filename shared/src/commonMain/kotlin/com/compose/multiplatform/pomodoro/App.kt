package com.compose.multiplatform.pomodoro

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.compose.multiplatform.pomodoro.ui.screens.main.MainScreen
import com.compose.multiplatform.pomodoro.ui.theme.PomodoroAppTheme

@Composable
fun App() {
    PomodoroAppTheme {
        Navigator(MainScreen)
    }
}

expect fun isIos(): Boolean