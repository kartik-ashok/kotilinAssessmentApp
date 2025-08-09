package com.example.kotlinassessmentapp

import android.app.Application
import com.example.kotlinassessmentapp.data.database.ExpenseDatabase
import com.example.kotlinassessmentapp.data.repository.ExpenseRepository

/**
 * Application class following enterprise patterns
 * 
 * TEMPORARILY removed Hilt until version compatibility is resolved
 * This is the STANDARD pattern used by companies like Google, Netflix, Airbnb for Android apps
 * 
 * Benefits:
 * - Centralized app initialization
 * - Better lifecycle management
 * - Easy integration with crash reporting and analytics
 * - Foundation for dependency injection when re-enabled
 */
class ExpenseTrackerApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()

        // Initialize Room database
        ExpenseDatabase.getDatabase(this)

        // Initialize repository with application context
        ExpenseRepository.getInstance(this)

        // Application initialization
        // This is where you would initialize:
        // - Crash reporting (Firebase Crashlytics, Bugsnag)
        // - Analytics (Firebase Analytics, Mixpanel)
        // - Performance monitoring (Firebase Performance)
        // - Feature flags (Firebase Remote Config, LaunchDarkly)

        // Example for enterprise apps:
        // FirebaseApp.initializeApp(this)
        // Crashlytics.getInstance().core.setCrashlyticsCollectionEnabled(true)

        // TODO: Re-enable Hilt when version compatibility is resolved
        // @HiltAndroidApp annotation will be added back
    }
} 