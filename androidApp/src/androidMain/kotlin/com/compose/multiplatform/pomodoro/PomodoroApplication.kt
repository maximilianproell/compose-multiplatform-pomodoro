package com.compose.multiplatform.pomodoro

import android.app.Application
import data.di.initKoin
import org.koin.android.ext.koin.androidContext

class PomodoroApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@PomodoroApplication)
        }
    }
}