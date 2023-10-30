package com.compose.multiplatform.pomodoro.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.compose.multiplatform.pomodoro.storage.PomodoroDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(PomodoroDatabase.Schema, "PomodoroDatabase.db")
    }
}