package com.example.alp.uiState

import com.example.alp.model.CommentModel

sealed interface CommentPostUIState {
    data object Idle : CommentPostUIState
    data object Posting : CommentPostUIState
    data class Success(val comment: CommentModel) : CommentPostUIState
    data class Failed(val errorMessage: String) : CommentPostUIState
}