package com.example.kotlinassessmentapp.data.model

import java.time.LocalDateTime
import java.util.UUID

data class Expense(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val amount: Double,
    val category: Category,
    val description: String = "",
    val date: LocalDateTime = LocalDateTime.now(),
    val isRecurring: Boolean = false
) 