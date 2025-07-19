package com.example.kotlinassessmentapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun Setting(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize().background(color = Color.Green)

    ){
        Text("Setting Screen")
    }


}