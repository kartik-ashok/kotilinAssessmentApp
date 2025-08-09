package com.example.kotlinassessmentapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kotlinassessmentapp.data.model.Expense
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseItem(
    expense: Expense,
    onDeleteClick: () -> Unit,
    onItemClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onItemClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = Color(expense.category.color).copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = expense.category.icon,
                    contentDescription = expense.category.name,
                    tint = Color(expense.category.color),
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Expense Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = expense.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Text(
                    text = expense.category.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                if (expense.description.isNotBlank()) {
                    Text(
                        text = expense.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
                
                // Date and Time
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = expense.date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )

                    Text(
                        text = "•",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )

                    Text(
                        text = expense.date.format(DateTimeFormatter.ofPattern("HH:mm")),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Amount and Delete
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = formatCurrency(expense.amount),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete expense",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

private fun formatCurrency(amount: Double): String {
    return "₹${String.format("%.2f", amount)}"
}