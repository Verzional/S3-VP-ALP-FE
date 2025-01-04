package com.example.vp_alp_test.service

import com.example.vp_alp_test.model.LikeModel
import com.example.vp_alp_test.model.ResponseModel
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface LikeService {
    @GET("likes/post/{id}")
    suspend fun getLikesForPost(@Path("id") postId: Int): ResponseModel<List<LikeModel>>

    @GET("likes/user/{id}")
    suspend fun getLikesForUser(@Path("id") userId: Int): ResponseModel<List<LikeModel>>

    @POST("likes")
    suspend fun likePost(@Body like: LikeModel): ResponseModel<LikeModel>

    @DELETE("likes/{id}")
    suspend fun unlikePost(@Path("id") likeId: Int): ResponseModel<Unit>
}