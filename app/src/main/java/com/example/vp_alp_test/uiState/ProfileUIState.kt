package com.example.vp_alp_test.uiState

import com.example.vp_alp_test.model.UserModel

sealed interface ProfileUIState {
    data class Success(val data: UserModel) : ProfileUIState
    object Loading : ProfileUIState
    object Start : ProfileUIState
    data class Failed(val errorMessage: String) : ProfileUIState
}
