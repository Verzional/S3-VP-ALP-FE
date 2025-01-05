package com.example.vp_alp_test.uiState

import com.example.vp_alp_test.model.CommentModel

sealed interface CommentUIState {
    data object Start : CommentUIState
    data object Loading : CommentUIState
    data class Success(
        val comments: List<CommentModel>, val postCommentCount: Map<Int, Int>
    ) : CommentUIState
    data class Failed(val errorMessage: String) : CommentUIState
}