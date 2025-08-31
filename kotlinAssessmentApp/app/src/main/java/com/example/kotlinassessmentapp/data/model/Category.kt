package com.example.kotlinassessmentapp.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

data class Category(
    val id: String,
    val name: String,
    val icon: ImageVector,
    val color: Long
)

// Predefined categories as per requirements: Staff, Travel, Food, Utility
object Categories {
    val STAFF = Category("staff", "Staff", Icons.Default.Person, 0xFF2196F3)
    val TRAVEL = Category("travel", "Travel", Icons.Default.Flight, 0xFF00BCD4)
    val FOOD = Category("food", "Food", Icons.Default.Restaurant, 0xFFFF5722)
    val UTILITY = Category("utility", "Utility", Icons.Default.Receipt, 0xFFFF9800)
    
    val all = listOf(STAFF, TRAVEL, FOOD, UTILITY)
    
    // Legacy categories (kept for backward compatibility if needed)
    val TRANSPORT = Category("transport", "Transportation", Icons.Default.DirectionsCar, 0xFF2196F3)
    val SHOPPING = Category("shopping", "Shopping", Icons.Default.ShoppingCart, 0xFFE91E63)
    val ENTERTAINMENT = Category("entertainment", "Entertainment", Icons.Default.Movie, 0xFF9C27B0)
    val HEALTH = Category("health", "Health & Fitness", Icons.Default.LocalHospital, 0xFF4CAF50)
    val EDUCATION = Category("education", "Education", Icons.Default.School, 0xFF3F51B5)
    val OTHER = Category("other", "Other", Icons.Default.Category, 0xFF607D8B)
} 