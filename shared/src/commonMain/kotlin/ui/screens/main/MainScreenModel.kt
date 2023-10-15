package ui.screens.main

import cafe.adriel.voyager.core.model.StateScreenModel
import kotlinx.coroutines.flow.update

class MainScreenModel : StateScreenModel<MainScreenModel.MainScreenState>(MainScreenState()) {

    companion object {
        const val POMODORO_TIMER_INITIAL_SECONDS = 25 * 60
    }

    data class MainScreenState(
        val secondsLeft: Int = POMODORO_TIMER_INITIAL_SECONDS,
        val timerState: TimerState = TimerState.INITIAL,
    )

    enum class TimerState {
        INITIAL, RUNNING, PAUSED
    }

    fun onTimerButtonClick() {
        when (state.value.timerState) {
            TimerState.INITIAL, TimerState.PAUSED -> mutableState.update { it.copy(timerState = TimerState.RUNNING) }
            TimerState.RUNNING -> mutableState.update { it.copy(timerState = TimerState.PAUSED) }
        }
    }

    fun onStopTimerClick() {
        TODO()
    }

}
