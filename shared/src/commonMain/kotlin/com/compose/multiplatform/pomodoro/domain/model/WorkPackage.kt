package com.compose.multiplatform.pomodoro.domain.model

import kotlinx.datetime.LocalDateTime

data class WorkPackage(
    val id: Long = 0,
    val endDate: LocalDateTime,
    val minutes: Long,
)