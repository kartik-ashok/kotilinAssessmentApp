package com.example.kotlinassessmentapp.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlinassessmentapp.data.model.Category
import kotlin.math.cos
import kotlin.math.sin

/**
 * Animated Pie Chart for Expense Categories
 * 
 * Features:
 * - Smooth animations
 * - Category breakdown
 * - Interactive legend
 * - Professional styling
 */

data class PieChartData(
    val category: Category,
    val amount: Double,
    val percentage: Float,
    val color: Color
)

@Composable
fun ExpensePieChart(
    data: List<PieChartData>,
    modifier: Modifier = Modifier,
    animationDuration: Int = 1000
) {
    var animationPlayed by remember { mutableStateOf(false) }
    val animationProgress = remember { Animatable(0f) }
    
    // Start animation
    LaunchedEffect(data) {
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(animationDuration)
        )
        animationPlayed = true
    }
    
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Expense Breakdown",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            if (data.isEmpty()) {
                // Empty state
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No expenses to display",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Pie Chart
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(
                            modifier = Modifier.size(180.dp)
                        ) {
                            drawPieChart(data, animationProgress.value)
                        }
                        
                        // Center text with total
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Total",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "₹${String.format("%.0f", data.sumOf { it.amount })}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    
                    // Legend
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(data) { item ->
                            PieChartLegendItem(
                                data = item,
                                animationProgress = animationProgress.value
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PieChartLegendItem(
    data: PieChartData,
    animationProgress: Float
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Color indicator
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(data.color)
        )
        
        // Category info
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = data.category.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${String.format("%.1f", data.percentage * animationProgress)}%",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        // Amount
        Text(
            text = "₹${String.format("%.0f", data.amount * animationProgress)}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private fun DrawScope.drawPieChart(
    data: List<PieChartData>,
    animationProgress: Float
) {
    val center = androidx.compose.ui.geometry.Offset(size.width / 2, size.height / 2)
    val radius = size.minDimension / 2 * 0.8f
    val strokeWidth = 40.dp.toPx()
    
    var startAngle = -90f // Start from top
    
    data.forEach { item ->
        val sweepAngle = 360f * item.percentage * animationProgress
        
        // Draw pie slice
        drawArc(
            color = item.color,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(width = strokeWidth),
            size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
            topLeft = androidx.compose.ui.geometry.Offset(
                center.x - radius,
                center.y - radius
            )
        )
        
        startAngle += sweepAngle
    }
}

/**
 * Helper function to generate pie chart data from expenses
 */
fun generatePieChartData(expenses: List<com.example.kotlinassessmentapp.data.model.Expense>): List<PieChartData> {
    if (expenses.isEmpty()) return emptyList()
    
    val categoryTotals = expenses.groupBy { it.category }
        .mapValues { (_, expenses) -> expenses.sumOf { it.amount } }
    
    val totalAmount = categoryTotals.values.sum()
    
    return categoryTotals.map { (category, amount) ->
        PieChartData(
            category = category,
            amount = amount,
            percentage = (amount / totalAmount).toFloat(),
            color = Color(category.color)
        )
    }.sortedByDescending { it.amount }
}
