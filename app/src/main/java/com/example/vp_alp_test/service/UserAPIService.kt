package com.example.vp_alp_test.service

import com.example.vp_alp_test.model.GeneralResponseModel
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST


interface UserAPIService {
    @POST("/logout")
    fun logout(@Header("X-API-TOKEN") token: String): Call<GeneralResponseModel>



}
