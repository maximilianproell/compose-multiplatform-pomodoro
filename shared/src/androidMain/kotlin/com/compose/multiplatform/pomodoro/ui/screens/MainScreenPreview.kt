package com.compose.multiplatform.pomodoro.ui.screens

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.compose.multiplatform.pomodoro.ui.screens.main.MainScreen

@Composable
@Preview
fun MainScreenPreview() {
    MaterialTheme {
        MainScreen.Content()
    }
}