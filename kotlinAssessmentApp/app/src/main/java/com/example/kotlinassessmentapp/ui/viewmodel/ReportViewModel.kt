package com.example.kotlinassessmentapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinassessmentapp.data.model.*
import com.example.kotlinassessmentapp.data.repository.ExpenseRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class DailyExpenseData(
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

data class ReportUiState(
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
@OptIn(ExperimentalCoroutinesApi::class)
class ReportViewModel(
    private val repository: ExpenseRepository = ExpenseRepository.getInstance()
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ReportUiState())
    val uiState: StateFlow<ReportUiState> = _uiState.asStateFlow()
    
    private val _selectedPeriod = MutableStateFlow(YearMonth.now())
    
    init {
        loadReportData()
    }
    
    private fun loadReportData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                combine(
                    _selectedPeriod.flatMapLatest { period ->
                        repository.getMonthlyReport(period)
                    },
                    repository.expenses.map { expenses ->
                        generateExpensesByCategory(expenses)
                    },
                    repository.expenses.map { expenses ->
                        generateMonthlyTrends(expenses)
                    },
                    repository.expenses.map { expenses ->
                        generateLast7DaysData(expenses)
                    },
                    repository.expenses.map { expenses ->
                        generateCategoryData(expenses)
                    },
                    _selectedPeriod
                ) { report, expensesByCategory, monthlyTrends, dailyData, categoryData, period ->
                    ReportUiState(
                        currentReport = report,
                        expensesByCategory = expensesByCategory,
                        monthlyTrends = monthlyTrends,
                        dailyExpenses = dailyData,
                        categoryExpenses = categoryData,
                        selectedPeriod = period,
                        last7DaysTotal = dailyData.sumOf { it.totalAmount },
                        isLoading = false
                    )
                }.collect { state ->
                    _uiState.value = state
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }
    
    private fun generateExpensesByCategory(expenses: List<Expense>): List<ExpensesByCategory> {
        return expenses
            .groupBy { it.category }
            .map { (category, categoryExpenses) ->
                ExpensesByCategory(
                    category = category,
                    expenses = categoryExpenses.sortedByDescending { it.date },
                    total = categoryExpenses.sumOf { it.amount }
                )
            }
            .sortedByDescending { it.total }
    }
    
    private fun generateMonthlyTrends(expenses: List<Expense>): List<MonthlyExpenseTrend> {
        return expenses
            .groupBy { YearMonth.from(it.date) }
            .map { (month, monthExpenses) ->
                MonthlyExpenseTrend(
                    month = month,
                    totalAmount = monthExpenses.sumOf { it.amount },
                    expenseCount = monthExpenses.size
                )
            }
            .sortedBy { it.month }
    }
    
    fun selectPeriod(yearMonth: YearMonth) {
        _selectedPeriod.value = yearMonth
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
    
    fun getCurrentMonthExpenses(): StateFlow<List<Expense>> {
        return repository.expenses.map { expenses ->
            val currentMonth = YearMonth.now()
            expenses.filter { expense ->
                YearMonth.from(expense.date) == currentMonth
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }
    
    fun getTopCategoriesThisMonth(): StateFlow<List<Pair<Category, Double>>> {
        return repository.expenses.map { expenses ->
            val currentMonth = YearMonth.now()
            expenses
                .filter { YearMonth.from(it.date) == currentMonth }
                .groupBy { it.category }
                .mapValues { (_, expenses) -> expenses.sumOf { it.amount } }
                .toList()
                .sortedByDescending { it.second }
                .take(3)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    private fun generateLast7DaysData(expenses: List<Expense>): List<DailyExpenseData> {
        val today = LocalDate.now()
        val last7Days = (6 downTo 0).map { today.minusDays(it.toLong()) }

        return last7Days.map { date ->
            val dayExpenses = expenses.filter { expense ->
                expense.date.toLocalDate() == date
            }

            DailyExpenseData(
                date = date,
                totalAmount = dayExpenses.sumOf { it.amount },
                expenseCount = dayExpenses.size
            )
        }
    }

    private fun generateCategoryData(expenses: List<Expense>): List<CategoryExpenseData> {
        val today = LocalDate.now()
        val last7DaysExpenses = expenses.filter { expense ->
            val expenseDate = expense.date.toLocalDate()
            expenseDate >= today.minusDays(6) && expenseDate <= today
        }

        val totalAmount = last7DaysExpenses.sumOf { it.amount }

        return last7DaysExpenses
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
    }

    fun exportReport(format: String): String {
        // Simulate export functionality
        return when (format.lowercase()) {
            "pdf" -> "report_${System.currentTimeMillis()}.pdf"
            "csv" -> "report_${System.currentTimeMillis()}.csv"
            else -> "report_${System.currentTimeMillis()}.txt"
        }
    }
}