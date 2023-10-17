package ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.sp

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
            // TODO: optimize to prevent recompositions
            .alpha(timerOpacityAnimation),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val minutes = (timerSecondsLeft / 60).toString().padStart(length = 2, padChar = '0')
        val seconds = (timerSecondsLeft % 60).toString().padStart(length = 2, padChar = '0')

        TimerText(
            text = "$minutes:$seconds",
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