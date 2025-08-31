package com.example.crud4.ui.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.crud4.ui.screens.MyPage
import com.example.crud4.ui.screens.posts.PostsScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavRoutes.PostScreen.route) {
        composable(NavRoutes.PostScreen.route) { PostsScreen(navController) }
        composable(NavRoutes.MyPage.route) { MyPage(navController) }
    }
}
