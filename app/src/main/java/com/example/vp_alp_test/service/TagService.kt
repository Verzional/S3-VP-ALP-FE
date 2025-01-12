package com.example.vp_alp_test.service

import com.example.vp_alp_test.model.GeneralResponseModel
import com.example.vp_alp_test.model.TagModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TagService {

    // Endpoint untuk membuat tag baru
    @POST("tags")
    suspend fun createTag(@Body tag: TagModel): Response<GeneralResponseModel>

    // Endpoint untuk mendapatkan tag berdasarkan ID
    @GET("tags/{id}")
    suspend fun getTagById(@Path("id") tagId: Int): Response<TagModel>

    // Endpoint untuk menghapus tag berdasarkan ID
    @DELETE("tags/{id}")
    suspend fun deleteTag(@Path("id") tagId: Int): Response<GeneralResponseModel>
}