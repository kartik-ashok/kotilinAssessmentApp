package com.example.kotlinassessmentapp

//import AppNavigation
import AppNavigation
import HomeScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.kotlinassessmentapp.bottom_nav.MainScreen
import com.example.kotlinassessmentapp.screens.BookNow
import com.example.kotlinassessmentapp.screens.SplashScreen
import com.example.kotlinassessmentapp.ui.theme.KotlinAssessmentAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            KotlinAssessmentAppTheme {
//                AppNavigation()

//                SplashScreen()
//                HomeScreen()

//                MainScreen()

//                BookNow()

                AppNavigation()
            }
        }
    }
}

