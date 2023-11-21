package com.compose.multiplatform.pomodoro.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.sp
import com.compose.multiplatform.pomodoro.domain.usecase.FormatSecondsForTimerUseCase

@Composable
fun Timer(
    timerSecondsLeft: Int,
    isTimerPaused: Boolean,
) {
    val timerOpacityAnimation by rememberInfiniteTransition(label = "infinite").animateFloat(
        initialValue = 1f,
        targetValue = if (isTimerPaused) .4f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "timerOpacityAnimation"
    )

    Row(
        modifier = Modifier
            .wrapContentSize()
            .graphicsLayer {
                alpha = timerOpacityAnimation
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        TimerText(
            text = FormatSecondsForTimerUseCase(timerSecondsLeft),
            modifier = Modifier
        )
    }
}

@Composable
private fun TimerText(text: String, modifier: Modifier) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = 74.sp
    )
}