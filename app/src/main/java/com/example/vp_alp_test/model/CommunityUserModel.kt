package com.example.vp_alp_test.model

data class CommunityUserModel(
    val userId: Int,
    val communityId: Int,
    val user: UserModel,
    val community: CommunityModel
)