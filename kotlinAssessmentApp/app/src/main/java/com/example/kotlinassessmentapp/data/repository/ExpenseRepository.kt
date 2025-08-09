package com.example.kotlinassessmentapp.data.repository

import com.example.kotlinassessmentapp.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.YearMonth

class ExpenseRepository {
    
    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: Flow<List<Expense>> = _expenses.asStateFlow()
    
    // Sample data for demonstration
    init {
        _expenses.value = listOf(
            Expense(
                title = "Lunch at Restaurant",
                amount = 25.50,
                category = Categories.FOOD,
                description = "Lunch with colleagues",
                date = LocalDateTime.now().minusDays(1)
            ),
            Expense(
                title = "Gas Station",
                amount = 45.00,
                category = Categories.TRANSPORT,
                description = "Weekly fuel",
                date = LocalDateTime.now().minusDays(2)
            ),
            Expense(
                title = "Grocery Shopping",
                amount = 85.30,
                category = Categories.FOOD,
                description = "Weekly groceries",
                date = LocalDateTime.now().minusDays(3)
            )
        )
    }
    
    suspend fun addExpense(expense: Expense) {
        val currentExpenses = _expenses.value.toMutableList()
        currentExpenses.add(expense)
        _expenses.value = currentExpenses
    }
    
    suspend fun updateExpense(expense: Expense) {
        val currentExpenses = _expenses.value.toMutableList()
        val index = currentExpenses.indexOfFirst { it.id == expense.id }
        if (index != -1) {
            currentExpenses[index] = expense
            _expenses.value = currentExpenses
        }
    }
    
    suspend fun deleteExpense(expenseId: String) {
        val currentExpenses = _expenses.value.toMutableList()
        currentExpenses.removeAll { it.id == expenseId }
        _expenses.value = currentExpenses
    }
    
    fun getExpenseById(id: String): Expense? {
        return _expenses.value.find { it.id == id }
    }
    
    fun getExpensesByCategory(category: Category): Flow<List<Expense>> {
        return expenses.map { list -> 
            list.filter { it.category.id == category.id }
        }
    }
    
    fun getExpensesByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<Expense>> {
        return expenses.map { list ->
            list.filter { expense ->
                expense.date.isAfter(startDate) && expense.date.isBefore(endDate)
            }
        }
    }
    
    fun getTotalExpenses(): Flow<Double> {
        return expenses.map { list ->
            list.sumOf { it.amount }
        }
    }
    
    fun getMonthlyReport(yearMonth: YearMonth): Flow<Report> {
        return expenses.map { list ->
            val monthExpenses = list.filter { expense ->
                YearMonth.from(expense.date) == yearMonth
            }
            
            val totalExpenses = monthExpenses.sumOf { it.amount }
            val expenseCount = monthExpenses.size
            
            val categoryBreakdown = monthExpenses.groupBy { it.category }
                .mapValues { (_, expenses) -> expenses.sumOf { it.amount } }
            
            val topCategories = categoryBreakdown.toList()
                .sortedByDescending { it.second }
                .take(5)
            
            val daysInMonth = yearMonth.lengthOfMonth()
            val averageDaily = if (daysInMonth > 0) totalExpenses / daysInMonth else 0.0
            
            Report(
                period = yearMonth,
                totalExpenses = totalExpenses,
                expenseCount = expenseCount,
                categoryBreakdown = categoryBreakdown,
                topCategories = topCategories,
                averageDaily = averageDaily
            )
        }
    }
    
    companion object {
        @Volatile
        private var INSTANCE: ExpenseRepository? = null
        
        fun getInstance(): ExpenseRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ExpenseRepository().also { INSTANCE = it }
            }
        }
    }
} 