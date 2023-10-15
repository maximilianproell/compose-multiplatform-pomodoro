package ui.screens.main

import cafe.adriel.voyager.core.model.StateScreenModel

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


}
