package com.compose.multiplatform.pomodoro

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIApplication

actual fun isIos(): Boolean = true

@Composable
actual fun KeepScreenOnHandler(keepScreenOn: Boolean) {
    DisposableEffect(keepScreenOn) {
        fun keepOn(on: Boolean) {
            UIApplication.sharedApplication.setIdleTimerDisabled(idleTimerDisabled = on)
        }

        if (keepScreenOn) {
            keepOn(true)
        } else {
            keepOn(false)
        }

        onDispose {
            keepOn(false)
        }
    }
}

fun MainViewController() = ComposeUIViewController { App() }