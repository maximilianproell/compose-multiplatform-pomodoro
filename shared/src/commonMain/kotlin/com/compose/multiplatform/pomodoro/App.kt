package com.compose.multiplatform.pomodoro

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.compose.multiplatform.pomodoro.ui.screens.main.MainScreen
import com.compose.multiplatform.pomodoro.ui.theme.PomodoroAppTheme

@Composable
fun App() {
    PomodoroAppTheme {
        Navigator(MainScreen) {
            SlideTransition(it)
        }
    }
}

expect fun isIos(): Boolean

@Composable
expect fun KeepScreenOnHandler(keepScreenOn: Boolean)