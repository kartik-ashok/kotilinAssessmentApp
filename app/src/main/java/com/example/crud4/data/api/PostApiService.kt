package com.example.crud4.data.api

import com.example.crud4.data.model.Post
import com.example.crud4.data.model.PostRequest
import retrofit2.Response
import retrofit2.http.*

/**
 * Retrofit API service interface for JSONPlaceholder posts endpoint
 * This defines the contract for all HTTP operations (GET, POST, DELETE, PATCH)
 */
interface PostApiService {
    
    /**
     * GET /posts - Retrieve all posts
     * Returns a list of all posts from the API
     */
    @GET("posts")
    suspend fun getPosts(): Response<List<Post>>
    
    /**
     * GET /posts/{id} - Retrieve a specific post by ID
     * Returns a single post with the specified ID
     */
    @GET("posts/{id}")
    suspend fun getPost(@Path("id") id: Int): Response<Post>
    
    /**
     * POST /posts - Create a new post
     * Creates a new post and returns the created post with generated ID
     */
    @POST("posts")
    suspend fun createPost(@Body post: PostRequest): Response<Post>
    
    /**
     * PATCH /posts/{id} - Update an existing post
     * Updates a post with the specified ID and returns the updated post
     */
    @PATCH("posts/{id}")
    suspend fun updatePost(
        @Path("id") id: Int,
        @Body post: PostRequest
    ): Response<Post>
    
    /**
     * DELETE /posts/{id} - Delete a post
     * Deletes a post with the specified ID
     */
    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") id: Int): Response<Unit>
}
