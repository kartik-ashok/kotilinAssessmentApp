package com.example.kotlinassessmentapp.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinassessmentapp.data.model.*
import com.example.kotlinassessmentapp.data.repository.ExpenseRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

/**
 * Extension function to convert LocalDateTime to LocalDate
 * This is needed for date comparisons in the ReportViewModel
 */
@RequiresApi(Build.VERSION_CODES.O)
private fun LocalDateTime.toLocalDate(): LocalDate = LocalDate.of(this.year, this.monthValue, this.dayOfMonth)

/**
 * Utility function to subtract days from LocalDate using Calendar (API level 21 compatible)
 * This replaces LocalDate.minusDays() which requires API level 26
 */
@RequiresApi(Build.VERSION_CODES.O)
private fun LocalDate.minusDaysCompat(days: Long): LocalDate {
    val calendar = Calendar.getInstance()
    calendar.set(this.year, this.monthValue - 1, this.dayOfMonth) // Calendar months are 0-based
    calendar.add(Calendar.DAY_OF_MONTH, -days.toInt())
    return LocalDate.of(
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH) + 1, // Convert back to 1-based
        calendar.get(Calendar.DAY_OF_MONTH)
    )
}

data class DailyExpenseData @RequiresApi(Build.VERSION_CODES.O) constructor(
    val date: LocalDate,
    val totalAmount: Double,
    val expenseCount: Int,
    val formattedDate: String = date.format(DateTimeFormatter.ofPattern("MMM dd"))
)

data class CategoryExpenseData(
    val category: Category,
    val totalAmount: Double,
    val percentage: Float,
    val expenseCount: Int
)

data class ReportUiState @RequiresApi(Build.VERSION_CODES.O) constructor(
    val currentReport: Report? = null,
    val expensesByCategory: List<ExpensesByCategory> = emptyList(),
    val monthlyTrends: List<MonthlyExpenseTrend> = emptyList(),
    val dailyExpenses: List<DailyExpenseData> = emptyList(),
    val categoryExpenses: List<CategoryExpenseData> = emptyList(),
    val selectedPeriod: YearMonth = YearMonth.now(),
    val last7DaysTotal: Double = 0.0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

/**
 * ReportViewModel following Enterprise MVVM Pattern
 * 
 * TEMPORARILY using direct repository access until Hilt compatibility is resolved
 * This follows the same enterprise patterns as ExpenseViewModel
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalCoroutinesApi::class)
class ReportViewModel(
    private val repository: ExpenseRepository
) : ViewModel() {

    constructor() : this(ExpenseRepository.getInstance())
    
    private val _uiState = MutableStateFlow(ReportUiState())
    val uiState: StateFlow<ReportUiState> = _uiState.asStateFlow()
    
    @RequiresApi(Build.VERSION_CODES.O)
    private val _selectedPeriod = MutableStateFlow(YearMonth.now())
    
    init {
        loadReportData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadReportData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                // Combine all data flows efficiently
                combine(
                    _selectedPeriod.flatMapLatest { period ->
                        repository.getMonthlyReport(period)
                    },
                    repository.expenses,
                    _selectedPeriod
                ) { report, expenses, period ->
                    // Process all data in a single operation to avoid multiple iterations
                    val expensesByCategory = generateExpensesByCategory(expenses)
                    val monthlyTrends = generateMonthlyTrends(expenses)
                    val dailyExpenses = generateLast7DaysData(expenses)
                    val categoryExpenses = generateCategoryData(expenses)
                    val last7DaysTotal = dailyExpenses.sumOf { it.totalAmount }

                    ReportUiState(
                        currentReport = report,
                        expensesByCategory = expensesByCategory,
                        monthlyTrends = monthlyTrends,
                        dailyExpenses = dailyExpenses,
                        categoryExpenses = categoryExpenses,
                        selectedPeriod = period,
                        last7DaysTotal = last7DaysTotal,
                        isLoading = false,
                        errorMessage = null
                    )
                }.catch { e ->
                    // Handle errors in the flow
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to load report data: ${e.message ?: "Unknown error"}"
                    )
                }.collect { state ->
                    _uiState.value = state
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to load report data: ${e.message ?: "Unknown error"}"
                )
            }
        }
    }


    
    private fun generateExpensesByCategory(expenses: List<Expense>): List<ExpensesByCategory> {
        return try {
            expenses
                .groupBy { it.category }
                .map { (category, categoryExpenses) ->
                    ExpensesByCategory(
                        category = category,
                        expenses = categoryExpenses.sortedByDescending { it.date },
                        total = categoryExpenses.sumOf { it.amount }
                    )
                }
                .sortedByDescending { it.total }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun generateMonthlyTrends(expenses: List<Expense>): List<MonthlyExpenseTrend> {
        return try {
            expenses
                .groupBy { YearMonth.from(it.date) }
                .map { (month, monthExpenses) ->
                    MonthlyExpenseTrend(
                        month = month,
                        totalAmount = monthExpenses.sumOf { it.amount },
                        expenseCount = monthExpenses.size
                    )
                }
                .sortedBy { it.month }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    @RequiresApi(Build.VERSION_CODES.O)
    fun selectPeriod(yearMonth: YearMonth) {
        try {
            _selectedPeriod.value = yearMonth
            // Clear any existing errors when selecting a new period
            _uiState.value = _uiState.value.copy(errorMessage = null)
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Failed to select period: ${e.message ?: "Unknown error"}"
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentMonthExpenses(): StateFlow<List<Expense>> {
        return repository.expenses.map { expenses ->
            try {
                val currentMonth = YearMonth.now()
                expenses.filter { expense ->
                    YearMonth.from(expense.date) == currentMonth
                }
            } catch (e: Exception) {
                emptyList()
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTopCategoriesThisMonth(): StateFlow<List<Pair<Category, Double>>> {
        return repository.expenses.map { expenses ->
            try {
                val currentMonth = YearMonth.now()
                expenses
                    .filter { YearMonth.from(it.date) == currentMonth }
                    .groupBy { it.category }
                    .mapValues { (_, expenses) -> expenses.sumOf { it.amount } }
                    .toList()
                    .sortedByDescending { it.second }
                    .take(3)
            } catch (e: Exception) {
                emptyList()
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateLast7DaysData(expenses: List<Expense>): List<DailyExpenseData> {
        return try {
            val today = LocalDate.now()
            val last7Days = (6 downTo 0).map { today.minusDaysCompat(it.toLong()) }

            last7Days.map { date ->
                val dayExpenses = expenses.filter { expense ->
                    expense.date.toLocalDate() == date
                }

                DailyExpenseData(
                    date = date,
                    totalAmount = dayExpenses.sumOf { it.amount },
                    expenseCount = dayExpenses.size
                )
            }
        } catch (e: Exception) {
            // Return empty data for the last 7 days if there's an error
            val today = LocalDate.now()
            (6 downTo 0).map {
                DailyExpenseData(
                    date = today.minusDaysCompat(it.toLong()),
                    totalAmount = 0.0,
                    expenseCount = 0
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateCategoryData(expenses: List<Expense>): List<CategoryExpenseData> {
        return try {
            val today = LocalDate.now()
            val last7DaysExpenses = expenses.filter { expense ->
                val expenseDate = expense.date.toLocalDate()
                expenseDate >= today.minusDaysCompat(6) && expenseDate <= today
            }

            val totalAmount = last7DaysExpenses.sumOf { it.amount }

            last7DaysExpenses
                .groupBy { it.category }
                .map { (category, categoryExpenses) ->
                    val categoryTotal = categoryExpenses.sumOf { it.amount }
                    CategoryExpenseData(
                        category = category,
                        totalAmount = categoryTotal,
                        percentage = if (totalAmount > 0) (categoryTotal / totalAmount * 100).toFloat() else 0f,
                        expenseCount = categoryExpenses.size
                    )
                }
                .sortedByDescending { it.totalAmount }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun exportReport(format: String): String {
        return try {
            // Validate format
            val validFormats = listOf("pdf", "csv", "txt")
            val normalizedFormat = format.lowercase().trim()

            if (normalizedFormat !in validFormats) {
                throw IllegalArgumentException("Unsupported format: $format. Supported formats: ${validFormats.joinToString()}")
            }

            // Simulate export functionality with proper error handling
            val timestamp = System.currentTimeMillis()
            val filename = "report_$timestamp.$normalizedFormat"

            // In a real implementation, you would:
            // 1. Generate the actual report content
            // 2. Save to device storage
            // 3. Return the file path or URI

            filename
        } catch (e: Exception) {
            // Log error and return a default filename
            "report_error_${System.currentTimeMillis()}.txt"
        }
    }

    /**
     * Refresh report data manually
     * Useful for pull-to-refresh functionality
     */
    fun refreshReportData() {
        loadReportData()
    }

    /**
     * Get current UI state value (for one-time reads)
     */
    fun getCurrentUiState(): ReportUiState = _uiState.value

    override fun onCleared() {
        super.onCleared()
        // Clean up any resources if needed
        // The StateFlow will be automatically cleaned up by the ViewModel
    }
}