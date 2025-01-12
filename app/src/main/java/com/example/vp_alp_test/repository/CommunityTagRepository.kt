package com.example.vp_alp_test.repository

import android.util.Log
import com.example.vp_alp_test.util.AppClient
import com.example.vp_alp_test.model.CommunityTagModel
import com.example.vp_alp_test.model.GeneralResponseModel
import com.example.vp_alp_test.service.CommunityTagService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CommunityTagRepository {

    private val communityTagService = AppClient.retrofit.create(CommunityTagService::class.java)

    // Fungsi untuk menambahkan tag ke komunitas
    suspend fun createCommunityTag(communityTag: CommunityTagModel): GeneralResponseModel? = withContext(Dispatchers.IO) {
        try {
            val response = communityTagService.createCommunityTag(communityTag)
            Log.d("CommunityTagRepository", "Response: $response")
            if (response.isSuccessful) {
                return@withContext response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("CommunityTagRepository", "Error creating community-tag", e)
            null
        }
    }

    // Fungsi untuk menghapus tag dari komunitas
    suspend fun deleteCommunityTag(communityId: Int, tagId: Int): GeneralResponseModel? = withContext(Dispatchers.IO) {
        try {
            val response = communityTagService.deleteCommunityTag(communityId, tagId)
            Log.d("CommunityTagRepository", "Response: $response")
            if (response.isSuccessful) {
                return@withContext response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("CommunityTagRepository", "Error deleting community-tag", e)
            null
        }
    }

    // Fungsi untuk mendapatkan semua tag berdasarkan communityId
    suspend fun getCommunityTags(communityId: Int): List<CommunityTagModel> = withContext(Dispatchers.IO) {
        try {
            val response = communityTagService.getCommunityTags(communityId)
            Log.d("CommunityTagRepository", "Response: $response")
            if (response.isSuccessful) {
                return@withContext response.body() ?: emptyList()
            } else {
                return@withContext emptyList()
            }
        } catch (e: Exception) {
            Log.e("CommunityTagRepository", "Error fetching community-tags", e)
            return@withContext emptyList()
        }
    }
}
