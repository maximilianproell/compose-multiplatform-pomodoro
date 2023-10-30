package com.compose.multiplatform.pomodoro.permission

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

actual class PermissionController : KoinComponent {
    actual suspend fun hasPermissionForNotification(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationPermission = ContextCompat.checkSelfPermission(
                get(),
                Manifest.permission.POST_NOTIFICATIONS
            )
            notificationPermission == PackageManager.PERMISSION_GRANTED
        } else true
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    actual fun NotificationPermissionRequester(
        askForPermission: Boolean,
        onResult: (Boolean) -> Unit,
        onPermissionRequestShown: () -> Unit,
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationPermissionState = rememberPermissionState(
                permission = Manifest.permission.POST_NOTIFICATIONS
            )

            LaunchedEffect(key1 = notificationPermissionState.status) {
                if (notificationPermissionState.status.isGranted) {
                    onResult(true)
                } else onResult(false)
            }

            LaunchedEffect(key1 = askForPermission) {
                if (askForPermission) {
                    notificationPermissionState.launchPermissionRequest()
                    onPermissionRequestShown()
                }
            }
        } else {
            onResult(true)
        }
    }
}