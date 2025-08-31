package com.example.crud4.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crud4.data.model.Post
import com.example.crud4.data.model.PostRequest
import com.example.crud4.data.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel class that manages UI state and business logic for posts
 * This is the ViewModel layer in MVVM architecture that:
 * - Holds UI state (posts list, loading states, errors)
 * - Handles business logic and API calls through Repository
 * - Provides StateFlow for UI observation (Compose compatible)
 * - Survives configuration changes (like screen rotation)
 */
class PostViewModel : ViewModel() {
    
    companion object {
        private const val TAG = "PostViewModel"
    }
    
    private val repository = PostRepository()
    
    // StateFlow for posts list - UI observes this for updates (Compose compatible)
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()
    
    // StateFlow for loading state - UI shows/hides loading indicators
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // StateFlow for error messages - UI displays error states
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    // StateFlow for operation success messages
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()
    
    init {
        try {
            Log.d(TAG, "PostViewModel initialized, loading posts...")
            // Load posts when ViewModel is created
            loadPosts()
        } catch (e: Exception) {
            Log.e(TAG, "Error in PostViewModel init", e)
            _error.value = "Failed to initialize: ${e.message}"
        }
    }
    
    /**
     * Loads all posts from the API
     * Uses coroutines for asynchronous operation
     */
    fun loadPosts() {
        try {
            Log.d(TAG, "loadPosts: Starting to load posts")
            viewModelScope.launch {
                try {
                    _isLoading.value = true
                    _error.value = null
                    
                    Log.d(TAG, "loadPosts: Calling repository.getPosts()")
                    repository.getPosts()
                        .onSuccess { postsList ->
                            Log.d(TAG, "loadPosts: Successfully loaded ${postsList.size} posts")
                            _posts.value = postsList
                            _isLoading.value = false
                        }
                        .onFailure { exception ->
                            Log.e(TAG, "loadPosts: Failed to load posts", exception)
                            _error.value = exception.message ?: "Unknown error occurred"
                            _isLoading.value = false
                        }
                } catch (e: Exception) {
                    Log.e(TAG, "loadPosts: Unexpected error in coroutine", e)
                    _error.value = "Unexpected error: ${e.message}"
                    _isLoading.value = false
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "loadPosts: Error launching coroutine", e)
            _error.value = "Failed to start loading: ${e.message}"
        }
    }
    
    /**
     * Creates a new post with dummy data
     * Demonstrates POST API call functionality
     */
    fun createPost() {
        try {
            Log.d(TAG, "createPost: Starting to create post")
            val dummyPost = PostRequest(
                userId = 1,
                title = "New Post Created at ${System.currentTimeMillis()}",
                body = "This is a dummy post created to test the POST API endpoint. " +
                        "It demonstrates how to create new posts through the API."
            )
            
            viewModelScope.launch {
                try {
                    _isLoading.value = true
                    _error.value = null
                    
                    Log.d(TAG, "createPost: Calling repository.createPost()")
                    repository.createPost(dummyPost)
                        .onSuccess { createdPost ->
                            Log.d(TAG, "createPost: Successfully created post with ID ${createdPost.id}")
                            // Add the new post to the existing list
                            val currentPosts = _posts.value.toMutableList()
                            currentPosts.add(0, createdPost) // Add at the beginning
                            _posts.value = currentPosts
                            _message.value = "Post created successfully!"
                            _isLoading.value = false
                        }
                        .onFailure { exception ->
                            Log.e(TAG, "createPost: Failed to create post", exception)
                            _error.value = "Failed to create post: ${exception.message}"
                            _isLoading.value = false
                        }
                } catch (e: Exception) {
                    Log.e(TAG, "createPost: Unexpected error in coroutine", e)
                    _error.value = "Unexpected error: ${e.message}"
                    _isLoading.value = false
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "createPost: Error launching coroutine", e)
            _error.value = "Failed to start creating post: ${e.message}"
        }
    }
    
    /**
     * Updates an existing post with dummy data
     * Demonstrates PATCH API call functionality
     */
    fun updatePost(postId: Int) {
        try {
            Log.d(TAG, "updatePost: Starting to update post with ID $postId")
            val dummyUpdate = PostRequest(
                userId = 1,
                title = "Updated Post at ${System.currentTimeMillis()}",
                body = "This post has been updated to test the PATCH API endpoint. " +
                        "It demonstrates how to modify existing posts through the API."
            )
            
            viewModelScope.launch {
                try {
                    _isLoading.value = true
                    _error.value = null
                    
                    Log.d(TAG, "updatePost: Calling repository.updatePost()")
                    repository.updatePost(postId, dummyUpdate)
                        .onSuccess { updatedPost ->
                            Log.d(TAG, "updatePost: Successfully updated post with ID ${updatedPost.id}")
                            // Update the post in the existing list
                            val currentPosts = _posts.value.toMutableList()
                            val index = currentPosts.indexOfFirst { it.id == postId }
                            if (index != -1) {
                                currentPosts[index] = updatedPost
                                _posts.value = currentPosts
                                _message.value = "Post updated successfully!"
                            } else {
                                Log.w(TAG, "updatePost: Post with ID $postId not found in current list")
                            }
                            _isLoading.value = false
                        }
                        .onFailure { exception ->
                            Log.e(TAG, "updatePost: Failed to update post", exception)
                            _error.value = "Failed to update post: ${exception.message}"
                            _isLoading.value = false
                        }
                } catch (e: Exception) {
                    Log.e(TAG, "updatePost: Unexpected error in coroutine", e)
                    _error.value = "Unexpected error: ${e.message}"
                    _isLoading.value = false
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "updatePost: Error launching coroutine", e)
            _error.value = "Failed to start updating post: ${e.message}"
        }
    }
    
    /**
     * Deletes a post by ID
     * Demonstrates DELETE API call functionality
     */
    fun deletePost(postId: Int) {
        try {
            Log.d(TAG, "deletePost: Starting to delete post with ID $postId")
            viewModelScope.launch {
                try {
                    _isLoading.value = true
                    _error.value = null
                    
                    Log.d(TAG, "deletePost: Calling repository.deletePost()")
                    repository.deletePost(postId)
                        .onSuccess {
                            Log.d(TAG, "deletePost: Successfully deleted post with ID $postId")
                            // Remove the post from the existing list
                            val currentPosts = _posts.value.toMutableList()
                            currentPosts.removeAll { it.id == postId }
                            _posts.value = currentPosts
                            _message.value = "Post deleted successfully!"
                            _isLoading.value = false
                        }
                        .onFailure { exception ->
                            Log.e(TAG, "deletePost: Failed to delete post", exception)
                            _error.value = "Failed to delete post: ${exception.message}"
                            _isLoading.value = false
                        }
                } catch (e: Exception) {
                    Log.e(TAG, "deletePost: Unexpected error in coroutine", e)
                    _error.value = "Unexpected error: ${e.message}"
                    _isLoading.value = false
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "deletePost: Error launching coroutine", e)
            _error.value = "Failed to start deleting post: ${e.message}"
        }
    }
    
    /**
     * Clears error messages
     */
    fun clearError() {
        try {
            _error.value = null
            Log.d(TAG, "clearError: Error message cleared")
        } catch (e: Exception) {
            Log.e(TAG, "clearError: Error clearing error message", e)
        }
    }
    
    /**
     * Clears success messages
     */
    fun clearMessage() {
        try {
            _message.value = null
            Log.d(TAG, "clearMessage: Success message cleared")
        } catch (e: Exception) {
            Log.e(TAG, "clearMessage: Error clearing success message", e)
        }
    }
}
