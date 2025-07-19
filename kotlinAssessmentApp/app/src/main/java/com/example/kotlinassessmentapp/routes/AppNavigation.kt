import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kotlinassessmentapp.screens.ProfileScreen
import com.example.kotlinassessmentapp.screens.Setting
import com.example.kotlinassessmentapp.screens.SplashScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("welcome") {
            ProfileScreen()
        }
        composable("home") {
            HomeScreen()
        }
        composable("settings") {
            Setting() // <-- Add this screen
        }
    }
}
