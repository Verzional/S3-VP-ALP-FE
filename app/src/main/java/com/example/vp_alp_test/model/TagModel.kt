package com.example.vp_alp_test.model

data class TagModel(
    val id: Int,
    val name: String,
    val createdAt: String,
    val updatedAt: String,
    val posts: List<PostModel> = emptyList(),
    val postTags: List<PostTagModel> = emptyList(),
    val communityTags: List<CommunityTagModel> = emptyList()
)