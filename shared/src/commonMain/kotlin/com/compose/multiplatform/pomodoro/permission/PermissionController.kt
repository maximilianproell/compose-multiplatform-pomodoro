package com.compose.multiplatform.pomodoro.permission

import androidx.compose.runtime.Composable

expect class PermissionController() {
    suspend fun hasPermissionForNotification(): Boolean

    @Composable
    fun NotificationPermissionRequester(
        askForPermission: Boolean,
        onResult: (Boolean) -> Unit,
        onPermissionRequestShown: () -> Unit,
    )
}