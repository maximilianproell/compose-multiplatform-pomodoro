package com.compose.multiplatform.pomodoro.data.di

import com.compose.multiplatform.pomodoro.data.database.di.databaseModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import com.compose.multiplatform.pomodoro.service.di.serviceModule

/**
 * Start Koin for this App.
 */
fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(dataModule, serviceModule)
}