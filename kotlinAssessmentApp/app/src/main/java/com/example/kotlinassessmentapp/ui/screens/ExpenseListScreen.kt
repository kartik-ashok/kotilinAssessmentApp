package com.example.kotlinassessmentapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinassessmentapp.ui.components.*
import com.example.kotlinassessmentapp.ui.viewmodel.*
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
/**
 * ExpenseListScreen following Enterprise UI Patterns
 * 
 * Features:
 * - Default: Show today's expenses
 * - Date filtering with calendar picker
 * - Group by Category or Time toggle
 * - Total count and amount display
 * - Empty state handling
 * - Scrollable list with detailed expense items
 * - Proper MVVM flow implementation
 */
@Composable
fun ExpenseListScreen(
    onBackClick: () -> Unit,
    expenseViewModel: ExpenseViewModel = viewModel()
) {
    val uiState by expenseViewModel.uiState.collectAsState()
    var showFilters by remember { mutableStateOf(false) }
    
    // Initialize with today's expenses
    LaunchedEffect(Unit) {
        expenseViewModel.setDateFilter(DateFilter.TODAY)
    }
    
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
            
            Text(
                text = "All Expenses",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            
            IconButton(onClick = { showFilters = !showFilters }) {
                Icon(
                    Icons.Default.FilterList,
                    contentDescription = "Toggle Filters"
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Summary Card
        ExpenseSummaryCard(
            totalAmount = uiState.totalAmount,
            expenseCount = uiState.expenseCount,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Filters Section
        if (showFilters) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Date Filter Chips
                    Text(
                        text = "Filter by Date",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    DateFilterChips(
                        selectedFilter = uiState.dateFilter,
                        onFilterSelected = { filter ->
                            expenseViewModel.setDateFilter(filter)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    // Custom Date Range Picker
                    if (uiState.dateFilter == DateFilter.CUSTOM_RANGE) {
                        DateRangePicker(
                            startDate = uiState.customStartDate,
                            endDate = uiState.customEndDate,
                            onDateRangeSelected = { start, end ->
                                expenseViewModel.setCustomDateRange(start, end)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    
                    // Group Toggle Buttons
                    Text(
                        text = "Group by",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    GroupToggleButtons(
                        selectedGroup = uiState.groupBy,
                        onGroupSelected = { group ->
                            expenseViewModel.setGroupBy(group)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Loading State
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        
        // Expenses List
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (uiState.groupBy == GroupBy.NONE) {
                // Ungrouped list
                items(uiState.expenses) { expense ->
                    ExpenseItem(
                        expense = expense,
                        onDeleteClick = { expenseViewModel.deleteExpense(expense.id) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                // Grouped list
                uiState.groupedExpenses.forEach { (groupName, expenses) ->
                    item {
                        // Group Header
                        Text(
                            text = groupName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    
                    items(expenses) { expense ->
                        ExpenseItem(
                            expense = expense,
                            onDeleteClick = { expenseViewModel.deleteExpense(expense.id) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            
            // Empty State
            if (uiState.expenses.isEmpty() && !uiState.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "No expenses found",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Try adjusting your filters or add some expenses",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }
    }
}
