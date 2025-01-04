package com.example.vp_alp_test.repository

import android.util.Log
import com.example.vp_alp_test.AppClient
import com.example.vp_alp_test.model.LikeModel
import com.example.vp_alp_test.service.LikeService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LikeRepository {
    private val likeService = AppClient.retrofit.create(LikeService::class.java)

    suspend fun getLikesForPost(postId: Int): List<LikeModel> = withContext(Dispatchers.IO) {
        try {
            val response = likeService.getLikesForPost(postId)
            Log.d("LikeRepository", "Response: $response")
            if (response.status == "success") {
                response.data
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("LikeRepository", "Error fetching likes for post $postId", e)
            emptyList()
        }
    }

    suspend fun getLikesForUser(userId: Int): List<LikeModel> = withContext(Dispatchers.IO) {
        val response = likeService.getLikesForUser(userId)
        if (response.status == "success") {
            response.data
        } else {
            emptyList()
        }
    }

    suspend fun likePost(like: LikeModel): LikeModel? = withContext(Dispatchers.IO) {
        try {
            val response = likeService.likePost(like)
            Log.d("LikeRepository", "Response: $response")
            if (response.status == "success") {
                response.data
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("LikeRepository", "Error liking post", e)
            null
        }
    }

    suspend fun unlikePost(likeId: Int): Boolean = withContext(Dispatchers.IO) {
        try {
            val response = likeService.unlikePost(likeId)
            Log.d("LikeRepository", "Response: $response")
            response.status == "success"
        } catch (e: Exception) {
            Log.e("LikeRepository", "Error unliking post", e)
            false
        }
    }
}