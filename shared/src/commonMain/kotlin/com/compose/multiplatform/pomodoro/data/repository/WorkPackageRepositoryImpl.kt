package com.compose.multiplatform.pomodoro.data.repository

import com.compose.multiplatform.pomodoro.data.database.dao.WorkPackageDao
import com.compose.multiplatform.pomodoro.data.database.mapper.toDomain
import com.compose.multiplatform.pomodoro.data.database.mapper.toEntity
import com.compose.multiplatform.pomodoro.domain.model.WorkPackage
import com.compose.multiplatform.pomodoro.domain.repository.WorkPackageRepository
import com.compose.multiplatform.pomodoro.storage.WorkPackageEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate

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

    override suspend fun getWorkPackagesInDataRange(fromDate: LocalDate, toDate: LocalDate): List<WorkPackage> {
        return workPackageDao
            .getInRange(fromDate.toString(), toDate.toString())
            .map(WorkPackageEntity::toDomain)
    }
}