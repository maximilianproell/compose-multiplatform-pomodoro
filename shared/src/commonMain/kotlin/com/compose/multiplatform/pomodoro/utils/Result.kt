package com.compose.multiplatform.pomodoro.utils

import kotlin.reflect.KProperty

sealed interface Result<out T> {
    data object Loading: Result<Nothing>

    class Success<T>(val data: T): Result<T> {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
            return data
        }
    }
}