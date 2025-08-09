package com.example.kotlinassessmentapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kotlinassessmentapp.ui.screens.AddExpenseScreen
import com.example.kotlinassessmentapp.ui.screens.HomeScreen
import com.example.kotlinassessmentapp.ui.theme.KotlinAssessmentAppTheme
import com.example.kotlinassessmentapp.ui.viewmodel.ExpenseViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinAssessmentAppTheme {
                ExpenseTrackerApp()
            }
        }
    }
}

@Composable
fun ExpenseTrackerApp() {
    val navController = rememberNavController()
    val expenseViewModel: ExpenseViewModel = viewModel()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                HomeScreen(
                    onAddExpenseClick = {
                        navController.navigate("add_expense")
                    },
                    expenseViewModel = expenseViewModel
                )
            }
            
            composable("add_expense") {
                AddExpenseScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onExpenseAdded = {
                        navController.popBackStack()
                    },
                    expenseViewModel = expenseViewModel
                )
            }
        }
    }
} 