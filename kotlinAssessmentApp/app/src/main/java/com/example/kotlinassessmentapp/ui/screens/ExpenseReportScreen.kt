package com.example.kotlinassessmentapp.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinassessmentapp.data.repository.ExpenseRepository
import com.example.kotlinassessmentapp.ui.components.*
import com.example.kotlinassessmentapp.ui.viewmodel.ReportViewModel

@OptIn(ExperimentalMaterial3Api::class)
/**
 * ExpenseReportScreen following Enterprise Reporting Patterns
 * 
 * Features:
 * - Mock data for last 7 days
 * - Daily totals visualization (bar chart)
 * - Category-wise totals (list with percentages)
 * - Export functionality (PDF/CSV simulation)
 * - Share intent integration
 * - Proper MVVM flow implementation
 */
@Composable
fun ExpenseReportScreen(
    onBackClick: () -> Unit,
    reportViewModel: ReportViewModel = viewModel()
) {
    val uiState by reportViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val repository = ExpenseRepository.getInstance()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
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
                text = "Expense Report",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            
            // Export Menu
            var showExportMenu by remember { mutableStateOf(false) }
            
            Box {
                IconButton(onClick = { showExportMenu = true }) {
                    Icon(
                        Icons.Default.Share,
                        contentDescription = "Export & Share"
                    )
                }
                
                DropdownMenu(
                    expanded = showExportMenu,
                    onDismissRequest = { showExportMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Export as PDF") },
                        onClick = {
                            showExportMenu = false
                            val fileName = repository.generateReportPDF()
                            Toast.makeText(context, "PDF exported: $fileName", Toast.LENGTH_SHORT).show()
                        },
                        leadingIcon = {
                            Icon(Icons.Default.PictureAsPdf, contentDescription = null)
                        }
                    )
                    
                    DropdownMenuItem(
                        text = { Text("Export as CSV") },
                        onClick = {
                            showExportMenu = false
                            val fileName = repository.generateReportCSV()
                            Toast.makeText(context, "CSV exported: $fileName", Toast.LENGTH_SHORT).show()
                        },
                        leadingIcon = {
                            Icon(Icons.Default.TableChart, contentDescription = null)
                        }
                    )
                    
                    DropdownMenuItem(
                        text = { Text("Share Report") },
                        onClick = {
                            showExportMenu = false
                            val reportData = repository.getShareableReportData()
                            val shareIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, reportData)
                                putExtra(Intent.EXTRA_SUBJECT, "Expense Report")
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Report"))
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Share, contentDescription = null)
                        }
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Summary Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Last 7 Days Summary",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "₹${String.format("%.2f", uiState.last7DaysTotal)}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                val totalExpenses = uiState.dailyExpenses.sumOf { it.expenseCount }
                Text(
                    text = "$totalExpenses ${if (totalExpenses == 1) "expense" else "expenses"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Loading State
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // Daily Expense Chart
            DailyExpenseChart(
                dailyData = uiState.dailyExpenses,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Category Expense Chart
            CategoryExpenseChart(
                categoryData = uiState.categoryExpenses,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Export Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        val fileName = repository.generateReportPDF()
                        Toast.makeText(context, "PDF exported: $fileName", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.PictureAsPdf,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Export PDF")
                }
                
                OutlinedButton(
                    onClick = {
                        val fileName = repository.generateReportCSV()
                        Toast.makeText(context, "CSV exported: $fileName", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.TableChart,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Export CSV")
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Share Button
            Button(
                onClick = {
                    val reportData = repository.getShareableReportData()
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, reportData)
                        putExtra(Intent.EXTRA_SUBJECT, "Expense Report")
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share Report"))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Default.Share,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Share Report")
            }
        }
        
        // Error State
        uiState.errorMessage?.let { error ->
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = error,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}
