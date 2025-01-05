package com.example.vp_alp_test.service

import com.example.vp_alp_test.model.CommentModel
import com.example.vp_alp_test.model.ResponseModel
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CommentService {
    @GET("comments/post/{id}")
    suspend fun getCommentsForPost(@Path("id") postId: Int): ResponseModel<List<CommentModel>>

    @POST("comments")
    suspend fun commentOnPost(@Body comment: CommentModel): ResponseModel<CommentModel>

    @DELETE("comments/{id}")
    suspend fun deleteComment(@Path("id") commentId: Int): ResponseModel<Unit?>
}