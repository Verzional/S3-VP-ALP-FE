package com.example.vp_alp_test.service

import com.example.vp_alp_test.model.GeneralResponseModel
import com.example.vp_alp_test.model.GetResponse
import com.example.vp_alp_test.model.ProfileRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProfileService {
    @GET("/profile/{id}")
    fun getUserProfile(@Header("X-API-TOKEN") token: String, @Path("id") userId: Int): Call<GetResponse>

    @POST("/profile/{id}")
    fun createUserProfile(@Header("X-API-TOKEN") token: String, @Body todoModel: ProfileRequest): Call<GeneralResponseModel>

    @PUT("/updateProfile/{id}")
    fun updateUserProfile(@Header("X-API-TOKEN") token: String, @Path("id") todoId: Int, @Body todoModel: ProfileRequest): Call<GeneralResponseModel>

    @DELETE("deleteProfile/{id}")
    fun deleteUserProfile(@Header("X-API-TOKEN") token: String, @Path("id") todoId: Int): Call<GeneralResponseModel>
}