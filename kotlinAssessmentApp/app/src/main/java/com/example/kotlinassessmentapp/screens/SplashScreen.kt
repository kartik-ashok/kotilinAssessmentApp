package com.example.kotlinassessmentapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen
import androidx.navigation.NavHostController
import com.example.kotlinassessmentapp.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController, modifier: Modifier = Modifier)
{
    LaunchedEffect(true) {
        delay(2000) // Wait for 2 seconds
        navController.navigate("welcome") {
            popUpTo("splash") { inclusive = true }
        }
    }


    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(R.drawable.shopping_basket),
            contentDescription = null,
            modifier.size(184.dp, 206.dp),
        )
    }

}

