package data.database.di

import data.database.DatabaseDriverFactory
import data.database.createDatabase
import org.koin.dsl.module

actual val databaseModule = module {
    single {
        createDatabase(DatabaseDriverFactory())
    }
}