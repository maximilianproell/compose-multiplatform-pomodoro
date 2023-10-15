package ui.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.compose.multiplatform.pomodoro.MR
import dev.icerock.moko.resources.compose.stringResource
import ui.components.Timer


object MainScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val mainScreenModel = rememberScreenModel {
            MainScreenModel()
        }

        val screenState by mainScreenModel.state.collectAsState()

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text("Pomodoro")
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
                    timerSecondsLeft = 300,
                    isTimerPaused = screenState.timerState == MainScreenModel.TimerState.PAUSED,
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AnimatedVisibility(
                        visible = screenState.timerState == MainScreenModel.TimerState.PAUSED,
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
                            MainScreenModel.TimerState.INITIAL -> MR.strings.timer_start
                            MainScreenModel.TimerState.RUNNING -> MR.strings.timer_pause
                            MainScreenModel.TimerState.PAUSED -> MR.strings.timer_continue
                        }
                        Text(text = stringResource(buttonTextId))
                    }
                }
            }
        }
    }
}
