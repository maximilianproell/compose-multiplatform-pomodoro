package com.compose.multiplatform.pomodoro.data.settings

import com.russhwolf.settings.SharedPreferencesSettings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import org.koin.dsl.module

actual val settingsModule = module {
    factory<FlowSettings> {
        SharedPreferencesSettings.Factory(get()).create().toFlowSettings()
    }
}
