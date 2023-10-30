package com.compose.multiplatform.pomodoro

import androidx.compose.ui.window.ComposeUIViewController
import com.compose.multiplatform.pomodoro.App

actual fun getPlatformName(): String = "iOS"

fun MainViewController() = ComposeUIViewController { App() }