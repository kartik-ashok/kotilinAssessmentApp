package com.example.kotlinassessmentapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.kotlinassessmentapp.R
import com.example.kotlinassessmentapp.ui.theme.AppTypography
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    // Delay and navigate after 5 seconds
    LaunchedEffect(Unit) {
        delay(5000) // Wait for 5 seconds
        navController.navigate("home") {
            popUpTo("splash") { inclusive = true } // Clear splash from backstack
        }
    }
    val typography = AppTypography()



    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        colorResource(id = R.color.primaryColor),
                        colorResource(id = R.color.secondaryColor),

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
//                 horizontalArrangement = Arrangement.Center, // Not strictly needed if Column is already centered
                // modifier = Modifier.fillMaxWidth() // May not be needed if Column handles width
            ) {
                Text(
                    text = "Travel",
                    style = typography.titleLarge
                )
                Spacer(modifier = Modifier.width(8.dp)) // Space between text and icon
                Image(
                    modifier = Modifier.size(height = 37.6.dp, width = 37.dp),
                    painter = painterResource(id = R.drawable.globe_icon),
                    contentDescription = "Globe Icon"
                )
            }
            Spacer(modifier.height(10.dp))

            Text(
                text = "Find your dream",
                style = typography.labelSmall
            )
            Text(
                text = "Destination with us",
                style = typography.labelSmall
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun SplashScreenPreview() {
//    SplashScreen()
//}
