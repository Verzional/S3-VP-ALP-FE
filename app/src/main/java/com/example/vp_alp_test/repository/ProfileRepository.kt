package com.example.vp_alp_test.repository

import android.util.Log
import com.example.vp_alp_test.model.GeneralResponseModel
import com.example.vp_alp_test.model.GetResponse
import com.example.vp_alp_test.model.ProfileRequest
import com.example.vp_alp_test.service.ProfileService
import retrofit2.Call

interface ProfileRepository {
    fun getUserProfile(token: String, id: Int): Call<GetResponse>

    fun createUserProfile(
        token: String, username: String?, email: String?, avatar: String?, bio: String?
    ): Call<GeneralResponseModel>

    fun updateUserProfile(
        token: String, id: Int, username: String?, email: String?, avatar: String?, bio: String?
    ): Call<GeneralResponseModel>

    fun deleteUserProfile(token: String, id: Int): Call<GeneralResponseModel>
}

class NetworkProfileRepository(
    private val profileService: ProfileService
) : ProfileRepository {

    private companion object {
        const val TAG = "NetworkProfileRepository"
    }

    override fun getUserProfile(token: String, id: Int): Call<GetResponse> {
        Log.d(TAG, "Fetching user profile with token: $token and id: $id")
        // Remove the "Bearer " prefix if your API doesn't expect it
        val call = profileService.getUserProfile(token, id)

        // Debug logging
        call.request().let { request ->
            Log.d(TAG, "Request URL: ${request.url}")
            Log.d(TAG, "Request Headers: ${request.headers}")
            Log.d(TAG, "Request Method: ${request.method}")
        }

        return call
    }

    override fun createUserProfile(
        token: String, username: String?, email: String?, avatar: String?, bio: String?
    ): Call<GeneralResponseModel> {
        Log.d(TAG, "Creating user profile with token: $token")
        Log.d(TAG, "Profile details: username=$username, email=$email, avatar=$avatar, bio=$bio")
        val call = profileService.createUserProfile(
            token, ProfileRequest(avatar, bio)
        )
        Log.d(TAG, "createUserProfile Call initiated.")
        return call
    }

    override fun updateUserProfile(
        token: String, id: Int, username: String?, email: String?, avatar: String?, bio: String?
    ): Call<GeneralResponseModel> {
        Log.d(TAG, "Updating user profile with token: $token and id: $id")
        Log.d(TAG, "Updated details: username=$username, email=$email, avatar=$avatar, bio=$bio")
        val call = profileService.updateUserProfile(
            token, id, ProfileRequest(username, email, avatar, bio)
        )
        Log.d(TAG, "updateUserProfile Call initiated.")
        return call
    }

    override fun deleteUserProfile(token: String, id: Int): Call<GeneralResponseModel> {
        Log.d(TAG, "Deleting user profile with token: $token and id: $id")
        val call = profileService.deleteUserProfile(token, id)
        Log.d(TAG, "deleteUserProfile Call initiated.")
        return call
    }
}
