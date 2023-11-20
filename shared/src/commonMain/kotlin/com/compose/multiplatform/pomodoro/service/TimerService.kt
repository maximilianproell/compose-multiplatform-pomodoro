package com.compose.multiplatform.pomodoro.service

import com.compose.multiplatform.pomodoro.domain.model.WorkPackage
import com.compose.multiplatform.pomodoro.domain.repository.SettingsRepository
import com.compose.multiplatform.pomodoro.domain.repository.WorkPackageRepository
import com.compose.multiplatform.pomodoro.utils.createLogger
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
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.seconds

/**
 * This class handles the entire timer logic.
 */
class TimerService(
    private val workPackageRepository: WorkPackageRepository,
    private val settingsRepository: SettingsRepository,
) {

    companion object {
        const val POMODORO_TIMER_DEFAULT_MINUTES = 25
    }

    /**
     * The job responsible for counting down every second.
     */
    private var countdownJob: Job? = null

    private val logger = createLogger()

    // Normally, that would be injected for better testability
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _timerStateFlow: MutableStateFlow<TimerState> = MutableStateFlow(
        TimerState.Initial.withDefaultDuration
    )
    val timerStateFlow = _timerStateFlow.asStateFlow()

    init {
        logger.d { "Initial startup of TimerService" }
        applicationScope.launch {
            launch {
                logger.i { "Starting with set time duration observation" }
                settingsRepository.observeTimerDurationMinutes().collect { currentlySetMinutes ->
                    if (timerStateFlow.value is TimerState.Initial) {
                        // Only when timer is still in the initial state we update the duration coming from the repo.
                        _timerStateFlow.value = TimerState.Initial(currentlySetMinutes * 60)
                    }
                }
            }

            logger.d { "Checking if timer should still be running." }

            // Check if timer should still be running. If yes, then start the timer.
            val finishTimerTimestamp = settingsRepository.getTimerFinishTimestamp()
            if (finishTimerTimestamp != null) {
                val currentInstant = Clock.System.now()
                val finishTimerInstant = Instant.fromEpochMilliseconds(finishTimerTimestamp)
                if (currentInstant < finishTimerInstant) {
                    // Timer finishes in the future, we therefore start the timer.
                    logger.d { "Timer should still be running. Starting timer..." }

                    // Calculate how long the timer should still run.
                    val initialSecondsInstant = finishTimerInstant - currentInstant
                    startTimer(initialSecondsInstant.inWholeSeconds.toInt())
                } else {
                    logger.d { "Timer should have finished in the past, but app was probably killed by the system. Saving progress..." }
                    settingsRepository.removeTimerFinishTimestamp()
                    saveProgressAndResetTimer()
                }
            }
        }
    }

    /**
     * Stops the timer and discards any progress.
     */
    fun stopTimer() {
        cancelTimer()
        applicationScope.launch {
            val initialSeconds = settingsRepository.getTimerDurationMinutes() * 60
            _timerStateFlow.value = TimerState.Initial(initialSeconds)
        }
    }

    /**
     * Cancels the [countdownJob] and sets it to `null`
     */
    private fun cancelTimer() {
        applicationScope.launch {
            settingsRepository.removeTimerFinishTimestamp()
        }

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
            is TimerState.Initial, is TimerState.Paused -> startTimer(timerState.secondsLeft)
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
            settingsRepository.saveTimerFinishTimestamp(
                Clock.System.now().toEpochMilliseconds() + initialSeconds.seconds.inWholeMilliseconds
            )

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
                    minutes = settingsRepository.getTimerDurationMinutes().toLong(),
                )
            )
        }
    }

    sealed class TimerState(val secondsLeft: Int) {
        class Initial(secondsLeft: Int) : TimerState(secondsLeft) {
            companion object {
                val withDefaultDuration: TimerState
                    get() = Initial(POMODORO_TIMER_DEFAULT_MINUTES)
            }
        }

        class Running(secondsLeft: Int) : TimerState(secondsLeft)
        class Paused(secondsLeft: Int) : TimerState(secondsLeft)
    }
}