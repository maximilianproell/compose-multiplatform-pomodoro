package com.compose.multiplatform.pomodoro

import androidx.compose.ui.window.ComposeUIViewController

actual fun isIos(): Boolean = true

fun MainViewController() = ComposeUIViewController { App() }