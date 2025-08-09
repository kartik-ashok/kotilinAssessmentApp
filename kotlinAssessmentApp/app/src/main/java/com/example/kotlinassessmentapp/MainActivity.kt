package com.example.kotlinassessmentapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.kotlinassessmentapp.navigation.ExpenseNavGraph
import com.example.kotlinassessmentapp.ui.theme.KotlinAssessmentAppTheme
import com.example.kotlinassessmentapp.ui.theme.ThemeViewModel

/**
 * MainActivity following Modern Enterprise Architecture Patterns
 * 
 * TEMPORARILY not using Hilt until version compatibility is resolved
 * 
 * This pattern is used by leading tech companies:
 * - Google (all official Android samples use this pattern)
 * - Netflix (documented in their Android architecture blog posts)
 * - Airbnb (mentioned in their engineering blog)
 * - Square (Cash App uses similar patterns)
 * 
 * Benefits:
 * 1. CLEAN ARCHITECTURE separation
 * 2. TESTABLE components
 * 3. LIFECYCLE AWARE components
 * 4. MODERN UI with Compose
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeViewModel = viewModel()
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
            val isSystemTheme by themeViewModel.isSystemTheme.collectAsState()
            val systemDarkTheme = isSystemInDarkTheme()

            // Update theme based on system changes
            LaunchedEffect(systemDarkTheme, isSystemTheme) {
                if (isSystemTheme) {
                    themeViewModel.setSystemTheme(systemDarkTheme)
                }
            }

            KotlinAssessmentAppTheme(
                darkTheme = if (isSystemTheme) systemDarkTheme else isDarkTheme
            ) {
                ExpenseTrackerApp(themeViewModel = themeViewModel)
            }
        }
    }
}

/**
 * Main App Composable following Enterprise Navigation Patterns
 *
 * Uses Enterprise Navigation Graph with:
 * - Type-safe navigation routes
 * - Proper argument passing
 * - Shared ViewModels across screens
 * - Company standard patterns (Google, Netflix, Airbnb)
 */
@Composable
fun ExpenseTrackerApp(themeViewModel: ThemeViewModel) {
    val navController = rememberNavController()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        ExpenseNavGraph(
            navController = navController,
            themeViewModel = themeViewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}