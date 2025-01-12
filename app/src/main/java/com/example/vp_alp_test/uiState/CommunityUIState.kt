package com.example.vp_alp_test.uiState

import com.example.vp_alp_test.model.CommunityModel

interface CommunityUIState {
    data class Success(val data: CommunityModel) : CommunityUIState
    object Loading : CommunityUIState
    object Start : CommunityUIState
    data class Failed(val errorMessage: String) : CommunityUIState
}