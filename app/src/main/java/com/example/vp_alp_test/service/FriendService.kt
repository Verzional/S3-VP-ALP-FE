package com.example.vp_alp_test.service

import com.example.vp_alp_test.model.Friend
import com.example.vp_alp_test.model.FriendModel
import com.example.vp_alp_test.model.ResponseModel
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FriendService {
    @GET("friends")
    suspend fun getFriends(s: String, userId: Int): ResponseModel<FriendModel>
    @POST("friends")
    suspend fun createFriend(@Body post: String, userId: Int, friendId: Int): ResponseModel<FriendModel>
    @DELETE("friends/{id}")
    suspend fun deleteFriend(@Path("id") postId: String, userId: Int, friendId: Int): ResponseModel<List<FriendModel>>
}