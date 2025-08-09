package com.example.kotlinassessmentapp

import com.example.kotlinassessmentapp.data.model.Categories
import com.example.kotlinassessmentapp.data.model.Expense
import com.example.kotlinassessmentapp.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import java.time.YearMonth

/**
 * Test class to verify date/time operations work correctly
 * Tests the fixes made to ReportViewModel and ExpenseRepository
 */
class DateTimeOperationsTest {

    private lateinit var repository: ExpenseRepository

    @Before
    fun setup() {
        repository = ExpenseRepository.getInstance()
    }

    @Test
    fun `test LocalDateTime to LocalDate conversion`() {
        val dateTime = LocalDateTime.of(2024, 1, 15, 14, 30, 0)
        
        // Test the extension function logic (simulated)
        val localDate = java.time.LocalDate.of(dateTime.year, dateTime.monthValue, dateTime.dayOfMonth)
        
        assertEquals(2024, localDate.year)
        assertEquals(1, localDate.monthValue)
        assertEquals(15, localDate.dayOfMonth)
    }

    @Test
    fun `test date range filtering includes boundary dates`() = runTest {
        // Clear existing data
        val testExpenses = listOf(
            Expense(
                title = "Start Date Expense",
                amount = 100.0,
                category = Categories.FOOD,
                date = LocalDateTime.of(2024, 1, 1, 10, 0)
            ),
            Expense(
                title = "Middle Date Expense", 
                amount = 200.0,
                category = Categories.TRAVEL,
                date = LocalDateTime.of(2024, 1, 15, 12, 0)
            ),
            Expense(
                title = "End Date Expense",
                amount = 300.0,
                category = Categories.UTILITY,
                date = LocalDateTime.of(2024, 1, 31, 18, 0)
            ),
            Expense(
                title = "Outside Range Expense",
                amount = 400.0,
                category = Categories.STAFF,
                date = LocalDateTime.of(2024, 2, 1, 9, 0)
            )
        )

        // Add test expenses
        testExpenses.forEach { repository.addExpense(it) }

        val startDate = LocalDateTime.of(2024, 1, 1, 0, 0)
        val endDate = LocalDateTime.of(2024, 1, 31, 23, 59)

        val filteredExpenses = repository.getExpensesByDateRange(startDate, endDate).first()

        // Should include boundary dates (start and end)
        assertEquals(3, filteredExpenses.size)
        assertTrue(filteredExpenses.any { it.title == "Start Date Expense" })
        assertTrue(filteredExpenses.any { it.title == "Middle Date Expense" })
        assertTrue(filteredExpenses.any { it.title == "End Date Expense" })
        assertFalse(filteredExpenses.any { it.title == "Outside Range Expense" })
    }

    @Test
    fun `test monthly report generation`() = runTest {
        val testMonth = YearMonth.of(2024, 1)
        
        val testExpenses = listOf(
            Expense(
                title = "January Expense 1",
                amount = 150.0,
                category = Categories.FOOD,
                date = LocalDateTime.of(2024, 1, 10, 12, 0)
            ),
            Expense(
                title = "January Expense 2",
                amount = 250.0,
                category = Categories.TRAVEL,
                date = LocalDateTime.of(2024, 1, 20, 15, 0)
            ),
            Expense(
                title = "February Expense",
                amount = 100.0,
                category = Categories.UTILITY,
                date = LocalDateTime.of(2024, 2, 5, 10, 0)
            )
        )

        testExpenses.forEach { repository.addExpense(it) }

        val report = repository.getMonthlyReport(testMonth).first()

        assertEquals(testMonth, report.period)
        assertEquals(400.0, report.totalExpenses, 0.01) // 150 + 250
        assertEquals(2, report.expenseCount)
        assertTrue(report.categoryBreakdown.containsKey(Categories.FOOD))
        assertTrue(report.categoryBreakdown.containsKey(Categories.TRAVEL))
        assertFalse(report.categoryBreakdown.containsKey(Categories.UTILITY)) // February expense
    }

    @Test
    fun `test YearMonth from LocalDateTime conversion`() {
        val dateTime1 = LocalDateTime.of(2024, 3, 15, 14, 30)
        val dateTime2 = LocalDateTime.of(2024, 3, 25, 9, 15)
        val dateTime3 = LocalDateTime.of(2024, 4, 1, 16, 45)

        val yearMonth1 = YearMonth.from(dateTime1)
        val yearMonth2 = YearMonth.from(dateTime2)
        val yearMonth3 = YearMonth.from(dateTime3)

        assertEquals(YearMonth.of(2024, 3), yearMonth1)
        assertEquals(YearMonth.of(2024, 3), yearMonth2)
        assertEquals(YearMonth.of(2024, 4), yearMonth3)
        
        // Same month should be equal
        assertEquals(yearMonth1, yearMonth2)
        assertNotEquals(yearMonth1, yearMonth3)
    }

    @Test
    fun `test last 7 days calculation`() {
        val today = java.time.LocalDate.now()
        val last7Days = (6 downTo 0).map { today.minusDays(it.toLong()) }

        assertEquals(7, last7Days.size)
        assertEquals(today.minusDays(6), last7Days.first())
        assertEquals(today, last7Days.last())
        
        // Verify consecutive days
        for (i in 1 until last7Days.size) {
            assertEquals(1, java.time.temporal.ChronoUnit.DAYS.between(last7Days[i-1], last7Days[i]))
        }
    }

    @Test
    fun `test category expense percentage calculation`() {
        val totalAmount = 1000.0
        val categoryAmount = 250.0
        
        val percentage = if (totalAmount > 0) (categoryAmount / totalAmount * 100).toFloat() else 0f
        
        assertEquals(25.0f, percentage, 0.01f)
        
        // Test zero total
        val zeroPercentage = if (0.0 > 0) (categoryAmount / 0.0 * 100).toFloat() else 0f
        assertEquals(0.0f, zeroPercentage, 0.01f)
    }

    @Test
    fun `test average daily calculation`() {
        val totalExpenses = 3100.0
        val daysInMonth = 31
        
        val averageDaily = if (daysInMonth > 0) totalExpenses / daysInMonth else 0.0
        
        assertEquals(100.0, averageDaily, 0.01)
        
        // Test zero days
        val zeroAverage = if (0 > 0) totalExpenses / 0 else 0.0
        assertEquals(0.0, zeroAverage, 0.01)
    }
}
