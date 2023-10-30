package com.compose.multiplatform.pomodoro.data.database.di

import com.compose.multiplatform.pomodoro.data.database.DatabaseDriverFactory
import com.compose.multiplatform.pomodoro.data.database.createDatabase
import org.koin.dsl.module

actual val databaseModule = module {
    single {
        createDatabase(DatabaseDriverFactory())
    }
}