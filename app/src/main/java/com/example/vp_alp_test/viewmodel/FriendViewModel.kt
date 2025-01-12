package com.example.vp_alp_test.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vp_alp_test.repository.FriendRepository
import com.example.vp_alp_test.uiState.FriendUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FriendViewModel(private val friendRepository: FriendRepository) : ViewModel() {

    // MutableStateFlow untuk mengelola status UI
    private val _friendState = MutableStateFlow<FriendUIState>(FriendUIState.Start)
    val friendState: StateFlow<FriendUIState> get() = _friendState

    // Fungsi untuk menambahkan teman
    fun addFriend(token: String, userId: Int, friendId: Int) {
        _friendState.value = FriendUIState.Loading
        viewModelScope.launch {
            try {
                val result = friendRepository.addFriend(token, userId, friendId)
                _friendState.value = FriendUIState.Success(listOf(result.data)) // Menampilkan teman yang berhasil ditambahkan
            } catch (e: Exception) {
                _friendState.value = FriendUIState.Failed("Failed to add friend: ${e.message}")
            }
        }
    }

    // Fungsi untuk menghapus teman
    fun removeFriend(token: String, userId: Int, friendId: Int) {
        _friendState.value = FriendUIState.Loading
        viewModelScope.launch {
            try {
                val result = friendRepository.removeFriend(token, userId, friendId)
                _friendState.value = FriendUIState.Success(result.data) // Menampilkan teman yang tersisa setelah dihapus
            } catch (e: Exception) {
                _friendState.value = FriendUIState.Failed("Failed to remove friend: ${e.message}")
            }
        }
    }

    // Fungsi untuk mendapatkan daftar teman
    fun getFriends(token: String, userId: Int) {
        _friendState.value = FriendUIState.Loading
        viewModelScope.launch {
            try {
                val response = friendRepository.getFriends(token, userId)
                _friendState.value = FriendUIState.Success(listOf(response.data)) // Menampilkan daftar teman
            } catch (e: Exception) {
                _friendState.value = FriendUIState.Failed("Error fetching friends: ${e.message}")
            }
        }
    }
}
