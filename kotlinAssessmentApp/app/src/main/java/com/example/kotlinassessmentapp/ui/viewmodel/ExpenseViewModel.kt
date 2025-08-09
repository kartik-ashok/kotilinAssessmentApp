package com.example.kotlinassessmentapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinassessmentapp.data.model.*
import com.example.kotlinassessmentapp.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.YearMonth

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
    val searchQuery: String = ""
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
    
    init {
        loadExpenses()
    }
    
    private fun loadExpenses() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                combine(
                    repository.expenses,
                    repository.getTotalExpenses(),
                    _searchQuery,
                    _selectedCategory
                ) { expenses, total, query, category ->
                    var filteredExpenses = expenses
                    
                    // Apply search filter
                    if (query.isNotBlank()) {
                        filteredExpenses = filteredExpenses.filter { expense ->
                            expense.title.contains(query, ignoreCase = true) ||
                            expense.description.contains(query, ignoreCase = true)
                        }
                    }
                    
                    // Apply category filter
                    if (category != null) {
                        filteredExpenses = filteredExpenses.filter { expense ->
                            expense.category.id == category.id
                        }
                    }
                    
                    ExpenseUiState(
                        expenses = filteredExpenses.sortedByDescending { it.date },
                        totalAmount = total,
                        isLoading = false,
                        searchQuery = query,
                        selectedCategory = category
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
    
    fun addExpense(
        title: String,
        amount: Double,
        category: Category,
        description: String = ""
    ) {
        viewModelScope.launch {
            try {
                val expense = Expense(
                    title = title,
                    amount = amount,
                    category = category,
                    description = description,
                    date = LocalDateTime.now()
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
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }
    
    fun deleteExpense(expenseId: String) {
        viewModelScope.launch {
            try {
                repository.deleteExpense(expenseId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }
    
    fun searchExpenses(query: String) {
        _searchQuery.value = query
    }
    
    fun filterByCategory(category: Category?) {
        _selectedCategory.value = category
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
    
    fun getExpensesByCategory(): StateFlow<Map<Category, List<Expense>>> {
        return repository.expenses.map { expenses ->
            expenses.groupBy { it.category }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )
    }
} 