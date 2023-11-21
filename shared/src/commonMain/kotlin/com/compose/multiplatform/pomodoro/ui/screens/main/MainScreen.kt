package com.compose.multiplatform.pomodoro.ui.screens.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.compose.multiplatform.pomodoro.MR
import com.compose.multiplatform.pomodoro.service.TimerService
import com.compose.multiplatform.pomodoro.ui.components.Timer
import com.compose.multiplatform.pomodoro.ui.screens.settings.SettingsScreen
import com.compose.multiplatform.pomodoro.ui.screens.statistics.StatisticsScreen
import com.compose.multiplatform.pomodoro.utils.createLogger
import dev.icerock.moko.resources.compose.stringResource


object MainScreen : Screen {

    private val logger = createLogger()

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val mainScreenModel = rememberScreenModel {
            MainScreenModel()
        }

        val navigator = LocalNavigator.currentOrThrow
        val screenState by mainScreenModel.state.collectAsState()

        mainScreenModel.permissionController.NotificationPermissionRequester(
            askForPermission = screenState.showNotificationPermissionRequest,
            onResult = { granted ->
                mainScreenModel.updateNotificationPermissionState(granted)
            },
            onPermissionRequestShown = mainScreenModel::onPermissionRequestShown
        )

        if (screenState.showStopTimerAlert) {
            AlertDialog(
                onDismissRequest = mainScreenModel::onDismissStopTimerAlert,
                title = {
                    Text(text = stringResource(MR.strings.pomodoro_timer_alert_stop_title))
                },
                text = {
                    Text(text = stringResource(MR.strings.pomodoro_timer_alert_stop_text))
                },
                confirmButton = {
                    TextButton(onClick = mainScreenModel::stopTimer) {
                        Text(text = stringResource(MR.strings.confirm))
                    }
                },
                dismissButton = {
                    TextButton(onClick = mainScreenModel::onDismissStopTimerAlert) {
                        Text(text = stringResource(MR.strings.cancel))
                    }
                }
            )
        }

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(stringResource(MR.strings.pomodoro_title))
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            navigator.push(SettingsScreen)
                        }) {
                            Icon(imageVector = Icons.Default.Settings, contentDescription = null)
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            navigator.push(StatisticsScreen)
                        }) {
                            Icon(imageVector = Icons.Default.BarChart, contentDescription = null)
                        }
                    }
                )
            }
        ) { scaffoldPaddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(scaffoldPaddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Timer(
                    timerSecondsLeft = screenState.timerState.secondsLeft,
                    isTimerPaused = screenState.timerState is TimerService.TimerState.Paused,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    AnimatedVisibility(
                        visible = screenState.timerState is TimerService.TimerState.Paused,
                        enter = scaleIn() + expandHorizontally(),
                        exit = scaleOut() + shrinkHorizontally(),
                    ) {
                        Row {
                            OutlinedButton(
                                onClick = mainScreenModel::onStopTimerClick
                            ) {
                                Text(text = stringResource(MR.strings.timer_stop))
                            }

                            Spacer(modifier = Modifier.size(16.dp))
                        }
                    }

                    Button(
                        onClick = {
                            val timerInInitialState = screenState.timerState is TimerService.TimerState.Initial
                            if (!screenState.hasPermissionForNotifications && timerInInitialState) {
                                mainScreenModel.showNotificationPermissionRequest()
                            }
                            mainScreenModel.onTimerButtonClick()
                        }
                    ) {
                        val buttonTextId = when (screenState.timerState) {
                            is TimerService.TimerState.Initial -> MR.strings.timer_start
                            is TimerService.TimerState.Running -> MR.strings.timer_pause
                            is TimerService.TimerState.Paused -> MR.strings.timer_continue
                        }
                        AnimatedContent(targetState = buttonTextId) {
                            Text(text = stringResource(it))
                        }
                    }
                }
            }
        }
    }
}
