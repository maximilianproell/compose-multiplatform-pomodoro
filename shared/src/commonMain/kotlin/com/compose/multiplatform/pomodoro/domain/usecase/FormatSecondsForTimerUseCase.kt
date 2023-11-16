package com.compose.multiplatform.pomodoro.domain.usecase

object FormatSecondsForTimerUseCase {
    operator fun invoke(seconds: Int): String {
        val minutesText = (seconds / 60).toString().padStart(length = 2, padChar = '0')
        val secondsText = (seconds % 60).toString().padStart(length = 2, padChar = '0')

        return "$minutesText:$secondsText"
    }
}