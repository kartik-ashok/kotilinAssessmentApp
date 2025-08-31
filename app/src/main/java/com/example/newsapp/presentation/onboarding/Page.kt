package com.example.newsapp.presentation.onboarding

import androidx.annotation.DrawableRes
import com.example.newsapp.R

data class Page(
    val title: String,
    val description: String,
    @DrawableRes val imageId: Int,
)

val pages = listOf(
    Page(
        title = "Stay Informed",
        description = "Get the latest news from around the world delivered directly to your fingertips.",
        imageId = R.drawable.onboarding_screen1
    ),
    Page(
        title = "Personalized News",
        description = "Customize your feed to receive news that matters to you most.",
        imageId = R.drawable.onboarding_screen1
    ),
    Page(
        title = "Breaking Alerts",
        description = "Receive instant notifications for breaking news and top headlines.",
        imageId = R.drawable.onboarding_screen1
    ),
    Page(
        title = "Offline Reading",
        description = "Save articles and read them later without an internet connection.",
        imageId = R.drawable.onboarding_screen1
    )
)
