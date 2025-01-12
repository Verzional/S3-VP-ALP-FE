package com.example.vp_alp_test.service

import com.example.vp_alp_test.model.Community
import com.example.vp_alp_test.model.CommunityUserModel
import com.example.vp_alp_test.model.ResponseModel
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CommunityUserService {
    @GET("member/user/{id}")
    suspend fun getMemberForCommunity(@Path("id") userId: Int): ResponseModel<List<CommunityUserModel>>
    @POST("member")
    suspend fun addMember(@Body member: CommunityUserModel): ResponseModel<CommunityUserModel>
    @DELETE("member/{id}")
    suspend fun deleteMember(@Path("id") memberId:Int): ResponseModel<CommunityUserModel>
}