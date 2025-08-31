package com.example.crud4.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data class representing a Post from the JSONPlaceholder API
 * This is the Model layer in MVVM architecture
 */
data class Post(
    @SerializedName("userId")
    val userId: Int,
    
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("body")
    val body: String
)

/**
 * Data class for creating/updating posts
 */
data class PostRequest(
    val userId: Int,
    val title: String,
    val body: String
)
