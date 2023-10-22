import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import service.NotificationService

class LifecycleHelper : KoinComponent {
    private val notificationService: NotificationService = get()

    fun onAppMovedToBackGround() {
        notificationService.scheduleNotification()
    }

    fun onAppMovedToForeground() {
        notificationService.stopNotification()
    }
}