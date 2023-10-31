package com.compose.multiplatform.pomodoro.utils

import co.touchlab.kermit.Logger

/**
 * Creates a [Logger] instance for this class where the logger tag is the name of this class. If this class is
 * an anonymous object, the logger tag is set to "anonymous".
 */
fun Any.createLogger(): Logger = Logger.withTag(this::class.simpleName ?: "anonymous")