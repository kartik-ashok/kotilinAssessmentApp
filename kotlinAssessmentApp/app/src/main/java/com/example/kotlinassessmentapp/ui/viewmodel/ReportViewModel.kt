package com.example.kotlinassessmentapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinassessmentapp.data.model.*
import com.example.kotlinassessmentapp.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.YearMonth

data class ReportUiState(
    val currentReport: Report? = null,
    val expensesByCategory: List<ExpensesByCategory> = emptyList(),
    val monthlyTrends: List<MonthlyExpenseTrend> = emptyList(),
    val selectedPeriod: YearMonth = YearMonth.now(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

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
                    _selectedPeriod
                ) { report, expensesByCategory, monthlyTrends, period ->
                    ReportUiState(
                        currentReport = report,
                        expensesByCategory = expensesByCategory,
                        monthlyTrends = monthlyTrends,
                        selectedPeriod = period,
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
} 