package com.example.alp.uiState

sealed interface LikeUIState {
    data object Start : LikeUIState
    data object Loading : LikeUIState
    data class Success(
        val postLikeCount: Map<Int, Int>, val userLikes: Set<Int>
    ) : LikeUIState

    data class Failed(val errorMessage: String) : LikeUIState
}