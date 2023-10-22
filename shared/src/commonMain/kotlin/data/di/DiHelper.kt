package data.di

import data.database.di.databaseModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import service.di.serviceModule

/**
 * Start Koin for this App.
 */
fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(databaseModule, dataModule, serviceModule)
}