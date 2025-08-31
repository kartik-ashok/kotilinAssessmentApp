package com.example.crud4

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.crud4.data.model.Post
import com.example.crud4.ui.navigation.AppNavGraph
import com.example.crud4.ui.screens.posts.PostsScreen
import com.example.crud4.ui.theme.Crud4Theme
import com.example.crud4.ui.viewmodel.PostViewModel

/**
 * MainActivity that demonstrates MVVM architecture with JSONPlaceholder API integration using Jetpack Compose
 * 
 * MVVM (Model-View-ViewModel) Architecture:
 * - Model: Post data classes and Repository layer
 * - View: Compose UI components
 * - ViewModel: PostViewModel that manages UI state and business logic
 * 
 * This architecture provides:
 * - Separation of concerns (UI logic vs business logic)
 * - Lifecycle awareness (survives configuration changes)
 * - Testability (ViewModel can be tested independently)
 * - Data consistency (single source of truth)
 */
class MainActivity : ComponentActivity() {
    override fun onStart() {
        super.onStart()
        print("-----------1-----------");
        Log.d("ActivityLifecycle", "onStart")
        print("-----------1-----------");

    }

    override fun onResume() {
        super.onResume()
        print("-----------2-----------");
        Log.d("ActivityLifecycle", "onResume")
        print("-----------2-----------");

    }

    override fun onPause() {
        super.onPause()
        print("-----------3-----------");
        Log.d("ActivityLifecycle", "onPause")
        print("-----------3-----------");


    }

    override fun onStop() {
        super.onStop()
        print("-----------4-----------");
        Log.d("ActivityLifecycle", "onStop")
        print("-----------4-----------");

    }

    override fun onDestroy() {
        super.onDestroy()
        print("-----------5-----------");
        Log.d("ActivityLifecycle", "onDestroy")
        print("-----------5-----------");

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        print("-----------6-----------");
        Log.d("ActivityLifecycle", "onDestroy")
        print("-----------6-----------");


        enableEdgeToEdge()
        setContent {
            Crud4Theme {
                val navController = rememberNavController() // ✅ create instance
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    PostsScreen(navController = navController)
                    // ✅ Use NavHost instead of just one screen
                    AppNavGraph(navController)

                }
            }
        }
    }
}
