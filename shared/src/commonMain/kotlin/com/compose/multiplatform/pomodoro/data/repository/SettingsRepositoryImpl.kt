package com.compose.multiplatform.pomodoro.data.repository

import com.compose.multiplatform.pomodoro.data.repository.SettingsRepositoryImpl.Constants.TIMER_FINISH_TIMESTAMP_KEY
import com.compose.multiplatform.pomodoro.domain.repository.SettingsRepository
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.SuspendSettings

@OptIn(ExperimentalSettingsApi::class)
class SettingsRepositoryImpl(private val settings: SuspendSettings) : SettingsRepository {

    private object Constants {
        const val TIMER_FINISH_TIMESTAMP_KEY = "timestampFinishKey"
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
}