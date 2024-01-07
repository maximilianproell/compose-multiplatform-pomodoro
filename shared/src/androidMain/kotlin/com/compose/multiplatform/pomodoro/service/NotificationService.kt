package com.compose.multiplatform.pomodoro.service

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import com.compose.multiplatform.pomodoro.MR
import com.compose.multiplatform.pomodoro.MainActivity
import com.compose.multiplatform.pomodoro.R
import com.compose.multiplatform.pomodoro.service.NotificationService.AlarmNotificationReceiver.Companion.getAlarmNotificationPendingIntent
import com.compose.multiplatform.pomodoro.utils.createLogger
import com.compose.multiplatform.pomodoro.utils.getString
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import kotlin.time.Duration.Companion.seconds


actual class NotificationService actual constructor(private val timerService: TimerService) : KoinComponent {

    private val logger = createLogger()

    companion object {
        const val notificationChannelId = "pomodoro_timer_channel"
        const val finishNotificationId = 101
    }

    actual fun scheduleNotification() {
        logger.d { "Scheduling notification" }
        val context: Context = get()
        val alarmManager: AlarmManager = context.getSystemService()!!

        // Only start foreground service if timer is running
        if (timerService.timerStateFlow.value is TimerService.TimerState.Running) {
            context.startForegroundService(Intent(context, AndroidTimerStatusService::class.java))
            // Set alarm notification
            fun setExactAlarm() {
                val alarmTimestamp = System.currentTimeMillis() + timerService.timerStateFlow.value
                    .secondsLeft
                    .seconds
                    .inWholeMilliseconds

                logger.d { "Setting exact alarm for timestamp $alarmTimestamp" }
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    alarmTimestamp,
                    getAlarmNotificationPendingIntent(context),
                )
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && alarmManager.canScheduleExactAlarms()) {
                setExactAlarm()
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                setExactAlarm()
            } else {
                logger.e { "Can't set alarm. Not allowed." }
            }
        }
    }

    class AlarmNotificationReceiver : BroadcastReceiver() {
        private val logger = createLogger()

        @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
        override fun onReceive(context: Context?, intent: Intent?) {
            logger.d { "Received alarm notification from alarm manager. Notifying user with notification." }
            context?.let {
                val notificationManager = NotificationManagerCompat.from(it)
                val notificationIntent = Intent(it, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                val pendingIntent = PendingIntent.getActivity(
                    it, 0,
                    notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                val alarmNotification = NotificationCompat.Builder(it, notificationChannelId)
                    .setSmallIcon(R.drawable.baseline_timer_24)
                    .setContentTitle(it.getString(MR.strings.notification_timer_finished_title))
                    .setContentText(it.getString(MR.strings.notification_timer_finished_description))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build()

                notificationManager.notify(finishNotificationId, alarmNotification)
            }
        }

        companion object {
            fun getAlarmNotificationPendingIntent(context: Context): PendingIntent = PendingIntent.getBroadcast(
                context,
                1,
                Intent(context, AlarmNotificationReceiver::class.java),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

    actual fun stopNotification() {
        logger.d { "Stopping scheduled alarm and removing foreground service." }
        val context: Context = get()
        val alarmManager: AlarmManager = context.getSystemService()!!

        context.stopService(Intent(context, AndroidTimerStatusService::class.java))
        alarmManager.cancel(getAlarmNotificationPendingIntent(context))
    }
}
