package com.example.kotlinassessmentapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kotlinassessmentapp.R
import com.example.kotlinassessmentapp.ui.theme.AppTypography

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    val typography = AppTypography()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.Blue.copy(alpha = 0.5f),
                        Color.Blue
                    )
                )
            ),
        contentAlignment = Alignment.Center // Center content within the Box
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                12.dp,
                Alignment.CenterVertically // Also center vertically within the column if needed
            ),
            // modifier = Modifier.background(Color.Red) // Keep for debugging if needed
        ) {
            // Centered Row with text and image
            Row(
                verticalAlignment = Alignment.CenterVertically,
                // horizontalArrangement = Arrangement.Center, // Not strictly needed if Column is already centered
                // modifier = Modifier.fillMaxWidth() // May not be needed if Column handles width
            ) {
                Text(
                    text = "Splash Screen",
                    style = typography.titleLarge
                )
                Spacer(modifier = Modifier.width(8.dp)) // Space between text and icon
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.globe_icon),
                    contentDescription = "Globe Icon"
                )
            }

            Text(
                text = "Find your dream",
                style = typography.titleLarge
            )
            Text(
                text = "Destination with us",
                style = typography.titleLarge
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}
