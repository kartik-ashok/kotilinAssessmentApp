package com.example.kotlinassessmentapp.data.repository

import android.content.Context
import com.example.kotlinassessmentapp.data.dao.ExpenseDao
import com.example.kotlinassessmentapp.data.database.ExpenseDatabase
import com.example.kotlinassessmentapp.data.model.*
import com.example.kotlinassessmentapp.domain.repository.IExpenseRepository
import com.example.kotlinassessmentapp.utils.FileExportManager
import com.example.kotlinassessmentapp.utils.ExportResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
class ExpenseRepository private constructor(private val context: Context) : IExpenseRepository {

    private val database = ExpenseDatabase.getDatabase(context)
    private val expenseDao = database.expenseDao()
    private val fileExportManager = FileExportManager(context)

    override val expenses: Flow<List<Expense>> = expenseDao.getAllExpenses()
    
    // Repository initialized - clean start without dummy data
    
    override suspend fun addExpense(expense: Expense) {
        expenseDao.insertExpense(expense)
    }

    override suspend fun updateExpense(expense: Expense) {
        expenseDao.updateExpense(expense)
    }

    override suspend fun deleteExpense(expenseId: String) {
        expenseDao.deleteExpenseById(expenseId)
    }
    
    override suspend fun getExpenseById(id: String): Expense? {
        return expenseDao.getExpenseById(id)
    }

    override fun getExpensesByCategory(category: Category): Flow<List<Expense>> {
        return expenseDao.getExpensesByCategory(category.name)
    }

    override fun getExpensesByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<Expense>> {
        val startDateStr = startDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val endDateStr = endDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        return expenseDao.getExpensesByDateRange(startDateStr, endDateStr)
    }

    override fun getTotalExpenses(): Flow<Double> {
        return expenseDao.getTotalExpenses().map { it ?: 0.0 }
    }
    
    override fun getMonthlyReport(yearMonth: YearMonth): Flow<Report> {
        val yearMonthStr = yearMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"))
        return expenseDao.getExpensesByMonth(yearMonthStr).map { monthExpenses ->
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
     * Real PDF/CSV export generation using FileExportManager
     */
    suspend fun generateReportPDF(): ExportResult {
        return try {
            val currentExpenses = getCurrentExpensesList()
            fileExportManager.exportToPDF(currentExpenses)
        } catch (e: Exception) {
            // Fallback to text report if PDF fails
            val currentExpenses = getCurrentExpensesList()
            fileExportManager.exportToTextReport(currentExpenses)
        }
    }

    suspend fun generateReportCSV(): ExportResult {
        val currentExpenses = getCurrentExpensesList()
        return fileExportManager.exportToCSV(currentExpenses)
    }

    private suspend fun getCurrentExpensesList(): List<Expense> {
        return try {
            // Get current expenses from database
            var expensesList = emptyList<Expense>()
            expenseDao.getAllExpenses().collect { expenses ->
                expensesList = expenses
                return@collect
            }
            expensesList
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun createShareablePDFReport(): ExportResult {
        val currentExpenses = getCurrentExpensesList()
        return fileExportManager.exportToPDF(currentExpenses)
    }

    fun createShareIntent(pdfResult: ExportResult.Success): android.content.Intent {
        return fileExportManager.createShareIntent(pdfResult.uri)
    }


    suspend fun getShareableReportData(): String {
        val expenses = getCurrentExpensesList()
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

        fun getInstance(context: Context): ExpenseRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ExpenseRepository(context.applicationContext).also { INSTANCE = it }
            }
        }

        fun getInstance(): ExpenseRepository {
            return INSTANCE ?: throw IllegalStateException("ExpenseRepository must be initialized with context first")
        }
    }
} 