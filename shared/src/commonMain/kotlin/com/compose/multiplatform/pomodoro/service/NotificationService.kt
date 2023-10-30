package com.compose.multiplatform.pomodoro.service

expect class NotificationService(timerService: TimerService) {

    /**
     * Schedules a notification showing the state of this timer.
     */
    fun scheduleNotification()

    /**
     * Stops or removes any ongoing/planned notifications.
     */
    fun stopNotification()
}
