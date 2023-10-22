package ui.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import dev.icerock.moko.resources.compose.stringResource
import service.TimerService
import ui.components.Timer
import ui.screens.settings.SettingsScreen
import ui.screens.statistics.StatisticsScreen


object MainScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val mainScreenModel = rememberScreenModel {
            MainScreenModel()
        }

        val navigator = LocalNavigator.currentOrThrow
        val screenState by mainScreenModel.state.collectAsState()

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
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Timer(
                    timerSecondsLeft = screenState.timerState.secondsLeft,
                    isTimerPaused = screenState.timerState is TimerService.TimerState.Paused,
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AnimatedVisibility(
                        visible = screenState.timerState is TimerService.TimerState.Paused,
                    ) {
                        OutlinedButton(
                            onClick = mainScreenModel::onStopTimerClick
                        ) {
                            Text(text = stringResource(MR.strings.timer_stop))
                        }
                    }

                    Button(
                        onClick = mainScreenModel::onTimerButtonClick
                    ) {
                        val buttonTextId = when (screenState.timerState) {
                            TimerService.TimerState.Initial -> MR.strings.timer_start
                            is TimerService.TimerState.Running -> MR.strings.timer_pause
                            is TimerService.TimerState.Paused -> MR.strings.timer_continue
                        }
                        Text(text = stringResource(buttonTextId))
                    }
                }
            }
        }
    }
}
