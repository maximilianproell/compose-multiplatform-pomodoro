package data.database.di

import data.database.DatabaseDriverFactory
import data.database.createDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val databaseModule = module {
    single {
        val databaseDriverFactory = DatabaseDriverFactory(androidContext())
        createDatabase(databaseDriverFactory)
    }
}