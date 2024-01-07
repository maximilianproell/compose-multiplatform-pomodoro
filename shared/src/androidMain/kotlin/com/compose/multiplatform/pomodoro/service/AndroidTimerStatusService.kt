package com.compose.multiplatform.pomodoro.service

import android.Manifest
import android.app.ForegroundServiceStartNotAllowedException
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import co.touchlab.kermit.Logger
import com.compose.multiplatform.pomodoro.MR
import com.compose.multiplatform.pomodoro.MainActivity
import com.compose.multiplatform.pomodoro.R
import com.compose.multiplatform.pomodoro.domain.usecase.FormatSecondsForTimerUseCase
import com.compose.multiplatform.pomodoro.service.NotificationService.Companion.notificationChannelId
import com.compose.multiplatform.pomodoro.utils.getString
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * This service is responsible for showing and updating a sticky notification, displaying the
 * time left of the pomodoro timer.
 */
class AndroidTimerStatusService : LifecycleService(), KoinComponent {

    private val timerService: TimerService by inject()
    private val stickyNotificationId = 100
    private val logger = Logger.withTag(this::class.simpleName.toString())

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        logger.d { "Starting foreground service" }

        val notificationIntent = Intent(this, MainActivity::class.java).apply {
            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val stickyNotificationBuilder = NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle(getString(MR.strings.pomodoro_timer_notification_title))
            .setSmallIcon(R.drawable.baseline_timer_24)
            .setOnlyAlertOnce(true)
            .setSilent(true)
            .setOngoing(true)
            .setContentText(
                getString(
                    MR.strings.pomodoro_timer_notification_content_text,
                    FormatSecondsForTimerUseCase(timerService.timerStateFlow.value.secondsLeft)
                )
            )
            .setContentIntent(pendingIntent)

        try {
            ServiceCompat.startForeground(
                /* service = */ this,
                /* id = */ stickyNotificationId, // Cannot be 0
                /* notification = */ stickyNotificationBuilder.build(),
                /* foregroundServiceType = */ 0,
            )
        } catch (e: Exception) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                && e is ForegroundServiceStartNotAllowedException
            ) {
                logger.e { "Failed starting foreground service: ${e.stackTraceToString()}" }
            }
        }

        val notificationManager = NotificationManagerCompat.from(applicationContext)
        lifecycleScope.launch {
            timerService.timerStateFlow.collect { timerState ->
                stickyNotificationBuilder.setContentText(
                    getString(
                        MR.strings.pomodoro_timer_notification_content_text,
                        FormatSecondsForTimerUseCase(timerState.secondsLeft)
                    )
                )

                if (timerState is TimerService.TimerState.Initial) {
                    stopSelf()
                } else {
                    notificationManager.notify(
                        stickyNotificationId,
                        stickyNotificationBuilder.build()
                    )
                }
            }
        }

        return START_STICKY
    }

    override fun onDestroy() {
        val notificationManager = NotificationManagerCompat.from(applicationContext)
        notificationManager.cancel(stickyNotificationId)

        super.onDestroy()
    }
}