package service


actual class NotificationService actual constructor(private val timerService: TimerService) {
    actual fun scheduleNotification() {
        // TODO: Start foreground service, which updates the notification
    }

    actual fun stopNotification() {
        // TODO: Stop the foreground service
    }
}
