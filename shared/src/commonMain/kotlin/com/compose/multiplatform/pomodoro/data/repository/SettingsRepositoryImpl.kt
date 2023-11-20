package com.compose.multiplatform.pomodoro.data.repository

import com.compose.multiplatform.pomodoro.data.repository.SettingsRepositoryImpl.Constants.TIMER_DURATION_MINUTES
import com.compose.multiplatform.pomodoro.data.repository.SettingsRepositoryImpl.Constants.TIMER_FINISH_TIMESTAMP_KEY
import com.compose.multiplatform.pomodoro.domain.repository.SettingsRepository
import com.compose.multiplatform.pomodoro.service.TimerService.Companion.POMODORO_TIMER_DEFAULT_MINUTES
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.SuspendSettings

@OptIn(ExperimentalSettingsApi::class)
class SettingsRepositoryImpl(private val settings: SuspendSettings) : SettingsRepository {

    private object Constants {
        const val TIMER_FINISH_TIMESTAMP_KEY = "timestampFinishKey"
        const val TIMER_DURATION_MINUTES = "timerDurationInMinutes"
    }

    override suspend fun saveTimerFinishTimestamp(timestamp: Long) {
        settings.putLong(TIMER_FINISH_TIMESTAMP_KEY, timestamp)
    }


    override suspend fun removeTimerFinishTimestamp() {
        settings.remove(TIMER_FINISH_TIMESTAMP_KEY)
    }

    override suspend fun getTimerFinishTimestamp(): Long? {
        return settings.getLongOrNull(TIMER_FINISH_TIMESTAMP_KEY)
    }

    override suspend fun saveTimerDurationMinutes(minutes: Int) {
        require(minutes > 0)
        settings.putInt(TIMER_DURATION_MINUTES, minutes)
    }

    override suspend fun getTimerDurationMinutes(): Int {
        return settings.getInt(TIMER_DURATION_MINUTES, POMODORO_TIMER_DEFAULT_MINUTES)
    }
}