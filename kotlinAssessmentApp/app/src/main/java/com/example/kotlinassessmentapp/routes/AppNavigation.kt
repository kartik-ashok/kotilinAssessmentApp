import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kotlinassessmentapp.screens.BookNow
import com.example.kotlinassessmentapp.screens.ProfileScreen
import com.example.kotlinassessmentapp.screens.Setting
import com.example.kotlinassessmentapp.screens.SplashScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable(
            "splash",


            ) {
            SplashScreen(navController)
        }

        composable(
            "home", enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {

            HomeScreen(navController);

        }

        composable(
            "bookNow",
            enterTransition = { slideInHorizontally() },
            exitTransition = { slideOutHorizontally() }
        ) {
            BookNow(navController)
        }

    }
}
