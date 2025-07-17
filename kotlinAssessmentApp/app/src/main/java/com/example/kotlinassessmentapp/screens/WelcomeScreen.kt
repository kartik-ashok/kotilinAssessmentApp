package com.example.kotlinassessmentapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.kotlinassessmentapp.R
@Composable
fun WelcomeScreen(navController: NavHostController,modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.primaryColor))
    ) {
        // Top Part (Image inside gradient Box)
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFFF8C42), Color(0xFFFF7A2B))
                    )
                )
                .fillMaxWidth()
                .weight(0.65f), // ✅ This is valid now — used inside a Column
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .background(
                        color = colorResource(R.color.white).copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.fruit_basket_image),
                    contentDescription = null,
                    modifier = Modifier.size(160.dp) // ✅ fixed here
                )
            }
        }

        // Bottom Part (Text & Button)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.35f) // ✅ valid here (inside Column)
                .background(color = Color.White.copy(alpha = 0.8f))
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Get the Freshest Fruit sald combo",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF272140),
                textAlign = TextAlign.Center,
                lineHeight = 28.sp
            )
            Spacer(modifier = Modifier.height(16.dp)) // ✅ fixed
            Text(
                "Get the Freshest Fruit sald combo",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF272140),
                textAlign = TextAlign.Center,
                lineHeight = 28.sp
            )
            Spacer(modifier = Modifier.height(16.dp)) // ✅ fixed

            Button(
                onClick = {
                    navController.navigate("home")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF8C42) // ✅ corrected: contentColor is for text
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    "Let's Connect",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp, // ✅ should be sp not dp
                    color = Color.White
                )
            }
        }
    }
}

