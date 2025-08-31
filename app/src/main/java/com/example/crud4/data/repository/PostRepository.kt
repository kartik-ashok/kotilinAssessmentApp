package com.example.crud4.data.repository

import android.util.Log
import com.example.crud4.data.api.RetrofitModule
import com.example.crud4.data.model.Post
import com.example.crud4.data.model.PostRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository class that acts as a single source of truth for post data
 * This is part of the data layer in MVVM architecture and handles all API operations
 */
class PostRepository {
    
    companion object {
        private const val TAG = "PostRepository"
    }
    
    private val apiService = RetrofitModule.postApiService
    
    /**
     * Fetches all posts from the API
     * Uses IO dispatcher for network operations
     */
    suspend fun getPosts(): Result<List<Post>> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "getPosts: Starting API call to fetch posts")
            val response = apiService.getPosts()
            
            if (response.isSuccessful) {
                val postsList = response.body() ?: emptyList()
                Log.d(TAG, "getPosts: Successfully fetched ${postsList.size} posts")
                Result.success(postsList)
            } else {
                Log.e(TAG, "getPosts: API call failed with code ${response.code()}")
                Result.failure(Exception("Failed to fetch posts: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "getPosts: Exception occurred", e)
            Result.failure(e)
        }
    }
    
    /**
     * Fetches a specific post by ID
     */
    suspend fun getPost(id: Int): Result<Post> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "getPost: Starting API call to fetch post with ID $id")
            val response = apiService.getPost(id)
            
            if (response.isSuccessful) {
                response.body()?.let { post ->
                    Log.d(TAG, "getPost: Successfully fetched post with ID ${post.id}")
                    Result.success(post)
                } ?: run {
                    Log.w(TAG, "getPost: Post not found for ID $id")
                    Result.failure(Exception("Post not found"))
                }
            } else {
                Log.e(TAG, "getPost: API call failed with code ${response.code()}")
                Result.failure(Exception("Failed to fetch post: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "getPost: Exception occurred", e)
            Result.failure(e)
        }
    }
    
    /**
     * Creates a new post
     */
    suspend fun createPost(post: PostRequest): Result<Post> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "createPost: Starting API call to create post")
            val response = apiService.createPost(post)
            
            if (response.isSuccessful) {
                response.body()?.let { createdPost ->
                    Log.d(TAG, "createPost: Successfully created post with ID ${createdPost.id}")
                    Result.success(createdPost)
                } ?: run {
                    Log.e(TAG, "createPost: Response body is null")
                    Result.failure(Exception("Failed to create post"))
                }
            } else {
                Log.e(TAG, "createPost: API call failed with code ${response.code()}")
                Result.failure(Exception("Failed to create post: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "createPost: Exception occurred", e)
            Result.failure(e)
        }
    }
    
    /**
     * Updates an existing post
     */
    suspend fun updatePost(id: Int, post: PostRequest): Result<Post> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "updatePost: Starting API call to update post with ID $id")
            val response = apiService.updatePost(id, post)
            
            if (response.isSuccessful) {
                response.body()?.let { updatedPost ->
                    Log.d(TAG, "updatePost: Successfully updated post with ID ${updatedPost.id}")
                    Result.success(updatedPost)
                } ?: run {
                    Log.e(TAG, "updatePost: Response body is null")
                    Result.failure(Exception("Failed to update post"))
                }
            } else {
                Log.e(TAG, "updatePost: API call failed with code ${response.code()}")
                Result.failure(Exception("Failed to update post: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "updatePost: Exception occurred", e)
            Result.failure(e)
        }
    }
    
    /**
     * Deletes a post
     */
    suspend fun deletePost(id: Int): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "deletePost: Starting API call to delete post with ID $id")
            val response = apiService.deletePost(id)
            
            if (response.isSuccessful) {
                Log.d(TAG, "deletePost: Successfully deleted post with ID $id")
                Result.success(Unit)
            } else {
                Log.e(TAG, "deletePost: API call failed with code ${response.code()}")
                Result.failure(Exception("Failed to delete post: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "deletePost: Exception occurred", e)
            Result.failure(e)
        }
    }
}
