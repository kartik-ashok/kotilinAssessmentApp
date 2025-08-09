package com.example.kotlinassessmentapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePicker(
    startDate: LocalDate?,
    endDate: LocalDate?,
    onDateRangeSelected: (LocalDate?, LocalDate?) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var isSelectingStartDate by remember { mutableStateOf(true) }
    
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Start Date Button
        OutlinedButton(
            onClick = { 
                isSelectingStartDate = true
                showDatePicker = true 
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                Icons.Default.DateRange,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = startDate?.format(DateTimeFormatter.ofPattern("MMM dd")) ?: "Start Date"
            )
        }
        
        Text(
            text = "to",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        // End Date Button
        OutlinedButton(
            onClick = { 
                isSelectingStartDate = false
                showDatePicker = true 
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                Icons.Default.DateRange,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = endDate?.format(DateTimeFormatter.ofPattern("MMM dd")) ?: "End Date"
            )
        }
        
        // Clear Button
        if (startDate != null || endDate != null) {
            TextButton(
                onClick = { onDateRangeSelected(null, null) }
            ) {
                Text("Clear")
            }
        }
    }
    
    // Date Picker Dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        
        DatePickerDialog(
            onDateSelected = { selectedDateMillis ->
                selectedDateMillis?.let { millis ->
                    val selectedDate = LocalDate.ofEpochDay(millis / (24 * 60 * 60 * 1000))
                    if (isSelectingStartDate) {
                        onDateRangeSelected(selectedDate, endDate)
                    } else {
                        onDateRangeSelected(startDate, selectedDate)
                    }
                }
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerDialog(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onDateSelected(datePickerState.selectedDateMillis) }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        text = {
            DatePicker(state = datePickerState)
        }
    )
}
