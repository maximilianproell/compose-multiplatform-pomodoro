package com.compose.multiplatform.pomodoro.service

import co.touchlab.kermit.Logger
import com.compose.multiplatform.pomodoro.MR
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import platform.Foundation.NSDateComponents
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNCalendarNotificationTrigger
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNNotificationSound
import platform.UserNotifications.UNUserNotificationCenter
import kotlin.time.Duration.Companion.seconds

private const val IOS_NOTIFICATION_ID = "ios_kmp_pomodoro_notification_id"

actual class NotificationService actual constructor(private val timerService: TimerService) {

    private val logger = Logger.withTag(this::class.simpleName.toString())

    actual fun scheduleNotification() {
        if (timerService.timerStateFlow.value !is TimerService.TimerState.Running) {
            logger.d { "Skipping schedule of notification since timer is not running." }
            return
        }

        val notificationCenter = UNUserNotificationCenter.currentNotificationCenter()

        // Check if permission is granted
        notificationCenter.getNotificationSettingsWithCompletionHandler { settings ->
            logger.d { "Checking for notification permission." }
            if (settings?.authorizationStatus == UNAuthorizationStatusAuthorized) {
                logger.d { "Notification permission granted. Scheduling notification." }


                val notificationContent = UNMutableNotificationContent().apply {
                    setTitle(StringDesc.Resource(MR.strings.notification_timer_finished_title).localized())
                    setBody(StringDesc.Resource(MR.strings.notification_timer_finished_description).localized())
                    setSound(UNNotificationSound.defaultSound())
                }

                val currentTimeInstant = Clock.System.now()
                val notificationTime =
                    (currentTimeInstant + timerService.timerStateFlow.value.secondsLeft.seconds).toLocalDateTime(
                        TimeZone.currentSystemDefault()
                    )

                val date = NSDateComponents().apply {
                    hour = notificationTime.hour.toLong()
                    minute = notificationTime.minute.toLong()
                    second = notificationTime.second.toLong()
                }

                val trigger = UNCalendarNotificationTrigger.triggerWithDateMatchingComponents(
                    dateComponents = date,
                    repeats = false,
                )

                val request =
                    UNNotificationRequest.requestWithIdentifier(
                        IOS_NOTIFICATION_ID,
                        notificationContent,
                        trigger
                    )

                notificationCenter.addNotificationRequest(request) {
                    it?.let {
                        logger.e { "received error on addNotificationRequest: $it" }
                    }
                }
            } else {
                logger.d { "No permission to show notification." }
            }
        }
    }

    actual fun stopNotification() {
        val notificationCenter = UNUserNotificationCenter.currentNotificationCenter()
        notificationCenter.removePendingNotificationRequestsWithIdentifiers(listOf(IOS_NOTIFICATION_ID))
        logger.d { "Removed pending notification." }
    }
}
