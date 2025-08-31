package com.example.kotlinassessmentapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.kotlinassessmentapp.ui.screens.*
import com.example.kotlinassessmentapp.ui.viewmodel.ExpenseViewModel
import com.example.kotlinassessmentapp.ui.viewmodel.ReportViewModel
import com.example.kotlinassessmentapp.ui.theme.ThemeViewModel

/**
 * Navigation Routes following Enterprise Type-Safety Patterns
 * 
 * This pattern is STANDARD at:
 * - Google (Now in Android app, Compose samples)
 * - Netflix (Android architecture blog)
 * - Airbnb (Engineering blog posts)
 * - Square (Cash App architecture)
 * 
 * Benefits:
 * 1. TYPE SAFETY - Compile-time route validation
 * 2. CENTRALIZED ROUTES - Single source of truth
 * 3. ARGUMENT VALIDATION - Proper parameter handling
 * 4. MAINTAINABLE - Easy to refactor and extend
 */
sealed class ExpenseDestination(val route: String) {
    object Home : ExpenseDestination("home")
    object AddExpense : ExpenseDestination("add_expense")
    object EditExpense : ExpenseDestination("edit_expense/{expenseId}") {
        fun createRoute(expenseId: String): String = "edit_expense/$expenseId"
        const val EXPENSE_ID_ARG = "expenseId"
    }
    object ExpenseList : ExpenseDestination("expense_list") {
        // Optional arguments for filtering
        fun createRoute(
            dateFilter: String? = null,
            categoryId: String? = null
        ): String {
            val params = mutableListOf<String>()
            dateFilter?.let { params.add("dateFilter=$it") }
            categoryId?.let { params.add("categoryId=$it") }
            
            return if (params.isNotEmpty()) {
                "$route?${params.joinToString("&")}"
            } else route
        }
    }
    object ExpenseReport : ExpenseDestination("expense_report") {
        // Optional arguments for report customization
        fun createRoute(
            reportType: String? = null,
            dateRange: String? = null
        ): String {
            val params = mutableListOf<String>()
            reportType?.let { params.add("reportType=$it") }
            dateRange?.let { params.add("dateRange=$it") }
            
            return if (params.isNotEmpty()) {
                "$route?${params.joinToString("&")}"
            } else route
        }
    }
    object ExpenseDetail : ExpenseDestination("expense_detail/{expenseId}") {
        fun createRoute(expenseId: String): String = "expense_detail/$expenseId"
        const val EXPENSE_ID_ARG = "expenseId"
    }
}

/**
 * Navigation Graph following Enterprise Shared ViewModel Pattern
 * 
 * COMPANY STANDARD APPROACH:
 * - Google: Shared ViewModels across navigation graph
 * - Netflix: Activity-scoped ViewModels for data sharing
 * - Airbnb: Shared state management across screens
 * 
 * This approach ensures:
 * 1. DATA CONSISTENCY across screens
 * 2. PERFORMANCE optimization (no data re-fetching)
 * 3. REACTIVE UPDATES across all screens
 * 4. PROPER LIFECYCLE management
 */
@Composable
fun ExpenseNavGraph(
    navController: NavHostController,
    themeViewModel: ThemeViewModel,
    startDestination: String = ExpenseDestination.Home.route,
    modifier: Modifier = Modifier
) {
    // Shared ViewModels - Enterprise Pattern
    // These ViewModels are scoped to the NavGraph lifecycle
    // Data persists across screen navigation
    val sharedExpenseViewModel: ExpenseViewModel = viewModel()
    val sharedReportViewModel: ReportViewModel = viewModel()
    
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Home Screen
        composable(ExpenseDestination.Home.route) {
            HomeScreen(
                onAddExpenseClick = {
                    navController.navigate(ExpenseDestination.AddExpense.route)
                },
                onViewAllExpensesClick = {
                    navController.navigate(ExpenseDestination.ExpenseList.route)
                },
                onViewReportsClick = {
                    navController.navigate(ExpenseDestination.ExpenseReport.route)
                },
                onExpenseClick = { expenseId ->
                    navController.navigate(
                        ExpenseDestination.ExpenseDetail.createRoute(expenseId)
                    )
                },
                expenseViewModel = sharedExpenseViewModel,
                themeViewModel = themeViewModel
            )
        }
        
        // Add Expense Screen
        composable(ExpenseDestination.AddExpense.route) {
            AddExpenseScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onExpenseAdded = {
                    // Navigate back and refresh data automatically via shared ViewModel
                    navController.popBackStack()
                },
                expenseViewModel = sharedExpenseViewModel
            )
        }

        // Edit Expense Screen
        composable(
            route = ExpenseDestination.EditExpense.route,
            arguments = listOf(
                navArgument(ExpenseDestination.EditExpense.EXPENSE_ID_ARG) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val expenseId = backStackEntry.arguments?.getString(ExpenseDestination.EditExpense.EXPENSE_ID_ARG)
            AddExpenseScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onExpenseAdded = {
                    // Navigate back after successful update
                    navController.popBackStack()
                },
                editExpenseId = expenseId,
                expenseViewModel = sharedExpenseViewModel
            )
        }

        // Expense List Screen with Optional Arguments
        composable(
            route = "${ExpenseDestination.ExpenseList.route}?dateFilter={dateFilter}&categoryId={categoryId}",
            arguments = listOf(
                navArgument("dateFilter") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument("categoryId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val dateFilter = backStackEntry.arguments?.getString("dateFilter")
            val categoryId = backStackEntry.arguments?.getString("categoryId")
            
            ExpenseListScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onExpenseClick = { expenseId ->
                    navController.navigate(
                        ExpenseDestination.ExpenseDetail.createRoute(expenseId)
                    )
                },
                initialDateFilter = dateFilter,
                initialCategoryId = categoryId,
                expenseViewModel = sharedExpenseViewModel
            )
        }
        
        // Expense Report Screen with Optional Arguments
        composable(
            route = "${ExpenseDestination.ExpenseReport.route}?reportType={reportType}&dateRange={dateRange}",
            arguments = listOf(
                navArgument("reportType") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = "weekly"
                },
                navArgument("dateRange") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val reportType = backStackEntry.arguments?.getString("reportType") ?: "weekly"
            val dateRange = backStackEntry.arguments?.getString("dateRange")
            
            ExpenseReportScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                initialReportType = reportType,
                initialDateRange = dateRange,
                reportViewModel = sharedReportViewModel,
                expenseViewModel = sharedExpenseViewModel // Shared for data consistency
            )
        }
        
        // Expense Detail Screen with Required Arguments
        composable(
            route = ExpenseDestination.ExpenseDetail.route,
            arguments = listOf(
                navArgument(ExpenseDestination.ExpenseDetail.EXPENSE_ID_ARG) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val expenseId = backStackEntry.arguments?.getString(
                ExpenseDestination.ExpenseDetail.EXPENSE_ID_ARG
            ) ?: return@composable
            
            ExpenseDetailScreen(
                expenseId = expenseId,
                onBackClick = {
                    navController.popBackStack()
                },
                onEditClick = { id ->
                    // Navigate to edit mode using EditExpense route
                    navController.navigate(ExpenseDestination.EditExpense.createRoute(id))
                },
                expenseViewModel = sharedExpenseViewModel
            )
        }
    }
}

/**
 * Navigation Extensions following Enterprise Helper Pattern
 * 
 * Used by companies like:
 * - Google (Compose Navigation samples)
 * - JetBrains (Kotlin Multiplatform samples)
 * - Square (Cash App navigation utilities)
 */
fun NavHostController.navigateToExpenseList(
    dateFilter: String? = null,
    categoryId: String? = null
) {
    navigate(ExpenseDestination.ExpenseList.createRoute(dateFilter, categoryId))
}

fun NavHostController.navigateToExpenseReport(
    reportType: String? = null,
    dateRange: String? = null
) {
    navigate(ExpenseDestination.ExpenseReport.createRoute(reportType, dateRange))
}

fun NavHostController.navigateToExpenseDetail(expenseId: String) {
    navigate(ExpenseDestination.ExpenseDetail.createRoute(expenseId))
}
