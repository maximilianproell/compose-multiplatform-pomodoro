package com.compose.multiplatform.pomodoro.service

import com.compose.multiplatform.pomodoro.domain.model.WorkPackage
import com.compose.multiplatform.pomodoro.domain.repository.WorkPackageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.seconds

class TimerService(private val workPackageRepository: WorkPackageRepository) {

    companion object {
        const val POMODORO_TIMER_INITIAL_MINUTES = 25
        const val POMODORO_TIMER_INITIAL_SECONDS = POMODORO_TIMER_INITIAL_MINUTES * 60
    }

    /**
     * The job responsible for counting down every second.
     */
    private var countdownJob: Job? = null

    // Normally, that would be injected for better testability
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _timerStateFlow: MutableStateFlow<TimerState> = MutableStateFlow(TimerState.Initial)
    val timerStateFlow = _timerStateFlow.asStateFlow()

    /**
     * Stops the timer and discards any progress.
     */
    fun stopTimer() {
        cancelTimer()
        _timerStateFlow.value = TimerState.Initial
    }

    /**
     * Cancels the [countdownJob] and sets it to `null`
     */
    private fun cancelTimer() {
        countdownJob?.cancel()
        countdownJob = null
    }

    private fun startCountdownFlow(initialSeconds: Int) = flow {
        val startTimestampInSeconds = Clock.System.now().epochSeconds

        // Immediately emit the initial seconds left.
        emit(initialSeconds)
        while (true) {
            delay(1.seconds)
            val currentEpochTimestamp = Clock.System.now().epochSeconds

            // Subtract seconds passed since timer was started.
            val secondsPassed = (currentEpochTimestamp - startTimestampInSeconds).toInt()
            emit(initialSeconds - secondsPassed)
        }
        // Buffer values so that timer runs without hindrance.
    }.conflate()


    /**
     * Starts, pauses or continues the timer.
     */
    fun toggleTimer() {
        when (val timerState = timerStateFlow.value) {
            TimerState.Initial, is TimerState.Paused -> startTimer(timerState.secondsLeft)
            is TimerState.Running -> {
                cancelTimer()
                _timerStateFlow.value = TimerState.Paused(timerState.secondsLeft)
            }
        }
    }

    /**
     * Starts a new [countdownJob] lasting [initialSeconds].
     */
    private fun startTimer(initialSeconds: Int) {
        countdownJob = applicationScope.launch {
            startCountdownFlow(initialSeconds).collect { secondsLeft ->
                _timerStateFlow.value = TimerState.Running(secondsLeft)

                if (secondsLeft <= 0) {
                    saveProgressAndResetTimer()
                }
            }
        }
    }

    private fun saveProgressAndResetTimer() {
        stopTimer()
        applicationScope.launch {
            workPackageRepository.insertWorkPackage(
                WorkPackage(
                    endDate = Clock.System.now().toLocalDateTime(
                        TimeZone.currentSystemDefault()
                    ),
                    minutes = POMODORO_TIMER_INITIAL_MINUTES.toLong(),
                )
            )
        }
    }

    sealed class TimerState(val secondsLeft: Int) {
        object Initial : TimerState(POMODORO_TIMER_INITIAL_SECONDS)
        class Running(secondsLeft: Int) : TimerState(secondsLeft)
        class Paused(secondsLeft: Int) : TimerState(secondsLeft)
    }
}