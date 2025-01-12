package com.example.alp.uiState

import com.example.alp.model.CommentModel

sealed interface CommentUIState {
    data object Start : CommentUIState
    data object Loading : CommentUIState
    data class Success(
        val comments: List<CommentModel>, val postCommentCount: Map<Int, Int>
    ) : CommentUIState

    data class Failed(val errorMessage: String) : CommentUIState
}