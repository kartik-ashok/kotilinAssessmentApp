<<<<<<< HEAD

=======
>>>>>>> 3f36c4f7dd7f47ced76ba2aa04eaaaeea6777ad9
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
<<<<<<< HEAD
    namespace = "com.example.crud4"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.crud4"
        minSdk = 21
=======
    namespace = "com.example.newsapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.newsapp"
        minSdk = 24
>>>>>>> 3f36c4f7dd7f47ced76ba2aa04eaaaeea6777ad9
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
<<<<<<< HEAD
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
}

dependencies {
    // Core Android dependencies
=======
}

dependencies {
    implementation("androidx.core:core-splashscreen:1.0.1")
    // Material 3 support
    implementation("androidx.compose.material3:material3")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.27.0")
>>>>>>> 3f36c4f7dd7f47ced76ba2aa04eaaaeea6777ad9
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
<<<<<<< HEAD
    
    // Retrofit - HTTP client for making API calls to JSONPlaceholder
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.gson)
    
    // OkHttp - HTTP client that Retrofit uses underneath, with logging for debugging
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    
    // Gson - JSON serialization/deserialization for API responses
    implementation(libs.gson)
    
    // Kotlin Coroutines - For asynchronous programming and background operations
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    
    // Lifecycle components - Core MVVM architecture components
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)
    implementation(libs.lifecycle.runtime)
    
    // Compose ViewModel integration
//    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // Navigation
    implementation(libs.navigation.compose)



    // Testing dependencies
=======
>>>>>>> 3f36c4f7dd7f47ced76ba2aa04eaaaeea6777ad9
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}