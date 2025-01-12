package com.example.vp_alp_test.model

import com.google.gson.annotations.SerializedName

data class UserResponse(

    @SerializedName("data") val data: UserModel,
    @SerializedName("status") val status: String
)


//data class UserData(
//    val id: Int?,
//    val token: String?
//)

data class UserModel(
    val id: Int,
    val username: String,
    val email: String,
    val avatar: String?,
    val bio: String?,
    val createdAt: String,
    val updatedAt: String,
    val token: String?
)

data class GetAllUser(
    val data: List<UserModel>
)

data class GetResponse(
    val id: Int,
    val username: String,
    val email: String,
    val createdAt: String,
    val updatedAt: String,
    val avatar: String? = null,
    val bio: String? = null
)

data class ProfileRequest(
    val username: String? = null,
    val email: String? = null,
    val avatar: String? = null,
    val bio: String? = null
)


data class UserProfile(
    val id: Int,
    val username: String,
    val email: String,
    val avatar: String?,
    val bio: String?,
    val createdAt: String,
    val updatedAt: String,
    val friends: List<Friend>,
    val posts: List<Post>,
    val communities: List<Community>
)

data class Friend(
    val id: Int,
    val username: String,
    val avatar: String?
)

data class Post(
    val id: Int,
    val title: String,
    val content: String,
    val createdAt: String
)

data class Community(
    val id: Int,
    val name: String,
    val description: String?
)
