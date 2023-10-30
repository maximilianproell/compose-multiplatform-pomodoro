package com.compose.multiplatform.pomodoro.data.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.compose.multiplatform.pomodoro.storage.PomodoroDatabase

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(PomodoroDatabase.Schema, context, "PomodoroDatabase.db")
    }
}