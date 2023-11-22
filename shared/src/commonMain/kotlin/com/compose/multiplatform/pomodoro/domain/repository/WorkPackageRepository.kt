package com.compose.multiplatform.pomodoro.domain.repository

import com.compose.multiplatform.pomodoro.domain.model.WorkPackage
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface WorkPackageRepository {
    suspend fun insertWorkPackage(workPackage: WorkPackage)

    fun observeAllWorkPackages(): Flow<List<WorkPackage>>

    suspend fun getWorkPackagesInDataRange(fromDate: LocalDate, toDate: LocalDate): List<WorkPackage>
}