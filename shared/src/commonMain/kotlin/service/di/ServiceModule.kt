package service.di

import org.koin.dsl.module
import service.NotificationService
import service.TimerService

val serviceModule = module {
    single { TimerService(get()) }
    factory { NotificationService(get()) }
}