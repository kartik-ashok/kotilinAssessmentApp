package com.example.crud4.data.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Network module that provides Retrofit configuration and API service instances
 * This is part of the data layer in MVVM architecture
 */
object RetrofitModule {
    
    // Base URL for JSONPlaceholder API
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    
    // Gson instance for JSON serialization/deserialization
    private val gson: Gson = GsonBuilder()
        .setLenient()
        .create()
    
    // OkHttp client with logging interceptor for debugging
    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Log all HTTP requests/responses
        })
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    // Retrofit instance configured with OkHttp client and Gson converter
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    
    // API service instance for making HTTP calls
    val postApiService: PostApiService = retrofit.create(PostApiService::class.java)
}
