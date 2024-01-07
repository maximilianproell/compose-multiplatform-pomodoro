package com.compose.multiplatform.pomodoro.permission

import android.Manifest
import android.app.AlarmManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
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
        val context = LocalContext.current

        fun triggerRequestExactAlarm() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val alarmManager = context.getSystemService<AlarmManager>()
                alarmManager?.let {
                    if (!it.canScheduleExactAlarms()) {
                        context.startActivity(
                            Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                                val packageName = context.packageName
                                data = Uri.parse("package:$packageName")
                            }
                        )
                    }
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationPermissionState = rememberPermissionState(
                permission = Manifest.permission.POST_NOTIFICATIONS
            )

            LaunchedEffect(key1 = notificationPermissionState.status) {
                if (notificationPermissionState.status.isGranted) {
                    onResult(true)
                    triggerRequestExactAlarm()
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
            LaunchedEffect(key1 = Unit) {
                triggerRequestExactAlarm()
            }
        }
    }
}