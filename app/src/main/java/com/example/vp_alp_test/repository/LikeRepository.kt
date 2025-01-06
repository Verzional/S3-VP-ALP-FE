package com.example.vp_alp_test.repository

import android.util.Log
import com.example.vp_alp_test.util.AppClient
import com.example.vp_alp_test.model.LikeModel
import com.example.vp_alp_test.service.LikeService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LikeRepository {
    private val service = AppClient.retrofit.create(LikeService::class.java)

    suspend fun getLikesForPost(postId: Int): List<LikeModel> = withContext(Dispatchers.IO) {
        try {
            val response = service.getLikesForPost(postId)
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
        val response = service.getLikesForUser(userId)
        if (response.status == "success") {
            response.data
        } else {
            emptyList()
        }
    }

    suspend fun likePost(like: LikeModel): LikeModel? = withContext(Dispatchers.IO) {
        try {
            val response = service.likePost(like)
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
            val response = service.unlikePost(likeId)
            Log.d("LikeRepository", "Unlike Response Code: ${response.code()}")
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("LikeRepository", "Error unliking post", e)
            false
        }
    }
}