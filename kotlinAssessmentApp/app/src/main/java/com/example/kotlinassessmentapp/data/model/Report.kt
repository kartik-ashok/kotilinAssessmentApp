package com.example.kotlinassessmentapp.data.model

import java.time.LocalDateTime
import java.time.YearMonth

data class Report(
    val period: YearMonth,
    val totalExpenses: Double,
    val expenseCount: Int,
    val categoryBreakdown: Map<Category, Double>,
    val topCategories: List<Pair<Category, Double>>,
    val averageDaily: Double,
    val generatedAt: LocalDateTime = LocalDateTime.now()
)

data class ExpensesByCategory(
    val category: Category,
    val expenses: List<Expense>,
    val total: Double
)

data class MonthlyExpenseTrend(
    val month: YearMonth,
    val totalAmount: Double,
    val expenseCount: Int
) 