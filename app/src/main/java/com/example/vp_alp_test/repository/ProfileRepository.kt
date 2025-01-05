package com.example.vp_alp_test.repository

import com.example.vp_alp_test.model.GeneralResponseModel
import com.example.vp_alp_test.model.GetResponse
import com.example.vp_alp_test.model.ProfileRequest
import com.example.vp_alp_test.model.UserModel
import com.example.vp_alp_test.service.ProfileService
import retrofit2.Call
import retrofit2.Response

interface ProfileRepository {
    fun getUserProfile(token: String, id: Int): Call<GetResponse>

    fun createUserProfile(token: String, username: String?, email: String?, avatar: String?, bio: String?): Call<GeneralResponseModel>

    fun updateUserProfile(token: String, id: Int, username: String?, email: String?, avatar: String?, bio: String?): Call<GeneralResponseModel>

    fun deleteUserProfile(token: String, id: Int): Call<GeneralResponseModel>
}

class NetworkProfileRepository(
    private val profileService: ProfileService
) : ProfileRepository {

    override fun getUserProfile(token: String, id: Int): Call<GetResponse> {
        return profileService.getUserProfile("Bearer $token", id)
    }

    override fun createUserProfile(
        token: String,
        username: String?,
        email: String?,
        avatar: String?,
        bio: String?
    ): Call<GeneralResponseModel> {
        return profileService.createUserProfile(
            token,
            ProfileRequest(username, email, avatar, bio)
        )
    }

    override fun updateUserProfile(
        token: String,
        id: Int,
        username: String?,
        email: String?,
        avatar: String?,
        bio: String?
    ): Call<GeneralResponseModel> {
        return profileService.updateUserProfile(
            token,
            id,
            ProfileRequest(username, email, avatar, bio)
        )
    }

    override fun deleteUserProfile(token: String, id: Int): Call<GeneralResponseModel> {
        return profileService.deleteUserProfile(token, id)
    }
}
