package com.example.vp_alp_test.model

data class PostModel(
    val id: Int,
    val title: String,
    val content: String,
    val imageUrl: String? = null,
    val userId: Int,
    val communityId: Int
)