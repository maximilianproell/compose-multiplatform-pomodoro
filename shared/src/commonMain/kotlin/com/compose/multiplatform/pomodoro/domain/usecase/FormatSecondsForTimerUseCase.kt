package com.compose.multiplatform.pomodoro.domain.usecase

object FormatSecondsForTimerUseCase {
    operator fun invoke(seconds: Int): String {
        val minutes = (seconds / 60).toString().padStart(length = 2, padChar = '0')
        val seconds = (seconds % 60).toString().padStart(length = 2, padChar = '0')

        return "$minutes:$seconds"
    }
}