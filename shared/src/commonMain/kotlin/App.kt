import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import ui.screens.main.MainScreen

@Composable
fun App() {
    MaterialTheme {

        Navigator(MainScreen)
    }
}

expect fun getPlatformName(): String