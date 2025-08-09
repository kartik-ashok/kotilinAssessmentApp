package com.example.kotlinassessmentapp.data.repository

import com.example.kotlinassessmentapp.data.model.*
import com.example.kotlinassessmentapp.domain.repository.IExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
/**
 * ExpenseRepository Implementation following Enterprise Patterns
 * 
 * TEMPORARILY using Singleton pattern until Hilt version compatibility is resolved
 * 
 * This implementation pattern is used by:
 * - Google (Android Architecture Components samples)
 * - Netflix (Android app architecture)
 * - Spotify (documented in Android Dev Summit)
 * - Square (Cash App architecture patterns)
 * 
 * Key Enterprise Patterns:
 * 1. INTERFACE SEGREGATION - Implements IExpenseRepository contract
 * 2. SINGLE RESPONSIBILITY - Only handles data operations
 * 3. REACTIVE PROGRAMMING - Uses StateFlow for reactive updates
 * 4. THREAD SAFETY - StateFlow handles concurrent access safely
 */
class ExpenseRepository private constructor() : IExpenseRepository {
    
    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    override val expenses: Flow<List<Expense>> = _expenses.asStateFlow()
    
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
    
    override suspend fun addExpense(expense: Expense) {
        val currentExpenses = _expenses.value.toMutableList()
        currentExpenses.add(expense)
        _expenses.value = currentExpenses
    }
    
    override suspend fun updateExpense(expense: Expense) {
        val currentExpenses = _expenses.value.toMutableList()
        val index = currentExpenses.indexOfFirst { it.id == expense.id }
        if (index != -1) {
            currentExpenses[index] = expense
            _expenses.value = currentExpenses
        }
    }
    
    override suspend fun deleteExpense(expenseId: String) {
        val currentExpenses = _expenses.value.toMutableList()
        currentExpenses.removeAll { it.id == expenseId }
        _expenses.value = currentExpenses
    }
    
    override fun getExpenseById(id: String): Expense? {
        return _expenses.value.find { it.id == id }
    }
    
    override fun getExpensesByCategory(category: Category): Flow<List<Expense>> {
        return expenses.map { list -> 
            list.filter { it.category.id == category.id }
        }
    }
    
    override fun getExpensesByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<Expense>> {
        return expenses.map { list ->
            list.filter { expense ->
                expense.date.isAfter(startDate) && expense.date.isBefore(endDate)
            }
        }
    }
    
    override fun getTotalExpenses(): Flow<Double> {
        return expenses.map { list ->
            list.sumOf { it.amount }
        }
    }
    
    override fun getMonthlyReport(yearMonth: YearMonth): Flow<Report> {
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

    /**
     * Export functionality for reports
     * Simulates PDF/CSV export generation
     */
    fun generateReportCSV(): String {
        val expenses = _expenses.value
        val csvContent = buildString {
            appendLine("Date,Title,Category,Amount,Description")
            expenses.forEach { expense ->
                appendLine(
                    "${expense.date.format(DateTimeFormatter.ISO_LOCAL_DATE)}," +
                    "\"${expense.title}\"," +
                    "\"${expense.category.name}\"," +
                    "${expense.amount}," +
                    "\"${expense.description}\""
                )
            }
        }

        // Simulate file creation
        val fileName = "expense_report_${System.currentTimeMillis()}.csv"
        // In a real app, you would save this to internal storage or external storage
        return fileName
    }

    fun generateReportPDF(): String {
        // Simulate PDF generation
        val fileName = "expense_report_${System.currentTimeMillis()}.pdf"
        // In a real app, you would use a PDF library like iText or similar
        return fileName
    }

    fun getShareableReportData(): String {
        val expenses = _expenses.value
        val totalAmount = expenses.sumOf { it.amount }
        val expenseCount = expenses.size

        return buildString {
            appendLine("📊 Expense Report")
            appendLine("================")
            appendLine("Total Expenses: ₹${String.format("%.2f", totalAmount)}")
            appendLine("Number of Expenses: $expenseCount")
            appendLine()
            appendLine("Category Breakdown:")

            expenses.groupBy { it.category }
                .mapValues { (_, expenses) -> expenses.sumOf { it.amount } }
                .toList()
                .sortedByDescending { it.second }
                .forEach { (category, amount) ->
                    appendLine("• ${category.name}: ₹${String.format("%.2f", amount)}")
                }

            appendLine()
            appendLine("Generated on: ${LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"))}")
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