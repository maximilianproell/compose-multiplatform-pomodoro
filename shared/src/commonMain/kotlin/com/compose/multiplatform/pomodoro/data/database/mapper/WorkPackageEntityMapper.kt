package com.compose.multiplatform.pomodoro.data.database.mapper

import com.compose.multiplatform.pomodoro.storage.WorkPackageEntity
import com.compose.multiplatform.pomodoro.domain.model.WorkPackage
import kotlinx.datetime.toLocalDateTime

fun WorkPackageEntity.toDomain(): WorkPackage = WorkPackage(
    id = id,
    endDate = endDate.toLocalDateTime(),
    minutes = minutes,
)

fun WorkPackage.toEntity(): WorkPackageEntity = WorkPackageEntity(
    id = id,
    endDate = endDate.toString(),
    minutes = minutes,
)
