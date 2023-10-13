package ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.compose.multiplatform.pomodoro.MR
import dev.icerock.moko.resources.compose.stringResource
import ui.components.TimerAndReset


object MainScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val scaffoldState = rememberBottomSheetScaffoldState()
        val coroutineScope = rememberCoroutineScope()

        val timerOpacityAnimation by rememberInfiniteTransition(label = "infinite").animateFloat(
            initialValue = 1f,
            // TODO: set animation when in pause mode
            targetValue = .4f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ), label = "timerOpacityAnimation"
        )

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
                Text(text = stringResource(MR.strings.my_string))
                TimerAndReset(
                    timerOpacityAnimation = timerOpacityAnimation,
                    // TODO
                    isResetVisible = false,
                    onResetIconClick = {
                        // TODO
                    },
                    // TODO
                    timerSecondsLeft = 300
                )
            }
        }
    }
}

@Composable
fun TimerText(text: String, modifier: Modifier) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.displayMedium
    )
}

// TODO: Use resources for strings
@Composable
fun PomodoroMiniFabs(
    expandedState: Boolean,
    onStopClick: () -> Unit,
    onPauseClick: () -> Unit,
) {
    AnimatedVisibility(
        modifier = Modifier
            .wrapContentSize()
            .layoutId("mini_fabs"),
        visible = expandedState,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut()
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            FloatingActionButton(
                modifier = Modifier.size(56.dp),
                onClick = {
                    onPauseClick()
                }
            ) {
                Icon(
                    Icons.Filled.Pause,
                    contentDescription = "Pause Pomodoro Timer",
                )
            }

            FloatingActionButton(
                onClick = {
                    onStopClick()
                }
            ) {
                Icon(
                    Icons.Filled.Stop,
                    contentDescription = "Stop Pomodoro Timer",
                )
            }
        }
    }
}