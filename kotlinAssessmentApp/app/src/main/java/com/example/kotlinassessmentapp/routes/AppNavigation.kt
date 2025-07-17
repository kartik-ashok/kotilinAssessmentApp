import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kotlinassessmentapp.screens.HomeScreen
import com.example.kotlinassessmentapp.screens.SplashScreen
import com.example.kotlinassessmentapp.screens.WelcomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("welcome") {
            WelcomeScreen(navController)
        }
        composable("home") {
            HomeScreen()
        }
    }
}
