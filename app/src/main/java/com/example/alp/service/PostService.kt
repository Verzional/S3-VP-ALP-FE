package com.example.alp.service

import com.example.alp.model.PostModel
import com.example.alp.model.ResponseModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface PostService {
    @GET("posts/{id}")
    suspend fun getPost(): ResponseModel<PostModel>

    @GET("posts")
    suspend fun getPosts(): ResponseModel<List<PostModel>>

    @POST("posts")
    suspend fun createPost(@Body post: PostModel): ResponseModel<PostModel>

    @Multipart
    @POST("posts")
    suspend fun createPostWithImage(
        @Part("title") title: RequestBody,
        @Part("content") content: RequestBody,
        @Part("userId") userId: RequestBody,
        @Part("communityId") communityId: RequestBody,
        @Part image: MultipartBody.Part
    ): ResponseModel<PostModel>

    @PUT("posts/{id}")
    suspend fun updatePost(@Path("id") postId: Int, @Body post: PostModel): ResponseModel<PostModel>

    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") postId: Int): ResponseModel<PostModel>
}
