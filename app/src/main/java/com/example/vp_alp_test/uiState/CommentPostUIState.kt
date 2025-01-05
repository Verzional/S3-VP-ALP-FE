package com.example.vp_alp_test.uiState

import com.example.vp_alp_test.model.CommentModel

sealed interface CommentPostUIState {
    data object Idle : CommentPostUIState
    data object Posting : CommentPostUIState
    data class Success(val comment: CommentModel) : CommentPostUIState
    data class Failed(val errorMessage: String) : CommentPostUIState
}