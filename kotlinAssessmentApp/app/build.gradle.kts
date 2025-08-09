plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    
    // TEMPORARILY DISABLED - Dependency Injection with Hilt
    // Will be re-enabled once version compatibility is resolved
    // id("com.google.dagger.hilt.android") version "2.50"
    // id("com.google.devtools.ksp") version "2.0.21-1.0.25"

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

    // Core Android Libraries - ESSENTIAL
    implementation(libs.androidx.core.ktx)
    // Kotlin extensions for Android APIs - REQUIRED for modern Android development

    implementation(libs.androidx.lifecycle.runtime.ktx)
    // Lifecycle-aware components with coroutines - REQUIRED for MVVM pattern

    implementation(libs.androidx.activity.compose)
    // Activity-Compose integration - REQUIRED for Compose apps

    // Jetpack Compose BOM - ESSENTIAL
    implementation(platform(libs.androidx.compose.bom))
    // Version alignment for all Compose libraries - PREVENTS dependency conflicts

    // Core Compose UI Libraries - ESSENTIAL
    implementation(libs.androidx.ui)
    // Core Compose UI toolkit - REQUIRED for declarative UI

    implementation(libs.androidx.ui.graphics)
    // Graphics primitives - NEEDED for custom icons and drawing

    implementation(libs.androidx.ui.tooling.preview)
    // @Preview support - ESSENTIAL for development productivity

    implementation(libs.androidx.material3)
    // Material 3 components - REQUIRED for modern UI design

    // MVVM Architecture Dependencies - ESSENTIAL for Enterprise Pattern
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.1")
    // ViewModel-Compose integration - REQUIRED for MVVM with Compose

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.1")
    // ViewModel with coroutines - REQUIRED for async operations in ViewModels

    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.9.1")
    // Lifecycle-aware Compose state - REQUIRED for reactive UI updates

    // TEMPORARILY DISABLED - Dependency Injection with Hilt
    // These will be re-enabled once version compatibility is resolved
    // implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    // implementation("com.google.dagger:hilt-android:2.50")
    // ksp("com.google.dagger:hilt-compiler:2.50")
    
    // Testing Dependencies - ESSENTIAL for Quality Assurance
    testImplementation(libs.junit)
    // Unit testing - REQUIRED for TDD/BDD practices

    testImplementation("org.mockito:mockito-core:5.7.0")
    // Mocking framework - REQUIRED for isolated unit tests

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    // Coroutines testing - REQUIRED for testing async code

    androidTestImplementation(libs.androidx.junit)
    // Android JUnit - REQUIRED for instrumented tests

    androidTestImplementation(libs.androidx.espresso.core)
    // UI testing framework - REQUIRED for E2E testing

    androidTestImplementation(platform(libs.androidx.compose.bom))
    // Test version alignment - PREVENTS test dependency conflicts

    androidTestImplementation(libs.androidx.ui.test.junit4)
    // Compose UI testing - REQUIRED for Compose component testing

    // Debug Tools - DEVELOPMENT PRODUCTIVITY
    debugImplementation(libs.androidx.ui.tooling)
    // Compose debugging tools - ESSENTIAL for development

    debugImplementation(libs.androidx.ui.test.manifest)
    // Test manifest for debug builds - REQUIRED for UI testing

    // Navigation - ESSENTIAL for Multi-Screen Apps
    implementation("androidx.navigation:navigation-compose:2.7.5")
    // Type-safe navigation - REQUIRED for screen navigation

    // Icons - ESSENTIAL for User Experience
    implementation("androidx.compose.material:material-icons-extended")
    // Extended icon set - REQUIRED for category icons and UI elements

    // Theme Support - REQUIRED even for pure Compose apps
    implementation("com.google.android.material:material:1.12.0")
    // REASON: Needed for Android theme system and Activity themes
    // NOTE: Even pure Compose apps need this for proper theme inheritance
    // ENTERPRISE PATTERN: All major apps include this for theme compatibility

    // COMMENTED OUT - UNNECESSARY DEPENDENCIES
    // implementation("androidx.core:core-splashscreen:1.0.0")
    // REASON: Not needed for basic expense tracker, adds unnecessary complexity
    // ALTERNATIVE: Use simple Compose splash screen if needed

    // implementation("androidx.compose.ui:ui-text-google-fonts:1.8.1")
    // REASON: Default system fonts are sufficient for MVP, adds download overhead
    // ALTERNATIVE: Use system fonts or add later if custom fonts are required

    // implementation("androidx.compose.material3:material3:1.2.0")
    // REASON: Duplicate dependency - already included via libs.androidx.material3
    // ALTERNATIVE: Use the BOM-managed version for consistency

}