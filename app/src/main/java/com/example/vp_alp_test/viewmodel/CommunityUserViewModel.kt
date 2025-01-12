package com.example.vp_alp_test.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vp_alp_test.model.CommunityUserModel
import com.example.vp_alp_test.repository.CommunityUserRepository
import com.example.vp_alp_test.uiState.CommunityUserUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CommunityUserViewModel(private val communityUserRepository: CommunityUserRepository) : ViewModel() {

    // MutableStateFlow untuk mengelola status UI
    private val _communityUserState = MutableStateFlow<CommunityUserUIState>(CommunityUserUIState.Start)
    val communityUserState: StateFlow<CommunityUserUIState> get() = _communityUserState

    // Fungsi untuk mendapatkan anggota komunitas berdasarkan communityId
    fun getMemberForCommunity(communityId: Int) {
        _communityUserState.value = CommunityUserUIState.Loading
        viewModelScope.launch {
            try {
                val members = communityUserRepository.getMemberForCommunity(communityId)
                val communityMemberCount = members.groupBy { it.communityId }
                    .mapValues { it.value.size }

                if (members.isNotEmpty()) {
                    _communityUserState.value = CommunityUserUIState.Success(
                        member = members.map { it.userId }.toSet(),
                        CommunityMemberCount = communityMemberCount
                    )
                } else {
                    _communityUserState.value = CommunityUserUIState.Failed("No members found for this community.")
                }
            } catch (e: Exception) {
                _communityUserState.value = CommunityUserUIState.Failed("Error fetching community members: ${e.message}")
            }
        }
    }

    // Fungsi untuk menambahkan anggota baru ke komunitas
    fun addMember(member: CommunityUserModel) {
        _communityUserState.value = CommunityUserUIState.Loading
        viewModelScope.launch {
            try {
                val result = communityUserRepository.addmember(member)
                if (result != null) {
                    _communityUserState.value = CommunityUserUIState.Success(
                        member = setOf(result.userId),
                        CommunityMemberCount = mapOf(result.communityId to 1)
                    )
                } else {
                    _communityUserState.value = CommunityUserUIState.Failed("Failed to add member to the community.")
                }
            } catch (e: Exception) {
                _communityUserState.value = CommunityUserUIState.Failed("Error adding member: ${e.message}")
            }
        }
    }

    // Fungsi untuk menghapus anggota dari komunitas
    fun deleteMember(memberId: Int) {
        _communityUserState.value = CommunityUserUIState.Loading
        viewModelScope.launch {
            try {
                val success = communityUserRepository.deletemember(memberId)
                if (success) {
                    _communityUserState.value = CommunityUserUIState.Success(
                        member = setOf(memberId),
                        CommunityMemberCount = emptyMap()
                    )
                } else {
                    _communityUserState.value = CommunityUserUIState.Failed("Failed to remove member.")
                }
            } catch (e: Exception) {
                _communityUserState.value = CommunityUserUIState.Failed("Error removing member: ${e.message}")
            }
        }
    }
}
