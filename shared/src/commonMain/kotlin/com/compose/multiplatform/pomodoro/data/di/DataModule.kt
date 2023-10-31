package com.compose.multiplatform.pomodoro.data.di

import com.compose.multiplatform.pomodoro.data.database.dao.WorkPackageDao
import com.compose.multiplatform.pomodoro.data.database.di.databaseModule
import com.compose.multiplatform.pomodoro.data.repository.SettingsRepositoryImpl
import com.compose.multiplatform.pomodoro.data.repository.WorkPackageRepositoryImpl
import com.compose.multiplatform.pomodoro.data.settings.settingsModule
import com.compose.multiplatform.pomodoro.domain.repository.SettingsRepository
import com.compose.multiplatform.pomodoro.domain.repository.WorkPackageRepository
import org.koin.dsl.module

val dataModule = module {
    includes(databaseModule, settingsModule)
    single { WorkPackageDao(get()) }
    single<WorkPackageRepository> { WorkPackageRepositoryImpl(get()) }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }
}