package com.example.kotlinassessmentapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinassessmentapp.data.model.*
import com.example.kotlinassessmentapp.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalDate
import java.time.YearMonth
import java.util.Calendar

/**
 * Extension function to convert LocalDateTime to LocalDate
 * This is needed for date comparisons in the ExpenseViewModel
 */
private fun LocalDateTime.toLocalDate(): LocalDate = LocalDate.of(this.year, this.monthValue, this.dayOfMonth)

/**
 * Utility function to subtract days from LocalDate using Calendar (API level 21 compatible)
 * This replaces LocalDate.minusDays() which requires API level 26
 */
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

/**
 * Utility function to add days to LocalDate using Calendar (API level 21 compatible)
 * This replaces LocalDate.plusDays() which requires API level 26
 */
private fun LocalDate.plusDaysCompat(days: Long): LocalDate {
    val calendar = Calendar.getInstance()
    calendar.set(this.year, this.monthValue - 1, this.dayOfMonth) // Calendar months are 0-based
    calendar.add(Calendar.DAY_OF_MONTH, days.toInt())
    return LocalDate.of(
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH) + 1, // Convert back to 1-based
        calendar.get(Calendar.DAY_OF_MONTH)
    )
}

/**
 * Grouping options for expense list
 */
enum class GroupBy {
    NONE,
    CATEGORY,
    TIME
}

/**
 * Date filter options
 */
enum class DateFilter {
    TODAY,
    THIS_WEEK,
    THIS_MONTH,
    CUSTOM_RANGE,
    ALL_TIME
}

/**
 * UI State for Expense Screen following Modern Enterprise Patterns
 * 
 * This immutable data class pattern is used by:
 * - Google (Android Architecture Components samples)
 * - Square (in their Android apps)
 * - JetBrains (Kotlin Multiplatform samples)
 * 
 * Benefits:
 * - IMMUTABLE state prevents accidental mutations
 * - SINGLE SOURCE OF TRUTH for UI state
 * - PREDICTABLE state updates
 * - EASY TESTING with known state objects
 */
data class ExpenseUiState(
    val expenses: List<Expense> = emptyList(),
    val totalAmount: Double = 0.0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedCategory: Category? = null,
    val searchQuery: String = "",
    val groupBy: GroupBy = GroupBy.NONE,
    val dateFilter: DateFilter = DateFilter.TODAY,
    val customStartDate: LocalDate? = null,
    val customEndDate: LocalDate? = null,
    val groupedExpenses: Map<String, List<Expense>> = emptyMap(),
    val expenseCount: Int = 0
)

/**
 * ExpenseViewModel following Enterprise MVVM Pattern
 * 
 * TEMPORARILY using direct repository access until Hilt compatibility is resolved
 * 
 * This pattern is STANDARD at companies like:
 * - Google (all Android sample apps use this pattern)
 * - Netflix (documented in their tech blog)
 * - Airbnb (mentioned in Android Dev Summit talks)
 * - Spotify (Android team uses ViewModel pattern)
 * 
 * Benefits:
 * 1. LIFECYCLE AWARE - Survives configuration changes
 * 2. REACTIVE STATE - UI updates automatically with StateFlow
 * 3. SEPARATION OF CONCERNS - Business logic separated from UI
 * 4. TESTABLE - Can be tested with mock repositories
 */
class ExpenseViewModel(
    private val repository: ExpenseRepository = ExpenseRepository.getInstance()
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ExpenseUiState())
    val uiState: StateFlow<ExpenseUiState> = _uiState.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    private val _selectedCategory = MutableStateFlow<Category?>(null)
    private val _groupBy = MutableStateFlow(GroupBy.NONE)
    private val _dateFilter = MutableStateFlow(DateFilter.TODAY)
    private val _customStartDate = MutableStateFlow<LocalDate?>(null)
    private val _customEndDate = MutableStateFlow<LocalDate?>(null)
    
    init {
        loadExpenses()
    }
    
    private fun loadExpenses() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // Combine all the flows using the correct syntax for multiple flows
                val combinedFlow = combine(
                    repository.expenses,
                    _searchQuery,
                    _selectedCategory,
                    _groupBy,
                    _dateFilter
                ) { expenses: List<Expense>, query: String, category: Category?, groupBy: GroupBy, dateFilter: DateFilter ->
                    ProcessedExpenseData(expenses, query, category, groupBy, dateFilter)
                }

                val finalFlow = combine(
                    combinedFlow,
                    _customStartDate,
                    _customEndDate
                ) { processedData: ProcessedExpenseData, startDate: LocalDate?, endDate: LocalDate? ->
                    var filteredExpenses = processedData.expenses

                    // Apply search filter
                    if (processedData.query.isNotBlank()) {
                        filteredExpenses = filteredExpenses.filter { expense: Expense ->
                            expense.title.contains(processedData.query, ignoreCase = true) ||
                            expense.description.contains(processedData.query, ignoreCase = true)
                        }
                    }

                    // Apply category filter
                    if (processedData.category != null) {
                        filteredExpenses = filteredExpenses.filter { expense: Expense ->
                            expense.category.id == processedData.category.id
                        }
                    }

                    // Apply date filter
                    filteredExpenses = applyDateFilter(filteredExpenses, processedData.dateFilter, startDate, endDate)

                    // Sort expenses
                    val sortedExpenses = filteredExpenses.sortedByDescending { it.date }

                    // Apply grouping
                    val groupedExpenses = applyGrouping(sortedExpenses, processedData.groupBy)

                    val totalAmount = filteredExpenses.sumOf { it.amount }

                    ExpenseUiState(
                        expenses = sortedExpenses,
                        totalAmount = totalAmount,
                        isLoading = false,
                        searchQuery = processedData.query,
                        selectedCategory = processedData.category,
                        groupBy = processedData.groupBy,
                        dateFilter = processedData.dateFilter,
                        customStartDate = startDate,
                        customEndDate = endDate,
                        groupedExpenses = groupedExpenses,
                        expenseCount = filteredExpenses.size
                    )
                }

                finalFlow.collect { state ->
                    _uiState.value = state
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to load expenses: ${e.message ?: "Unknown error"}"
                )
            }
        }
    }

    /**
     * Helper data class for combining flows
     */
    private data class ProcessedExpenseData(
        val expenses: List<Expense>,
        val query: String,
        val category: Category?,
        val groupBy: GroupBy,
        val dateFilter: DateFilter
    )
    
    fun addExpense(
        title: String,
        amount: Double,
        category: Category,
        description: String = "",
        receiptImageUri: String? = null
    ) {
        viewModelScope.launch {
            try {
                // Input validation
                if (title.isBlank()) {
                    _uiState.value = _uiState.value.copy(errorMessage = "Title cannot be empty")
                    return@launch
                }
                if (amount <= 0) {
                    _uiState.value = _uiState.value.copy(errorMessage = "Amount must be greater than 0")
                    return@launch
                }

                // Duplicate expense detection
                val isDuplicate = checkForDuplicateExpense(title, amount, category)
                if (isDuplicate) {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Similar expense already exists today. Use 'Add Anyway' to proceed."
                    )
                    return@launch
                }

                val expense = Expense(
                    title = title.trim(),
                    amount = amount,
                    category = category,
                    description = description.trim(),
                    date = LocalDateTime.now(),
                    receiptImageUri = receiptImageUri
                )
                repository.addExpense(expense)

                // Clear any previous errors on successful addition
                _uiState.value = _uiState.value.copy(errorMessage = null)

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to add expense: ${e.message}"
                )
            }
        }
    }

    fun addExpenseIgnoreDuplicate(
        title: String,
        amount: Double,
        category: Category,
        description: String = "",
        receiptImageUri: String? = null
    ) {
        viewModelScope.launch {
            try {
                val expense = Expense(
                    title = title,
                    amount = amount,
                    category = category,
                    description = description,
                    date = LocalDateTime.now(),
                    receiptImageUri = receiptImageUri
                )
                repository.addExpense(expense)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }
    
    fun updateExpense(expense: Expense) {
        viewModelScope.launch {
            try {
                repository.updateExpense(expense)
                _uiState.value = _uiState.value.copy(errorMessage = null)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to update expense: ${e.message}"
                )
            }
        }
    }

    fun deleteExpense(expenseId: String) {
        viewModelScope.launch {
            try {
                if (expenseId.isBlank()) {
                    _uiState.value = _uiState.value.copy(errorMessage = "Invalid expense ID")
                    return@launch
                }
                repository.deleteExpense(expenseId)
                _uiState.value = _uiState.value.copy(errorMessage = null)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to delete expense: ${e.message}"
                )
            }
        }
    }
    
    fun searchExpenses(query: String) {
        _searchQuery.value = query.trim()
    }

    fun filterByCategory(category: Category?) {
        _selectedCategory.value = category
    }

    fun setGroupBy(groupBy: GroupBy) {
        _groupBy.value = groupBy
    }

    fun setDateFilter(dateFilter: DateFilter) {
        _dateFilter.value = dateFilter
        // Clear custom dates when switching to non-custom filter
        if (dateFilter != DateFilter.CUSTOM_RANGE) {
            _customStartDate.value = null
            _customEndDate.value = null
        }
    }

    fun setCustomDateRange(startDate: LocalDate?, endDate: LocalDate?) {
        // Validate date range
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Start date cannot be after end date"
            )
            return
        }

        _customStartDate.value = startDate
        _customEndDate.value = endDate
        if (startDate != null && endDate != null) {
            _dateFilter.value = DateFilter.CUSTOM_RANGE
        }

        // Clear error if dates are valid
        if (startDate == null || endDate == null || !startDate.isAfter(endDate)) {
            _uiState.value = _uiState.value.copy(errorMessage = null)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
    
    fun getExpensesByCategory(): StateFlow<Map<Category, List<Expense>>> {
        return repository.expenses.map { expenses: List<Expense> ->
            expenses.groupBy { expense: Expense -> expense.category }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )
    }

    /**
     * Reactive StateFlow for Today's Total Expenses
     * Automatically updates when expenses change - no manual refresh needed
     */
    fun getTodaysTotalExpenses(): StateFlow<Double> {
        return repository.expenses.map { expenses: List<Expense> ->
            val today = LocalDate.now()
            expenses.filter { expense: Expense ->
                expense.date.toLocalDate() == today
            }.sumOf { expense: Expense -> expense.amount }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )
    }

    /**
     * Reactive StateFlow for Today's Expense Count
     * Automatically updates when expenses change - no manual refresh needed
     */
    fun getTodaysExpenseCount(): StateFlow<Int> {
        return repository.expenses.map { expenses: List<Expense> ->
            val today = LocalDate.now()
            expenses.count { expense: Expense ->
                expense.date.toLocalDate() == today
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )
    }

    private fun applyDateFilter(
        expenses: List<Expense>,
        dateFilter: DateFilter,
        startDate: LocalDate?,
        endDate: LocalDate?
    ): List<Expense> {
        val today = LocalDate.now()
        return when (dateFilter) {
            DateFilter.TODAY -> expenses.filter { expense: Expense -> expense.date.toLocalDate() == today }
            DateFilter.THIS_WEEK -> {
                val startOfWeek = today.minusDaysCompat(today.dayOfWeek.value - 1L)
                val endOfWeek = startOfWeek.plusDaysCompat(6)
                expenses.filter { expense: Expense ->
                    val expenseDate = expense.date.toLocalDate()
                    expenseDate >= startOfWeek && expenseDate <= endOfWeek
                }
            }
            DateFilter.THIS_MONTH -> {
                val startOfMonth = today.withDayOfMonth(1)
                val endOfMonth = today.withDayOfMonth(today.lengthOfMonth())
                expenses.filter { expense: Expense ->
                    val expenseDate = expense.date.toLocalDate()
                    expenseDate >= startOfMonth && expenseDate <= endOfMonth
                }
            }
            DateFilter.CUSTOM_RANGE -> {
                if (startDate != null && endDate != null) {
                    expenses.filter { expense: Expense ->
                        val expenseDate = expense.date.toLocalDate()
                        expenseDate >= startDate && expenseDate <= endDate
                    }
                } else expenses
            }
            DateFilter.ALL_TIME -> expenses
        }
    }

    private fun applyGrouping(expenses: List<Expense>, groupBy: GroupBy): Map<String, List<Expense>> {
        return when (groupBy) {
            GroupBy.CATEGORY -> expenses.groupBy { expense: Expense -> expense.category.name }
            GroupBy.TIME -> expenses.groupBy { expense: Expense ->
                expense.date.toLocalDate().toString() // Group by date
            }
            GroupBy.NONE -> mapOf("All Expenses" to expenses)
        }
    }

    /**
     * Duplicate Expense Detection following Enterprise Business Logic Patterns
     *
     * Checks for similar expenses on the same day to prevent accidental duplicates
     * Used by financial apps like:
     * - Mint (duplicate transaction detection)
     * - YNAB (similar expense warnings)
     * - PocketGuard (duplicate spending alerts)
     */
    private suspend fun checkForDuplicateExpense(
        title: String,
        amount: Double,
        category: Category
    ): Boolean {
        return try {
            val today = LocalDate.now()
            val allExpenses: List<Expense> = repository.expenses.first()
            val todayExpenses: List<Expense> = allExpenses.filter { expense: Expense ->
                expense.date.toLocalDate() == today
            }

            todayExpenses.any { expense: Expense ->
                expense.title.trim().equals(title.trim(), ignoreCase = true) &&
                expense.amount == amount &&
                expense.category.id == category.id
            }
        } catch (e: Exception) {
            // If duplicate check fails, don't block the expense addition
            false
        }
    }
}