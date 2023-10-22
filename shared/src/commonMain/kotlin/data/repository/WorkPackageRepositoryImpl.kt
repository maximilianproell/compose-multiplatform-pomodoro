package data.repository

import com.compose.multiplatform.pomodoro.storage.WorkPackageEntity
import data.database.dao.WorkPackageDao
import data.database.mapper.toDomain
import data.database.mapper.toEntity
import domain.model.WorkPackage
import domain.repository.WorkPackageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WorkPackageRepositoryImpl(
    private val workPackageDao: WorkPackageDao
) : WorkPackageRepository {
    override suspend fun insertWorkPackage(workPackage: WorkPackage) {
        workPackageDao.insert(workPackage.toEntity())
    }

    override fun observeAllWorkPackages(): Flow<List<WorkPackage>> {
        return workPackageDao.observeAllWorkPackages().map { entities ->
            entities.map(WorkPackageEntity::toDomain)
        }
    }
}