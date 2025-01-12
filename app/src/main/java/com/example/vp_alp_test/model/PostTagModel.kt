package com.example.vp_alp_test.model

data class PostTagModel(
    val postId: Int,
    val tagId: Int,
    val post: PostModel,
    val tag: TagModel
)