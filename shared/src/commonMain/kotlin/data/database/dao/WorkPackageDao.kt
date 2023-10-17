package data.database.dao

import com.compose.multiplatform.pomodoro.storage.PomodoroDatabase
import data.database.entity.WorkPackageEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class WorkPackageDao(private val pomodoroDatabase: PomodoroDatabase) {
    suspend fun insert(workPackageEntity: WorkPackageEntity) = withContext(Dispatchers.IO) {
        pomodoroDatabase.workPackageQueries.insert(
            id = null,
            // TODO: create converter
            startDate = "YYYY-MM-DD HH:MM:SS.SSS",
            minutes = workPackageEntity.minutes,
        )
    }
}