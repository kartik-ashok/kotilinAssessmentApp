plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

}

android {
    namespace = "com.example.kotlinassessmentapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.kotlinassessmentapp"
        minSdk = 21
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}







dependencies {

    // Core Android Libraries
    implementation(libs.androidx.core.ktx)
    // Provides Kotlin extensions for Android core APIs, making code more concise and idiomatic

    implementation(libs.androidx.lifecycle.runtime.ktx)
    // Provides lifecycle-aware components and Kotlin coroutines support for managing app lifecycle

    implementation(libs.androidx.activity.compose)
    // Enables integration between Activity and Jetpack Compose, provides setContent {} function

    // Jetpack Compose BOM (Bill of Materials)
    implementation(platform(libs.androidx.compose.bom))
    // Ensures all Compose libraries use compatible versions, prevents version conflicts

    // Core Compose UI Libraries
    implementation(libs.androidx.ui)
    // Core Compose UI toolkit for building user interfaces declaratively

    implementation(libs.androidx.ui.graphics)
    // Provides graphics primitives and utilities for Compose (Canvas, Paint, etc.)

    implementation(libs.androidx.ui.tooling.preview)
    // Enables @Preview annotations for previewing Composables in Android Studio

    implementation(libs.androidx.material3)
    // Material Design 3 components for Compose (buttons, cards, text fields, etc.)

    // MVVM Architecture Dependencies
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.1")
    // Integrates ViewModel with Compose, provides viewModel() function and lifecycle-aware state

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.1")
    // Provides ViewModel with Kotlin coroutines support (viewModelScope)

    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.9.1")
    // Enables lifecycle-aware state management in Compose (collectAsStateWithLifecycle)

    // Testing Dependencies
    testImplementation(libs.junit)
    // Unit testing framework for testing business logic and ViewModels

    androidTestImplementation(libs.androidx.junit)
    // AndroidX version of JUnit for instrumented tests that run on device/emulator

    androidTestImplementation(libs.androidx.espresso.core)
    // UI testing framework for testing user interactions and UI behavior

    androidTestImplementation(platform(libs.androidx.compose.bom))
    // Ensures test dependencies use same Compose versions as main code

    androidTestImplementation(libs.androidx.ui.test.junit4)
    // Testing utilities for Compose UI components (onNodeWithText, performClick, etc.)

    // Debug-only Dependencies
    debugImplementation(libs.androidx.ui.tooling)
    // Provides debugging tools for Compose (layout inspector, recomposition counts)

    debugImplementation(libs.androidx.ui.test.manifest)
    // Enables testing of Compose UI components in debug builds

    // Additional UI and UX Libraries
    implementation("androidx.core:core-splashscreen:1.0.0")
    // Provides modern splash screen API following Android 12+ guidelines

    implementation("com.google.android.material:material:1.12.0")
    // Material Design components for traditional Android Views (used for theme colors)

    implementation("androidx.navigation:navigation-compose:2.7.5")
    // Navigation component for Compose, enables screen-to-screen navigation with type safety

    implementation("androidx.compose.ui:ui-text-google-fonts:1.8.1")
    // Provides Google Fonts integration for Compose text (downloadable fonts)

    implementation("androidx.compose.material:material-icons-extended")
    // Extended set of Material Design icons for Compose (beyond the core set)

    implementation("androidx.compose.material3:material3:1.2.0")
    // Additional Material 3 components and theming capabilities (explicit version for consistency)

}