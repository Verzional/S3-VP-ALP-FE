package com.example.vp_alp_test.service

import com.example.vp_alp_test.model.GeneralResponseModel
import com.example.vp_alp_test.model.CommunityTagModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CommunityTagService {

    // Endpoint untuk menambahkan tag ke komunitas
    @POST("community-tags")
    suspend fun createCommunityTag(@Body communityTag: CommunityTagModel): Response<GeneralResponseModel>

    // Endpoint untuk menghapus tag dari komunitas
    @DELETE("community-tags/{communityId}/{tagId}")
    suspend fun deleteCommunityTag(@Path("communityId") communityId: Int, @Path("tagId") tagId: Int): Response<GeneralResponseModel>

    // Endpoint untuk mendapatkan semua tags berdasarkan communityId
    @GET("community-tags/{communityId}")
    suspend fun getCommunityTags(@Path("communityId") communityId: Int): Response<List<CommunityTagModel>>
}
