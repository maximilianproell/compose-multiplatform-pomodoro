package data.di

import data.database.dao.WorkPackageDao
import data.repository.WorkPackageRepositoryImpl
import domain.repository.WorkPackageRepository
import org.koin.dsl.module

val dataModule = module {
    single { WorkPackageDao(get()) }
    single<WorkPackageRepository> { WorkPackageRepositoryImpl(get()) }
}