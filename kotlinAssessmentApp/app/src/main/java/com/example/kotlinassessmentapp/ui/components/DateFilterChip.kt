package com.example.kotlinassessmentapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kotlinassessmentapp.ui.viewmodel.DateFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateFilterChips(
    selectedFilter: DateFilter,
    onFilterSelected: (DateFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    val filters = listOf(
        DateFilter.TODAY to "Today",
        DateFilter.THIS_WEEK to "This Week",
        DateFilter.THIS_MONTH to "This Month",
        DateFilter.ALL_TIME to "All Time"
    )
    
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(filters) { (filter, label) ->
            FilterChip(
                onClick = { onFilterSelected(filter) },
                label = { Text(label) },
                selected = selectedFilter == filter,
                modifier = Modifier.height(32.dp)
            )
        }
    }
}
