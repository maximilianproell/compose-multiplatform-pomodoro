package com.compose.multiplatform.pomodoro.data.database.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.compose.multiplatform.pomodoro.storage.PomodoroDatabase
import com.compose.multiplatform.pomodoro.storage.WorkPackageEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class WorkPackageDao(private val pomodoroDatabase: PomodoroDatabase) {
    suspend fun insert(workPackageEntity: WorkPackageEntity) = withContext(Dispatchers.IO) {
        // ID of 0 is considered not set
        val idToInsert = if (workPackageEntity.id == 0L) null else workPackageEntity.id
        pomodoroDatabase.workPackageQueries.insert(
            id = idToInsert,
            endDate = workPackageEntity.endDate,
            minutes = workPackageEntity.minutes,
        )
    }

    fun observeAllWorkPackages(): Flow<List<WorkPackageEntity>> =
        pomodoroDatabase.workPackageQueries
            .getAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
}