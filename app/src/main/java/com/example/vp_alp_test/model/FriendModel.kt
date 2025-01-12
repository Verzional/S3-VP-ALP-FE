package com.example.vp_alp_test.model

data class FriendModel(
    val id: Int,
    val userId: Int,
    val friendId: Int,
    val user: UserModel,
    val friend: UserModel
)