import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import ui.screens.main.MainScreen
import ui.theme.PomodoroAppTheme

@Composable
fun App() {
    PomodoroAppTheme {
        Navigator(MainScreen)
    }
}

expect fun getPlatformName(): String