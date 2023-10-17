package domain.repository

import domain.model.WorkPackage
import kotlinx.coroutines.flow.Flow

interface WorkPackageRepository {
    suspend fun insertWorkPackage(workPackage: WorkPackage)

    suspend fun observeAllWorkPackages(): Flow<List<WorkPackage>>
}