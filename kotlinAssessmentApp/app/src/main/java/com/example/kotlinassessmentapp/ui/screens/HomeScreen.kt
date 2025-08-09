package com.example.kotlinassessmentapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinassessmentapp.data.model.Expense
import com.example.kotlinassessmentapp.ui.components.ExpenseItem
import com.example.kotlinassessmentapp.ui.components.ExpenseSummaryCard
import com.example.kotlinassessmentapp.ui.viewmodel.ExpenseViewModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
/**
 * HomeScreen following Modern Enterprise UI Patterns
 * 
 * Uses hiltViewModel() for dependency injection instead of manual instantiation
 * This pattern ensures proper dependency management and testability
 * 
 * Enterprise UI patterns used:
 * - SINGLE SOURCE OF TRUTH (ViewModel holds all UI state)
 * - UNIDIRECTIONAL DATA FLOW (events up, state down)
 * - REACTIVE UI (UI recomposes when state changes)
 * - SEPARATION OF CONCERNS (UI logic separate from business logic)
 */
@Composable
fun HomeScreen(
    onAddExpenseClick: () -> Unit,
    onViewAllExpensesClick: () -> Unit = {},
    onViewReportsClick: () -> Unit = {},
    expenseViewModel: ExpenseViewModel = viewModel(),
    themeViewModel: com.example.kotlinassessmentapp.ui.theme.ThemeViewModel? = null
) {
    val uiState by expenseViewModel.uiState.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Expense Tracker",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Theme Toggle Button
                themeViewModel?.let { viewModel ->
                    IconButton(
                        onClick = { viewModel.toggleTheme() }
                    ) {
                        Icon(
                            Icons.Default.DarkMode,
                            contentDescription = "Toggle Theme"
                        )
                    }
                }

                FloatingActionButton(
                    onClick = onAddExpenseClick,
                    modifier = Modifier.size(48.dp),
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add Expense",
                        tint = Color.White
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Summary Card
        ExpenseSummaryCard(
            totalAmount = uiState.totalAmount,
            expenseCount = uiState.expenses.size,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        // Quick Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onViewAllExpensesClick,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.List,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("All Expenses")
            }

            OutlinedButton(
                onClick = onViewReportsClick,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    Icons.Default.Analytics,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Reports")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Recent Expenses Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recent Expenses",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            
            TextButton(onClick = onViewAllExpensesClick) {
                Text("View All")
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Loading state
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        
        // Error state
        uiState.errorMessage?.let { error ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Text(
                    text = error,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
        
        // Expenses List
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.expenses.take(10)) { expense ->
                ExpenseItem(
                    expense = expense,
                    onDeleteClick = { expenseViewModel.deleteExpense(expense.id) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            if (uiState.expenses.isEmpty() && !uiState.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No expenses yet. Add your first expense!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
} 