package com.compose.multiplatform.pomodoro.ui.screens.settings

import cafe.adriel.voyager.core.model.StateScreenModel
import com.compose.multiplatform.pomodoro.domain.repository.SettingsRepository
import com.compose.multiplatform.pomodoro.utils.Result
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SettingsScreenModel : StateScreenModel<Result<SettingsScreenModel.SettingsScreenState>>(Result.Loading),
    KoinComponent {

    private val settingsRepository: SettingsRepository by inject()

    data class SettingsScreenState(
        val pomodoroTimerMinutes: Int
    )

    init {
        //settingsRepository.getTimerFinishTimestamp()
    }
}