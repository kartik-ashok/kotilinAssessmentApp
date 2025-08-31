package com.example.kotlinassessmentapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@Entity(tableName = "expenses")
@TypeConverters(Converters::class)
data class Expense(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val amount: Double,
    val category: Category,
    val description: String = "",
    val date: LocalDateTime = LocalDateTime.now(),
    val isRecurring: Boolean = false,
    val receiptImageUri: String? = null
)

/**
 * Type converters for Room database
 */
class Converters {
    @TypeConverter
    fun fromLocalDateTime(date: LocalDateTime?): String? {
        return date?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    @TypeConverter
    fun toLocalDateTime(dateString: String?): LocalDateTime? {
        return dateString?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME) }
    }

    @TypeConverter
    fun fromCategory(category: Category): String {
        return category.name
    }

    @TypeConverter
    fun toCategory(categoryName: String): Category {
        return Categories.all.find { it.name == categoryName } ?: Categories.FOOD
    }
}