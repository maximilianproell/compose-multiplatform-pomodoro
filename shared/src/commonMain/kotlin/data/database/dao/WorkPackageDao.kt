package data.database.dao

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
        pomodoroDatabase.workPackageQueries.insert(
            id = workPackageEntity.id,
            startDate = workPackageEntity.startDate,
            minutes = workPackageEntity.minutes,
        )
    }

    suspend fun observeAllWorkPackages(): Flow<List<WorkPackageEntity>> =
        pomodoroDatabase.workPackageQueries
            .getAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
}