package permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import co.touchlab.kermit.Logger
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNUserNotificationCenter
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

actual class PermissionController {

    private val logger = Logger.withTag(this::class.simpleName.toString())

    actual suspend fun hasPermissionForNotification() = suspendCoroutine { continuation ->
        val notificationCenter = UNUserNotificationCenter.currentNotificationCenter()
        notificationCenter.getNotificationSettingsWithCompletionHandler { settings ->
            if (settings?.authorizationStatus == UNAuthorizationStatusAuthorized)
                continuation.resume(true)
            else continuation.resume(false)
        }
    }

    @Composable
    actual fun NotificationPermissionRequester(
        askForPermission: Boolean,
        onResult: (Boolean) -> Unit,
        onPermissionRequestShown: () -> Unit,
    ) {
        val notificationCenter = UNUserNotificationCenter.currentNotificationCenter()

        LaunchedEffect(key1 = askForPermission) {
            if (askForPermission) {
                logger.d { "Asking for notification permission" }

                notificationCenter.requestAuthorizationWithOptions(
                    UNAuthorizationOptionAlert
                            + UNAuthorizationOptionBadge
                            + UNAuthorizationOptionSound
                ) { granted, _ ->
                    onResult(granted)
                }
                onPermissionRequestShown()
            }
        }
    }
}