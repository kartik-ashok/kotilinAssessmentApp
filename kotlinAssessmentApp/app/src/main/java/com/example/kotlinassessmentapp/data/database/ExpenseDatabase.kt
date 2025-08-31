package com.example.kotlinassessmentapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.kotlinassessmentapp.data.dao.ExpenseDao
import com.example.kotlinassessmentapp.data.model.Converters
import com.example.kotlinassessmentapp.data.model.Expense

/**
 * Room Database for Expense Tracker
 * 
 * Enterprise Features:
 * - Singleton pattern for database instance
 * - Type converters for complex data types
 * - Migration support for schema changes
 * - Proper database versioning
 * - Thread-safe operations
 */
@Database(
    entities = [Expense::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ExpenseDatabase : RoomDatabase() {
    
    abstract fun expenseDao(): ExpenseDao
    
    companion object {
        @Volatile
        private var INSTANCE: ExpenseDatabase? = null
        
        private const val DATABASE_NAME = "expense_database"
        
        /**
         * Get database instance using singleton pattern
         */
        fun getDatabase(context: Context): ExpenseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExpenseDatabase::class.java,
                    DATABASE_NAME
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
        
        /**
         * Database callback for initialization
         */
        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Database created - can add initial data here if needed
            }
            
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                // Database opened - can perform maintenance tasks here
            }
        }
        
        /**
         * Migration from version 1 to 2 (example for future use)
         */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Example migration - add new column
                // database.execSQL("ALTER TABLE expenses ADD COLUMN new_column TEXT")
            }
        }
        
        /**
         * Clear database instance (for testing)
         */
        fun clearInstance() {
            INSTANCE = null
        }
    }
}
