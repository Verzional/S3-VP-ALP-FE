package com.example.vp_alp_test.model

data class CommunityTagModel(
    val communityId: Int,
    val tagId: Int,
    val community: CommunityModel,
    val tag: TagModel
)