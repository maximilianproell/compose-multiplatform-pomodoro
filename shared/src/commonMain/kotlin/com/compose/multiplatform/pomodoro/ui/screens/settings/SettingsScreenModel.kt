package com.compose.multiplatform.pomodoro.ui.screens.settings

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.compose.multiplatform.pomodoro.domain.repository.SettingsRepository
import com.compose.multiplatform.pomodoro.service.TimerService
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SettingsScreenModel : StateScreenModel<SettingsScreenModel.SettingsScreenState>(SettingsScreenState()),
    KoinComponent {

    private val settingsRepository: SettingsRepository by inject()
    private val timerService: TimerService by inject()

    private val applicationScope = MainScope()

    data class SettingsScreenState(
        val isLoading: Boolean = true,
        val pomodoroTimerMinutes: Int? = null,
        val inputBlocked: Boolean = false,
    )

    init {
        screenModelScope.launch {
            val minutesDuration = settingsRepository.getTimerDurationMinutes()
            mutableState.update { it.copy(
                pomodoroTimerMinutes = minutesDuration,
                isLoading = false,
            ) }

            timerService.timerStateFlow.collect { timerState ->
                // Block changes to settings while timer is running.
                val inputBlocked = timerState is TimerService.TimerState.Running || timerState is TimerService.TimerState.Paused
                mutableState.update { settingsScreenState ->
                    settingsScreenState.copy(inputBlocked = inputBlocked)
                }
            }
        }
    }

    fun updateTimerMinutes(minutes: Int?) {
        mutableState.update {
            it.copy(pomodoroTimerMinutes = minutes)
        }
    }

    private fun saveSettings() {
        val setTimerMinutes = state.value.pomodoroTimerMinutes
        if (setTimerMinutes != null) {
            applicationScope.launch {
                settingsRepository.saveTimerDurationMinutes(setTimerMinutes)
            }
        }
    }

    override fun onDispose() {
        // Save data when we leave the screen.
        saveSettings()

        super.onDispose()
    }
}