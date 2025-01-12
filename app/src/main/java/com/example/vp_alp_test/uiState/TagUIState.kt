package com.example.vp_alp_test.uiState

interface TagUIState {
    data class Success(val tags: String) : TagUIState
    object Loading : TagUIState
    object Start : TagUIState
    data class Failed(val errorMessage: String) : TagUIState
}