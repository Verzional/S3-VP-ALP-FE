package com.example.vp_alp_test.uiState

interface CommunityUIState {
    data class Success(val data: String) : CommunityUIState
    object Loading : CommunityUIState
    object Start : CommunityUIState
    data class Failed(val errorMessage: String) : CommunityUIState
}