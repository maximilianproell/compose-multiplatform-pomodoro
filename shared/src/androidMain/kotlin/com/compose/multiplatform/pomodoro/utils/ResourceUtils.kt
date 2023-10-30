package com.compose.multiplatform.pomodoro.utils

import android.content.Context
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.ResourceFormatted
import dev.icerock.moko.resources.desc.StringDesc

fun Context.getString(stringResource: StringResource) = StringDesc.Resource(stringResource).toString(this)
fun Context.getString(
    stringResource: StringResource,
    vararg args: String
) = StringDesc.ResourceFormatted(stringResource, *args).toString(this)