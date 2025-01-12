package com.example.alp.model

data class CommentModel (
    val id: Int,
    val content: String,
    val postId: Int,
    val userId: Int
)