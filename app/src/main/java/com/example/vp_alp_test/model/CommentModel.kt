package com.example.vp_alp_test.model

data class CommentModel (
    val id: Int,
    val content: String,
    val postId: Int,
    val userId: Int
)