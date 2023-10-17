package domain.model

import kotlinx.datetime.LocalDateTime

data class WorkPackage(
    val id: Long = 0,
    val startDate: LocalDateTime,
    val minutes: Long,
)