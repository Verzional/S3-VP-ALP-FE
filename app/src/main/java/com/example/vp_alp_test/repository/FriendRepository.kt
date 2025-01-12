package com.example.vp_alp_test.repository

import com.example.vp_alp_test.util.AppClient
import com.example.vp_alp_test.model.Friend
import com.example.vp_alp_test.model.FriendModel
import com.example.vp_alp_test.model.ResponseModel
import com.example.vp_alp_test.service.FriendService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FriendRepository {

    private val friendService = AppClient.retrofit.create(FriendService::class.java)

    // Menambahkan teman
    suspend fun addFriend(token: String, userId: Int, friendId: Int): ResponseModel<FriendModel> = withContext(Dispatchers.IO) {
        val response = friendService.createFriend("Bearer $token", userId, friendId)
        if (response.status == "success") {
            response
        } else {
            throw Exception("Failed to add friend")
        }
    }

    // Menghapus teman (unfriend)
    suspend fun removeFriend(token: String, userId: Int, friendId: Int): ResponseModel<List<FriendModel>> = withContext(Dispatchers.IO) {
        val response = friendService.deleteFriend("Bearer $token", userId, friendId)
        if (response.status == "success") {
            response
        } else {
            throw Exception("Failed to remove friend")
        }
    }
    suspend fun getFriends(token: String, userId: Int): ResponseModel<FriendModel> = withContext(Dispatchers.IO) {
        val response = friendService.getFriends("Bearer $token", userId)
        return@withContext response // Mengembalikan daftar teman
    }
}