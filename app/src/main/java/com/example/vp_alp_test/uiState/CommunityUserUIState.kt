package com.example.vp_alp_test.uiState

sealed interface CommunityUserUIState {
    data class Success(val member: Set<Int>, val CommunityMemberCount: Map<Int, Int>) : CommunityUserUIState
    data object Loading : CommunityUserUIState
    data object Start : CommunityUserUIState
    data class Failed(val errorMessage: String) : CommunityUserUIState
}