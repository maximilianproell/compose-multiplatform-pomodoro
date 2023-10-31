package com.compose.multiplatform.pomodoro.service.di

import org.koin.dsl.module
import com.compose.multiplatform.pomodoro.service.NotificationService
import com.compose.multiplatform.pomodoro.service.TimerService

val serviceModule = module {
    single { TimerService(get(), get()) }
    factory { NotificationService(get()) }
}