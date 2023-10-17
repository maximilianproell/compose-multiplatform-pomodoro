package data.repository

import data.database.dao.WorkPackageDao
import data.database.entity.WorkPackageEntity
import domain.repository.WorkPackageRepository

class WorkPackageRepositoryImpl(
    private val workPackageDao: WorkPackageDao
) : WorkPackageRepository {
    override suspend fun insertWorkPackage(workPackageEntity: WorkPackageEntity) {
        workPackageDao.insert(workPackageEntity)
    }
}