package com.example.crud4.ui.navigation
sealed class NavRoutes(val route: String) {
    object PostScreen : NavRoutes("post_screen")
    object MyPage : NavRoutes("my_page")
}
