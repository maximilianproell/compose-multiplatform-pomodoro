package ui.screens.main

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import permission.PermissionController
import service.TimerService

class MainScreenModel : StateScreenModel<MainScreenModel.MainScreenState>(MainScreenState()), KoinComponent {

    private val logger = Logger.withTag(this::class.simpleName ?: "")
    private val timerService: TimerService = get()
    val permissionController = PermissionController()

    data class MainScreenState(
        val timerState: TimerService.TimerState = TimerService.TimerState.Initial,
        val showStopTimerAlert: Boolean = false,
        val hasPermissionForNotifications: Boolean = false,
        val showNotificationPermissionRequest: Boolean = false,
    )

    init {
        coroutineScope.launch {
            timerService.timerStateFlow.collect { timerState ->
                mutableState.update {
                    it.copy(timerState = timerState)
                }
            }
        }
    }

    fun onTimerButtonClick() {
        logger.d { "Timer button clicked." }
        timerService.toggleTimer()
    }

    fun showNotificationPermissionRequest() {
        mutableState.update {
            it.copy(
                showNotificationPermissionRequest = true,
            )
        }
    }

    fun onPermissionRequestShown() {
        mutableState.update {
            it.copy(
                showNotificationPermissionRequest = false,
            )
        }
    }

    fun updateNotificationPermissionState(isGranted: Boolean) {
        logger.d { "Notification permission state changed: Granted = $isGranted" }
        mutableState.update {
            it.copy(hasPermissionForNotifications = isGranted)
        }
    }

    fun onStopTimerClick() {
        logger.d { "On stop timer clicked." }
        // TODO: Ask with alert dialog if user really wants to stop the progress.
        timerService.stopTimer()
    }

}
