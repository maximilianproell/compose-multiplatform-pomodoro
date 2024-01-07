package com.compose.multiplatform.pomodoro.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun saveTimerFinishTimestamp(timestamp: Long)

    /**
     * Gets the currently set timestamp for when the timer will finish.
     * @return The unix epoch timestamp for when the timer will be up. `null` if there is currently
     * no timer ongoing.
     */
    suspend fun getTimerFinishTimestamp(): Long?

    /**
     * Removes any set finish timestamp. Subsequent calls to [getTimerFinishTimestamp] will return `null` until
     * a new timestamp has been set with [saveTimerFinishTimestamp].
     */
    suspend fun removeTimerFinishTimestamp()

    /**
     * Saves the timer duration in minutes.
     */
    suspend fun saveTimerDurationMinutes(minutes: Int)

    /**
     * Returns the pomodoro timer duration in minutes.
     */
    suspend fun getTimerDurationMinutes(): Int

    /**
     * Returns a [Flow] emitting the most recently set timer duration in minutes.
     */
    fun observeTimerDurationMinutes(): Flow<Int>

    /**
     * Saves the "Keep screen on" setting.
     */
    suspend fun saveKeepScreenOn(keepOn: Boolean)

    /**
     * Returns a [Flow] emitting the most recently set "Keep screen on" setting.
     */
    fun observeKeepScreeOn(): Flow<Boolean>
}