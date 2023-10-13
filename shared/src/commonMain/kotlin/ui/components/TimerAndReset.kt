package ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import ui.screens.TimerText

@Composable
fun TimerAndReset(
    timerOpacityAnimation: Float,
    isResetVisible: Boolean,
    onResetIconClick: () -> Unit,
    timerSecondsLeft: Long
) {
    Row(
        modifier = Modifier
            .wrapContentSize()
            .alpha(timerOpacityAnimation),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TimerText(
            // TODO add koltinx.datetime to make that work
            text = timerSecondsLeft.toString(),
            modifier = Modifier
        )

        ResetIcon(
            modifier = Modifier.size(56.dp),
            isVisible = isResetVisible,
            onResetIconClick = onResetIconClick
        )
    }
}

@Composable
fun ResetIcon(
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
    onResetIconClick: () -> Unit
) {
    AnimatedVisibility(visible = isVisible) {
        IconButton(
            onClick = { onResetIconClick() },
            modifier = modifier
        ) {
            Icon(
                modifier = modifier,
                imageVector = Icons.Filled.Replay,
                contentDescription = "Reset timer"
            )
        }
    }
}