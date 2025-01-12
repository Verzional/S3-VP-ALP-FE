package com.example.vp_alp_test.repository

import android.util.Log
import com.example.vp_alp_test.util.AppClient
import com.example.vp_alp_test.model.CommunityUserModel
import com.example.vp_alp_test.model.ResponseModel
import com.example.vp_alp_test.service.CommunityUserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CommunityUserRepository {

    private val communityUserService = AppClient.retrofit.create(CommunityUserService::class.java)

    suspend fun getMemberForCommunity(communityId:Int): List<CommunityUserModel> = withContext(Dispatchers.IO) {
        try {
            val response = communityUserService.getMemberForCommunity(communityId)
            Log.d("CommunityUserRepository", "Response: $response")
            if (response.status == "success") {
                response.data
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("CommunityUserRepository", "Error fetching member for community $communityId", e)
            emptyList()
        }
    }
    suspend fun addmember(member: CommunityUserModel): CommunityUserModel? = withContext(Dispatchers.IO) {
        try {
            val response = communityUserService.addMember(member)
            Log.d("CommunityUserRepository", "Response: $response")
            if (response.status == "success") {
                response.data
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("CommunityUserRepository", "Error Joining Community", e)
            null
        }
    }

    suspend fun deletemember(memberId: Int): Boolean = withContext(Dispatchers.IO) {
        try {
            val response = communityUserService.deleteMember(memberId)
            Log.d("CommunityUserRepository", "Response: $response")
            response.status == "success"
        } catch (e: Exception) {
            Log.e("CommunityUserRepository", "Error Leaving Community", e)
            false
        }
    }
}