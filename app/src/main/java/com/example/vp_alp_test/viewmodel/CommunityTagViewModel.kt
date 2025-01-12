package com.example.vp_alp_test.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vp_alp_test.model.CommunityTagModel
import com.example.vp_alp_test.repository.CommunityTagRepository
import com.example.vp_alp_test.uiState.CommunityUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CommunityTagViewModel(private val communityTagRepository: CommunityTagRepository) : ViewModel() {

    // MutableStateFlow untuk mengelola status UI
    private val _communityTagState = MutableStateFlow<CommunityUIState>(CommunityUIState.Start)
    val communityTagState: StateFlow<CommunityUIState> get() = _communityTagState

    // Fungsi untuk menambahkan tag ke komunitas
    fun createCommunityTag(communityTag: CommunityTagModel) {
        _communityTagState.value = CommunityUIState.Loading
        viewModelScope.launch {
            val result = communityTagRepository.createCommunityTag(communityTag)
            if (result != null) {
                _communityTagState.value = CommunityUIState.Success(result.data)
            } else {
                _communityTagState.value = CommunityUIState.Failed("Failed to create community tag.")
            }
        }
    }

    // Fungsi untuk menghapus tag dari komunitas
    fun deleteCommunityTag(communityId: Int, tagId: Int) {
        _communityTagState.value = CommunityUIState.Loading
        viewModelScope.launch {
            val result = communityTagRepository.deleteCommunityTag(communityId, tagId)
            if (result != null) {
                _communityTagState.value = CommunityUIState.Success(result.data)
            } else {
                _communityTagState.value = CommunityUIState.Failed("Failed to delete community tag.")
            }
        }
    }

    // Fungsi untuk mendapatkan semua tag berdasarkan communityId
    fun getCommunityTags(communityId: Int) {
        _communityTagState.value = CommunityUIState.Loading
        viewModelScope.launch {
            try {
                val tags = communityTagRepository.getCommunityTags(communityId)
                if (tags.isNotEmpty()) {
                    _communityTagState.value = CommunityUIState.Success(tags.toString())
                } else {
                    _communityTagState.value = CommunityUIState.Failed("No tags found for this community.")
                }
            } catch (e: Exception) {
                _communityTagState.value = CommunityUIState.Failed("Error fetching community tags: ${e.message}")
            }
        }
    }
}
