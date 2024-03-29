package com.compose.multiplatform.pomodoro.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.compose.multiplatform.pomodoro.MR
import com.compose.multiplatform.pomodoro.ui.components.BackIcon
import dev.icerock.moko.resources.compose.stringResource

object SettingsScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val screenModel = rememberScreenModel {
            SettingsScreenModel()
        }

        val screenState by screenModel.state.collectAsState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(stringResource(MR.strings.settings_title))
                    },
                    navigationIcon = {
                        IconButton(onClick = navigator::pop) {
                            BackIcon()
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(contentAlignment = Alignment.Center) {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(16.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = stringResource(MR.strings.settings_pomodoro_minutes_label),
                            style = MaterialTheme.typography.bodyLarge
                        )

                        val keyboardController = LocalSoftwareKeyboardController.current
                        val minutesText = screenState.pomodoroTimerMinutes?.toString() ?: ""

                        OutlinedTextField(
                            modifier = Modifier.width(70.dp),
                            value = minutesText,
                            onValueChange = {
                                if (it.length <= 3 && it.all(Char::isDigit)) {
                                    screenModel.updateTimerMinutes(it.toIntOrNull())
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                autoCorrect = false,
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(onDone = {
                                keyboardController?.hide()
                            }),
                            singleLine = true,
                            enabled = !screenState.inputBlocked
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = stringResource(MR.strings.settings_keep_screen_on),
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Switch(
                            checked = screenState.keepScreenOn,
                            onCheckedChange = { isChecked ->
                                screenModel.updateKeepScreenOnSetting(isChecked)
                            }
                        )
                    }
                }
                if (screenState.isLoading) {
                    CircularProgressIndicator()
                }
            }
        }

    }
}