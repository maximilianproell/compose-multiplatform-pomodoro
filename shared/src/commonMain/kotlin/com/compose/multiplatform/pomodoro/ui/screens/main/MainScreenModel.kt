package com.compose.multiplatform.pomodoro.ui.screens.main

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import co.touchlab.kermit.Logger
import com.compose.multiplatform.pomodoro.domain.repository.SettingsRepository
import com.compose.multiplatform.pomodoro.permission.PermissionController
import com.compose.multiplatform.pomodoro.service.TimerService
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject

class MainScreenModel : StateScreenModel<MainScreenModel.MainScreenState>(MainScreenState()), KoinComponent {

    private val logger = Logger.withTag(this::class.simpleName ?: "")
    private val timerService: TimerService = get()
    val permissionController = PermissionController()
    private val settingsRepository: SettingsRepository by inject()

    data class MainScreenState(
        val timerState: TimerService.TimerState = TimerService.TimerState.Initial.withDefaultDuration,
        val showStopTimerAlert: Boolean = false,
        val hasPermissionForNotifications: Boolean = false,
        val showNotificationPermissionRequest: Boolean = false,
        val keepScreenOn: Boolean = false,
    )

    init {
        screenModelScope.launch {
            timerService
                .timerStateFlow
                .combine(settingsRepository.observeKeepScreeOn(), ::Pair)
                .collect { (timerState, keepScreenOn) ->
                    // Only keep screen on when timer is actually running or paused.
                    val tmpKeepScreenOn = (timerState is TimerService.TimerState.Running
                            || timerState is TimerService.TimerState.Paused)
                            && keepScreenOn
                    mutableState.update {
                        it.copy(
                            timerState = timerState,
                            keepScreenOn = tmpKeepScreenOn,
                        )
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
