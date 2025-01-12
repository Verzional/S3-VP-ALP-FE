package com.example.alp.service

import com.example.alp.model.LikeModel
import com.example.alp.model.ResponseModel
import retrofit2.Response
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
    suspend fun unlikePost(@Path("id") likeId: Int): Response<Void>
}
