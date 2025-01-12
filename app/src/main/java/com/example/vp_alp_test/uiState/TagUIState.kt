package com.example.vp_alp_test.uiState

import com.example.vp_alp_test.model.TagModel

interface TagUIState {
    data class Success(val tags: TagModel) : TagUIState
    object Loading : TagUIState
    object Start : TagUIState
    data class Failed(val errorMessage: String) : TagUIState
}