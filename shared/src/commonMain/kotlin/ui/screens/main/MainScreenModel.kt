package ui.screens.main

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class MainScreenModel : StateScreenModel<MainScreenModel.MainScreenState>(MainScreenState()) {

    companion object {
        const val POMODORO_TIMER_INITIAL_SECONDS = 25 * 60
    }

    data class MainScreenState(
        val secondsLeft: Int = POMODORO_TIMER_INITIAL_SECONDS,
        val timerState: TimerState = TimerState.INITIAL,
    )

    private fun startCountdownFlow(initialSeconds: Int) = flow {
        var currentCountdownValue = initialSeconds
        emit(initialSeconds)
        while (true) {
            kotlinx.coroutines.delay(1.seconds)
            emit(--currentCountdownValue)
        }
        // Buffer values so that timer runs without hindrance.
    }.conflate()

    private var countdownJob: Job? = null

    enum class TimerState {
        INITIAL, RUNNING, PAUSED
    }

    fun onTimerButtonClick() {
        when (state.value.timerState) {
            TimerState.INITIAL, TimerState.PAUSED -> {
                mutableState.update { it.copy(timerState = TimerState.RUNNING) }
                startTimer(initialSeconds = state.value.secondsLeft)
            }

            TimerState.RUNNING -> {
                stopTimer()
                mutableState.update { it.copy(timerState = TimerState.PAUSED) }
            }
        }
    }

    /**
     * Stops the timer by canceling the [countdownJob] and setting it to null.
     */
    private fun stopTimer() {
        countdownJob?.cancel()
        countdownJob = null
    }

    /**
     * Starts a new [countdownJob] lasting [initialSeconds].
     */
    private fun startTimer(initialSeconds: Int) {
        countdownJob = coroutineScope.launch {
            startCountdownFlow(initialSeconds).collect { secondsLeft ->
                mutableState.update {
                    it.copy(secondsLeft = secondsLeft)
                }
            }
        }
    }

    fun onStopTimerClick() {
        TODO()
        // TODO: Ask with alert dialog if user really wants to stop the progress.
    }

}
