package domain.repository

import data.database.entity.WorkPackageEntity

interface WorkPackageRepository {
    suspend fun insertWorkPackage(workPackageEntity: WorkPackageEntity)
}