package data.database.entity

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

data class WorkPackageEntity(
    val id: Long = 0,
    val startDate: LocalDateTime,
    val minutes: Long,
)
