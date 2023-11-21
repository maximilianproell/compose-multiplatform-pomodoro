package com.compose.multiplatform.pomodoro.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.compose.multiplatform.pomodoro.isIos

@Composable
fun BackIcon() {
    if (isIos()) {
        Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = null)
    } else {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
    }
}