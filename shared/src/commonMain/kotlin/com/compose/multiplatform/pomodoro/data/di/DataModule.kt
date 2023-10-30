package com.compose.multiplatform.pomodoro.data.di

import com.compose.multiplatform.pomodoro.data.database.dao.WorkPackageDao
import com.compose.multiplatform.pomodoro.data.repository.WorkPackageRepositoryImpl
import com.compose.multiplatform.pomodoro.domain.repository.WorkPackageRepository
import org.koin.dsl.module

val dataModule = module {
    single { WorkPackageDao(get()) }
    single<WorkPackageRepository> { WorkPackageRepositoryImpl(get()) }
}