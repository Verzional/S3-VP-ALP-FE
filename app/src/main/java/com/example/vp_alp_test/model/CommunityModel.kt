package com.example.vp_alp_test.model

data class CommunityModel(
    val id: Int = 0,
    val name: String,
    val avatar: String? = null,
    val bio: String? = null,
    val posts: List<PostModel> = emptyList(),
    val members: List<CommunityUserModel> = emptyList(),
    val communityTags: List<CommunityTagModel> = emptyList(),
    val createdAt: String,
    val updatedAt: String
)
data class CommunityGetResponse(
    val data: CommunityModel
)

data class CommunityRequest(
    val name: String? = null,
    val communityTags: String?,
    val avatar: String? = null,
    val bio: String? = null
)