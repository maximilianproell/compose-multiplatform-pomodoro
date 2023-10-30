package com.compose.multiplatform.pomodoro.data.repository

import com.compose.multiplatform.pomodoro.storage.WorkPackageEntity
import com.compose.multiplatform.pomodoro.data.database.dao.WorkPackageDao
import com.compose.multiplatform.pomodoro.data.database.mapper.toDomain
import com.compose.multiplatform.pomodoro.data.database.mapper.toEntity
import com.compose.multiplatform.pomodoro.domain.model.WorkPackage
import com.compose.multiplatform.pomodoro.domain.repository.WorkPackageRepository
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