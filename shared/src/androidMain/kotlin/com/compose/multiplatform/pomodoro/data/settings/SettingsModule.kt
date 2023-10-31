package com.compose.multiplatform.pomodoro.data.settings

import com.russhwolf.settings.SharedPreferencesSettings
import com.russhwolf.settings.coroutines.SuspendSettings
import com.russhwolf.settings.coroutines.toSuspendSettings
import org.koin.dsl.module

actual val settingsModule = module {
    factory<SuspendSettings> {
        SharedPreferencesSettings.Factory(get()).create().toSuspendSettings()
    }
}
