package ui.screens.main

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import co.touchlab.kermit.Logger
import domain.model.WorkPackage
import domain.repository.WorkPackageRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import kotlin.time.Duration.Companion.seconds

class MainScreenModel : StateScreenModel<MainScreenModel.MainScreenState>(MainScreenState()), KoinComponent {

    private val logger = Logger.withTag(this::class.simpleName ?: "")
    private val workPackageRepository: WorkPackageRepository = get()

    companion object {
        const val POMODORO_TIMER_INITIAL_SECONDS = 5 // TODO: change back, just for testing
    }

    data class MainScreenState(
        val secondsLeft: Int = POMODORO_TIMER_INITIAL_SECONDS,
        val timerState: TimerState = TimerState.INITIAL,
    )

    private fun startCountdownFlow(initialSeconds: Int) = flow {
        val startTimestampInSeconds = Clock.System.now().epochSeconds

        // Immediately emit the initial seconds left.
        emit(initialSeconds)
        while (true) {
            kotlinx.coroutines.delay(1.seconds)
            val currentEpochTimestamp = Clock.System.now().epochSeconds

            // Subtract seconds passed since timer was started.
            val secondsPassed = (currentEpochTimestamp - startTimestampInSeconds).toInt()
            emit(initialSeconds - secondsPassed)
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

                if (secondsLeft <= 0) {
                    saveProgressAndResetTimer()
                    stopTimer()
                }
            }
        }
    }

    private fun saveProgressAndResetTimer() {
        coroutineScope.launch {
            workPackageRepository.insertWorkPackage(
                WorkPackage(
                    // TODO: set correct start time
                    startDate = Clock.System.now().toLocalDateTime(
                        TimeZone.currentSystemDefault()
                    ),
                    minutes = 5, // TODO: set correct time worked
                )
            )

            mutableState.update {
                it.copy(
                    secondsLeft = POMODORO_TIMER_INITIAL_SECONDS,
                    timerState = TimerState.INITIAL
                )
            }
        }
    }

    fun onStopTimerClick() {
        TODO()
        // TODO: Ask with alert dialog if user really wants to stop the progress.
    }

}
