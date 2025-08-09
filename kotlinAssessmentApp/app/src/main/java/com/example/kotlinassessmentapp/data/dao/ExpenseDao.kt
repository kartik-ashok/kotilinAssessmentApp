package com.example.kotlinassessmentapp.data.dao

import androidx.room.*
import com.example.kotlinassessmentapp.data.model.Category
import com.example.kotlinassessmentapp.data.model.Expense
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

/**
 * Data Access Object for Expense entity
 * 
 * Following Enterprise Room Patterns:
 * - Flow for reactive data streams
 * - Suspend functions for async operations
 * - Comprehensive CRUD operations
 * - Query optimization with indexes
 */
@Dao
interface ExpenseDao {
    
    /**
     * Get all expenses ordered by date (newest first)
     */
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<Expense>>
    
    /**
     * Get expense by ID
     */
    @Query("SELECT * FROM expenses WHERE id = :id")
    suspend fun getExpenseById(id: String): Expense?
    
    /**
     * Get expenses by category
     */
    @Query("SELECT * FROM expenses WHERE category = :categoryName ORDER BY date DESC")
    fun getExpensesByCategory(categoryName: String): Flow<List<Expense>>
    
    /**
     * Get expenses within date range
     */
    @Query("SELECT * FROM expenses WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getExpensesByDateRange(startDate: String, endDate: String): Flow<List<Expense>>
    
    /**
     * Get total expenses amount
     */
    @Query("SELECT SUM(amount) FROM expenses")
    fun getTotalExpenses(): Flow<Double?>
    
    /**
     * Get expenses for today
     */
    @Query("SELECT * FROM expenses WHERE date(date) = date('now', 'localtime') ORDER BY date DESC")
    fun getTodayExpenses(): Flow<List<Expense>>
    
    /**
     * Get total amount for today
     */
    @Query("SELECT SUM(amount) FROM expenses WHERE date(date) = date('now', 'localtime')")
    fun getTodayTotal(): Flow<Double?>
    
    /**
     * Get expenses for current month
     */
    @Query("SELECT * FROM expenses WHERE strftime('%Y-%m', date) = strftime('%Y-%m', 'now', 'localtime') ORDER BY date DESC")
    fun getCurrentMonthExpenses(): Flow<List<Expense>>
    
    /**
     * Get monthly total
     */
    @Query("SELECT SUM(amount) FROM expenses WHERE strftime('%Y-%m', date) = strftime('%Y-%m', 'now', 'localtime')")
    fun getCurrentMonthTotal(): Flow<Double?>
    
    /**
     * Get expenses grouped by category with totals
     */
    @Query("SELECT category, SUM(amount) as total FROM expenses GROUP BY category ORDER BY total DESC")
    fun getCategoryTotals(): Flow<List<CategoryTotal>>
    
    /**
     * Insert new expense
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)
    
    /**
     * Insert multiple expenses
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpenses(expenses: List<Expense>)
    
    /**
     * Update expense
     */
    @Update
    suspend fun updateExpense(expense: Expense)
    
    /**
     * Delete expense
     */
    @Delete
    suspend fun deleteExpense(expense: Expense)
    
    /**
     * Delete expense by ID
     */
    @Query("DELETE FROM expenses WHERE id = :id")
    suspend fun deleteExpenseById(id: String)
    
    /**
     * Delete all expenses
     */
    @Query("DELETE FROM expenses")
    suspend fun deleteAllExpenses()
    
    /**
     * Get expense count
     */
    @Query("SELECT COUNT(*) FROM expenses")
    fun getExpenseCount(): Flow<Int>
    
    /**
     * Search expenses by title or description
     */
    @Query("SELECT * FROM expenses WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' ORDER BY date DESC")
    fun searchExpenses(query: String): Flow<List<Expense>>

    /**
     * Get expenses by month and year
     */
    @Query("SELECT * FROM expenses WHERE strftime('%Y-%m', date) = :yearMonth ORDER BY date DESC")
    fun getExpensesByMonth(yearMonth: String): Flow<List<Expense>>

    /**
     * Get monthly total for specific month
     */
    @Query("SELECT SUM(amount) FROM expenses WHERE strftime('%Y-%m', date) = :yearMonth")
    fun getMonthlyTotal(yearMonth: String): Flow<Double?>
}

/**
 * Data class for category totals query result
 */
data class CategoryTotal(
    val category: Category,
    val total: Double
)
