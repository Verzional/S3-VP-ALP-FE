package com.example.alp.uiState

import com.example.alp.model.PostModel

sealed interface PostUIState {
    data object Loading : PostUIState
    data object Start : PostUIState
    data class Success(val posts: List<PostModel>) : PostUIState
    data class Failed(val errorMessage: String) : PostUIState
}