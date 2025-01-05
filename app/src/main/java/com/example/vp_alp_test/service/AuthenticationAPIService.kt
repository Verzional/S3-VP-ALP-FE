package com.example.vp_alp_test.service

import com.example.vp_alp_test.model.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationAPIService {
    @POST("/register")
    fun register(@Body registerMap: Map<String, String>): Call<UserResponse>

    @POST("/login")
    fun login(@Body loginMap: Map<String, String>): Call<UserResponse>
}