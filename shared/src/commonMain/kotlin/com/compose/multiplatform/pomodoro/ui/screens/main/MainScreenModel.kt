package com.compose.multiplatform.pomodoro.ui.screens.main

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import com.compose.multiplatform.pomodoro.permission.PermissionController
import com.compose.multiplatform.pomodoro.service.TimerService

class MainScreenModel : StateScreenModel<MainScreenModel.MainScreenState>(MainScreenState()), KoinComponent {

    private val logger = Logger.withTag(this::class.simpleName ?: "")
    private val timerService: TimerService = get()
    val permissionController = PermissionController()

    data class MainScreenState(
        val timerState: TimerService.TimerState = TimerService.TimerState.Initial.withDefaultDuration,
        val showStopTimerAlert: Boolean = false,
        val hasPermissionForNotifications: Boolean = false,
        val showNotificationPermissionRequest: Boolean = false,
    )

    init {
        screenModelScope.launch {
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
        mutableState.update {
            it.copy(showStopTimerAlert = true)
        }
    }

    fun onDismissStopTimerAlert() {
        mutableState.update {
            it.copy(showStopTimerAlert = false)
        }
    }

    fun stopTimer() {
        onDismissStopTimerAlert()
        timerService.stopTimer()
    }
}
