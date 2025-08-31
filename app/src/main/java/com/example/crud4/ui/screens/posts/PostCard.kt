package com.example.crud4.ui.screens.posts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.Navigator
import com.example.crud4.data.model.Post


@Composable
fun PostCard(
    post: Post,
    onUpdateClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onNavigatorClick: ()->Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Post Title
            Text(
                text = post.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Post Body
            Text(
                text = post.body,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 12.dp),
                maxLines = 3
            )

            // Post ID and User ID
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Post ID: ${post.id}",
                    fontSize = 12.sp
                )
                Text(
                    text = "User ID: ${post.userId}",
                    fontSize = 12.sp
                )
            }

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = onUpdateClick,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Update")
                }

                OutlinedButton(
                    onClick = onDeleteClick
                ) {
                    Text("Delete")
                }
                OutlinedButton(
                    onClick = onNavigatorClick
                ) {
                    Text("Next")
                }
            }
        }
    }
}