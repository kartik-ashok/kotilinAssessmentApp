package com.example.kotlinassessmentapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.kotlinassessmentapp.ui.viewmodel.GroupBy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupToggleButtons(
    selectedGroup: GroupBy,
    onGroupSelected: (GroupBy) -> Unit,
    modifier: Modifier = Modifier
) {
    val groups = listOf(
        GroupBy.NONE to ("None" to Icons.Default.List),
        GroupBy.CATEGORY to ("Category" to Icons.Default.Category),
        GroupBy.TIME to ("Time" to Icons.Default.Schedule)
    )
    
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        groups.forEach { (group, labelIcon) ->
            val (label, icon) = labelIcon
            FilterChip(
                onClick = { onGroupSelected(group) },
                label = { 
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(label)
                    }
                },
                selected = selectedGroup == group,
                modifier = Modifier.height(32.dp)
            )
        }
    }
}
