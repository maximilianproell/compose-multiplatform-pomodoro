package com.compose.multiplatform.pomodoro

import android.app.Activity
import android.os.Build
import android.view.WindowManager
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

actual fun isIos(): Boolean = false

@Composable
actual fun KeepScreenOnHandler(keepScreenOn: Boolean) {
    val context = LocalContext.current
    DisposableEffect(keepScreenOn) {
        val activity = (context as? Activity)
        val window = activity?.window

        fun keepOn(on: Boolean) {
            if (on) {
                window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                activity?.setShowWhenLocked(true)
            } else {
                window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                activity?.setShowWhenLocked(false)
            }
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

@Composable
fun MainView() {
    val view = LocalView.current
    val isDarkTheme = isSystemInDarkTheme()
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            window.statusBarColor = Color.Transparent.toArgb()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window.isNavigationBarContrastEnforced = false
            }

            val windowsInsetsController = WindowCompat.getInsetsController(window, view)

            windowsInsetsController.isAppearanceLightStatusBars = !isDarkTheme
        }
    }

    App()
}
