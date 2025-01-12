package com.example.vp_alp_test.uiState

import com.example.vp_alp_test.model.FriendModel

interface FriendUIState {
    data class Success(val friends: List<FriendModel>) : FriendUIState
    data object Loading : FriendUIState
    data object Start : FriendUIState
    data class Failed(val errorMessage: String) : FriendUIState
}