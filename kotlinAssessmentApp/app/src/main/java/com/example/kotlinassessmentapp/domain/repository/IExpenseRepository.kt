package com.example.kotlinassessmentapp.domain.repository

import com.example.kotlinassessmentapp.data.model.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.time.YearMonth

/**
 * Repository Interface following Enterprise Domain-Driven Design (DDD) Patterns
 * 
 * This interface-based approach is STANDARD at enterprise companies:
 * - Google (Android Architecture Components use this pattern)
 * - Netflix (documented in their clean architecture blog posts)
 * - Uber (mentioned in their Android engineering talks)
 * - Airbnb (used in their mobile architecture)
 * - Square (Cash App follows similar patterns)
 * 
 * Benefits of Interface-Based Repository Pattern:
 * 1. DEPENDENCY INVERSION - High-level modules don't depend on low-level modules
 * 2. TESTABILITY - Easy to create mock implementations for testing
 * 3. FLEXIBILITY - Can switch between different data sources (Room, Network, etc.)
 * 4. CLEAN ARCHITECTURE - Domain layer doesn't know about data implementation details
 * 5. CONTRACT DEFINITION - Clear API contract for data operations
 * 
 * This follows Robert C. Martin's Clean Architecture principles
 */
interface IExpenseRepository {
    
    /**
     * Reactive data streams for real-time UI updates
     * Flow-based APIs are preferred in modern Android development
     */
    val expenses: Flow<List<Expense>>
    
    /**
     * CRUD Operations following Repository Pattern
     * All operations are suspend functions for coroutine-based async execution
     */
    suspend fun addExpense(expense: Expense)
    suspend fun updateExpense(expense: Expense)
    suspend fun deleteExpense(expenseId: String)
    
    /**
     * Query Operations for business logic
     */
    fun getExpenseById(id: String): Expense?
    fun getExpensesByCategory(category: Category): Flow<List<Expense>>
    fun getExpensesByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<Expense>>
    
    /**
     * Aggregation Operations for analytics and reporting
     */
    fun getTotalExpenses(): Flow<Double>
    fun getMonthlyReport(yearMonth: YearMonth): Flow<Report>
    
    /**
     * Future enterprise methods would include:
     * - suspend fun syncWithRemote(): Result<Unit>
     * - fun getExpensesWithPagination(page: Int, size: Int): Flow<PagingData<Expense>>
     * - suspend fun exportExpenses(): Result<String>
     * - suspend fun importExpenses(data: String): Result<Unit>
     */
} 