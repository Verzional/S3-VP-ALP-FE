package com.example.vp_alp_test.uiState

import com.example.vp_alp_test.model.PostModel

sealed interface PostUIState {
    data object Loading : PostUIState
    data object Start : PostUIState
    data class Success(val posts: List<PostModel>) : PostUIState
    data class Failed(val errorMessage: String) : PostUIState
}