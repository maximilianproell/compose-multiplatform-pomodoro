package com.compose.multiplatform.pomodoro.service

import android.content.Context
import android.content.Intent
import org.koin.core.component.KoinComponent
import org.koin.core.component.get


actual class NotificationService actual constructor(private val timerService: TimerService) : KoinComponent {

    actual fun scheduleNotification() {
        val context: Context = get()

        // Only start foreground service if timer is running
        if (timerService.timerStateFlow.value is TimerService.TimerState.Running) {
            context.startForegroundService(Intent(context, AndroidTimerStatusService::class.java))
        }
    }

    actual fun stopNotification() {
        val context: Context = get()
        context.stopService(Intent(context, AndroidTimerStatusService::class.java))
    }
}
