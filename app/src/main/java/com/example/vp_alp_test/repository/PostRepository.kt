package com.example.vp_alp_test.repository

import com.example.vp_alp_test.AppClient
import com.example.vp_alp_test.model.PostModel
import com.example.vp_alp_test.service.PostService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PostRepository {
    private val postService = AppClient.retrofit.create(PostService::class.java)

    suspend fun fetchPosts(): List<PostModel> = withContext(Dispatchers.IO) {
        val response = postService.getPosts()
        if (response.status == "success") {
            response.data
        } else {
            emptyList()
        }
    }

    suspend fun addPost(post: PostModel): PostModel = withContext(Dispatchers.IO) {
        val response = postService.createPost(post)
        if (response.status == "success") {
            response.data
        } else {
            throw Exception("Failed to add post")
        }
    }
}