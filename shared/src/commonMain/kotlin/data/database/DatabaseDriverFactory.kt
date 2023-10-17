package data.database

import app.cash.sqldelight.db.SqlDriver
import com.compose.multiplatform.pomodoro.storage.PomodoroDatabase

expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DatabaseDriverFactory): PomodoroDatabase {
    val driver = driverFactory.createDriver()
    return PomodoroDatabase(driver)
}