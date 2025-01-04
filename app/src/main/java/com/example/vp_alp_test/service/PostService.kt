package com.example.vp_alp_test.service

import com.example.vp_alp_test.model.PostModel
import com.example.vp_alp_test.model.ResponseModel
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PostService {
    @GET("posts/{id}")
    suspend fun getPost(): ResponseModel<PostModel>

    @GET("posts")
    suspend fun getPosts(): ResponseModel<List<PostModel>>

    @POST("posts")
    suspend fun createPost(@Body post: PostModel): ResponseModel<PostModel>

    @PUT("posts/{id}")
    suspend fun updatePost(@Path("id") postId: Int, @Body post: PostModel): ResponseModel<PostModel>

    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") postId: Int): ResponseModel<PostModel>
}
