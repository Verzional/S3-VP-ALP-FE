package com.example.vp_alp_test.uiState

sealed interface LikeUIState {
    data class Success(val userLikes: Set<Int>, val postLikeCount: Map<Int, Int>) : LikeUIState
    data object Loading : LikeUIState
    data object Start : LikeUIState
    data class Failed(val errorMessage: String) : LikeUIState
}