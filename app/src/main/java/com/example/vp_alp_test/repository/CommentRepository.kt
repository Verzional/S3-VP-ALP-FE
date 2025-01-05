package com.example.vp_alp_test.repository

import android.util.Log
import com.example.vp_alp_test.AppClient
import com.example.vp_alp_test.model.CommentModel
import com.example.vp_alp_test.service.CommentService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CommentRepository {
    private val commentService = AppClient.retrofit.create(CommentService::class.java)

    suspend fun getCommentsForPost(postId: Int): List<CommentModel> = withContext(Dispatchers.IO) {
        try {
            val response = commentService.getCommentsForPost(postId)
            Log.d("CommentRepository", "Response: $response")
            if (response.status == "success") {
                response.data
            } else {
                emptyList()
            }
        } catch (e: Exception){
            Log.e("CommentRepository", "Error fetching comments for post $postId", e)
            emptyList()
        }
    }
}