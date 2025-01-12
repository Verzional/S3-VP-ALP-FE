package com.example.vp_alp_test.repository

import com.example.vp_alp_test.model.GeneralResponseModel
import com.example.vp_alp_test.model.CommunityRequest
import com.example.vp_alp_test.model.CommunityGetResponse
import com.example.vp_alp_test.service.CommunityService
import retrofit2.Call


interface CommunityRepository {
    fun getCommunity(token: String, id: Int): Call<CommunityGetResponse>

    fun createCommunity(token: String, name: String?, communityTags: String, avatar: String?, bio: String?): Call<GeneralResponseModel>

    fun updateCommunity(token: String, id: Int, name: String?, communityTags: List<String>?, avatar: String?, bio: String?): Call<GeneralResponseModel>

    fun deleteCommunity(token: String, id: Int): Call<GeneralResponseModel>
}

class NetworkCommunityRepository(
    private val communityService: CommunityService
) : CommunityRepository {

    override fun getCommunity(token: String, id: Int): Call<CommunityGetResponse> {
        return communityService.getCommunity("Bearer $token", id)
    }

    override fun createCommunity(
        token: String,
        name: String?,
        communityTags: String,
        avatar: String?,
        bio: String?
    ): Call<GeneralResponseModel> {
        return communityService.createCommunity(
            token,
            CommunityRequest(avatar, bio)
        )
    }

    override fun updateCommunity(
        token: String,
        id: Int,
        name: String?,
        communityTags: List<String>?,
        avatar: String?,
        bio: String?
    ): Call<GeneralResponseModel> {
        return communityService.updateCommunity(
            token,
            id,
            CommunityRequest(name, communityTags.toString(), avatar, bio)
        )
    }

    override fun deleteCommunity(token: String, id: Int): Call<GeneralResponseModel> {
        return communityService.deleteCommunity(token, id)
    }
}