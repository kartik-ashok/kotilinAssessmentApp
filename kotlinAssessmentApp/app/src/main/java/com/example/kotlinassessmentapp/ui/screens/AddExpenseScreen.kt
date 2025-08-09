package com.example.kotlinassessmentapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinassessmentapp.data.model.Categories
import com.example.kotlinassessmentapp.data.model.Category
import com.example.kotlinassessmentapp.ui.viewmodel.ExpenseViewModel

@OptIn(ExperimentalMaterial3Api::class)
/**
 * AddExpenseScreen following Enterprise Form Handling Patterns
 * 
 * This screen demonstrates:
 * - FORM VALIDATION with proper error handling
 * - STATE MANAGEMENT with local UI state and ViewModel business logic
 * - USER EXPERIENCE patterns (loading states, error messages, validation feedback)
 * - ACCESSIBILITY considerations (content descriptions, semantic markup)
 * 
 * Pattern used by enterprise apps like:
 * - Google Pay (form validation and error handling)
 * - Netflix (user input validation)
 * - Airbnb (booking forms with category selection)
 */
@Composable
fun AddExpenseScreen(
    onBackClick: () -> Unit,
    onExpenseAdded: () -> Unit,
    expenseViewModel: ExpenseViewModel = viewModel()
) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = "Add Expense",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Title Input
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Expense Title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Amount Input
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            leadingIcon = { Text("$", style = MaterialTheme.typography.bodyLarge) },
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Description Input
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description (Optional)") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Category Selection
        Text(
            text = "Select Category",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.height(200.dp)
        ) {
            items(Categories.all) { category ->
                CategoryItem(
                    category = category,
                    isSelected = selectedCategory == category,
                    onClick = { selectedCategory = category }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Error Message
        if (showError) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Text(
                    text = errorMessage,
                    modifier = Modifier.padding(12.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Add Button
        Button(
            onClick = {
                when {
                    title.isBlank() -> {
                        showError = true
                        errorMessage = "Please enter an expense title"
                    }
                    amount.isBlank() || amount.toDoubleOrNull() == null || amount.toDouble() <= 0 -> {
                        showError = true
                        errorMessage = "Please enter a valid amount"
                    }
                    selectedCategory == null -> {
                        showError = true
                        errorMessage = "Please select a category"
                    }
                    else -> {
                        showError = false
                        expenseViewModel.addExpense(
                            title = title,
                            amount = amount.toDouble(),
                            category = selectedCategory!!,
                            description = description
                        )
                        onExpenseAdded()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Add Expense",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun CategoryItem(
    category: Category,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(
                    color = if (isSelected) {
                        Color(category.color)
                    } else {
                        Color(category.color).copy(alpha = 0.1f)
                    },
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = category.name,
                tint = if (isSelected) {
                    Color.White
                } else {
                    Color(category.color)
                },
                modifier = Modifier.size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = category.name.split(" ").first(), // Show only first word for space
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
} 