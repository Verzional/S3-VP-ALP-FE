package com.example.vp_alp_test.service

import retrofit2.Call
import com.example.vp_alp_test.model.CommunityGetResponse
import com.example.vp_alp_test.model.CommunityModel
import com.example.vp_alp_test.model.CommunityRequest
import com.example.vp_alp_test.model.GeneralResponseModel
import com.example.vp_alp_test.model.ResponseModel
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CommunityService {
    @GET("/community/{id}")
    fun getCommunity(@Header("X-API-TOKEN") token: String, @Path("id") id: Int): Call<CommunityGetResponse>

    @POST("/community/{id}")
    fun createCommunity(@Header("X-API-TOKEN") token: String, @Body CommunityModel: CommunityRequest): Call<GeneralResponseModel>

    @PUT("/updateCommunity/{id}")
    fun updateCommunity(@Header("X-API-TOKEN") token: String, @Path("id") id: Int, @Body CommunityModel: CommunityRequest): Call<GeneralResponseModel>

    @DELETE("deleteCommunity/{id}")
    fun deleteCommunity(@Header("X-API-TOKEN") token: String, @Path("id") id: Int): Call<GeneralResponseModel>
}