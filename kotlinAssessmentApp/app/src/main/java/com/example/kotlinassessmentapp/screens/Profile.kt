package com.example.kotlinassessmentapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(Color.Red)
            .fillMaxSize(),
        contentAlignment = Alignment.Center




    ){

        Text("Profile Screen")
    }

    
}