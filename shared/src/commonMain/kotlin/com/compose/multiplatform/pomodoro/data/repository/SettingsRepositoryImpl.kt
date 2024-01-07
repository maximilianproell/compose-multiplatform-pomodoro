package com.compose.multiplatform.pomodoro.data.repository

import com.compose.multiplatform.pomodoro.data.repository.SettingsRepositoryImpl.Constants.KEEP_SCREEN_ON
import com.compose.multiplatform.pomodoro.data.repository.SettingsRepositoryImpl.Constants.TIMER_DURATION_MINUTES
import com.compose.multiplatform.pomodoro.data.repository.SettingsRepositoryImpl.Constants.TIMER_FINISH_TIMESTAMP_KEY
import com.compose.multiplatform.pomodoro.domain.repository.SettingsRepository
import com.compose.multiplatform.pomodoro.service.TimerService.Companion.POMODORO_TIMER_DEFAULT_MINUTES
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalSettingsApi::class)
class SettingsRepositoryImpl(private val settings: FlowSettings) : SettingsRepository {

    private object Constants {
        const val TIMER_FINISH_TIMESTAMP_KEY = "timestampFinishKey"
        const val TIMER_DURATION_MINUTES = "timerDurationInMinutes"
        const val KEEP_SCREEN_ON = "keepScreenOn"
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

    override fun observeTimerDurationMinutes(): Flow<Int> {
        return settings.getIntFlow(TIMER_DURATION_MINUTES, POMODORO_TIMER_DEFAULT_MINUTES)
    }

    override suspend fun saveKeepScreenOn(keepOn: Boolean) {
        settings.putBoolean(KEEP_SCREEN_ON, keepOn)
    }

    override fun observeKeepScreeOn(): Flow<Boolean> {
        return settings.getBooleanFlow(KEEP_SCREEN_ON, defaultValue = false)
    }
}