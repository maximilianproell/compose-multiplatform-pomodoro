package data.database.mapper

import com.compose.multiplatform.pomodoro.storage.WorkPackageEntity
import domain.model.WorkPackage
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun WorkPackageEntity.toDomain(): WorkPackage = WorkPackage(
    id = id,
    startDate = Instant.parse(startDate).toLocalDateTime(TimeZone.currentSystemDefault()),
    minutes = minutes,
)

fun WorkPackage.toEntity(): WorkPackageEntity = WorkPackageEntity(
    id = id,
    startDate = startDate.toString(),
    minutes = minutes,
)
