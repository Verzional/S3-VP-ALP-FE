package com.example.vp_alp_test.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.vp_alp_test.CommunityApplication
import com.example.vp_alp_test.model.UserModel
import com.example.vp_alp_test.repository.ProfileRepository
import com.example.vp_alp_test.uiState.ProfileUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {

    private val _profileState = MutableStateFlow<ProfileUIState>(ProfileUIState.Start)
    val profileState: StateFlow<ProfileUIState> = _profileState


    private val _actionState = MutableStateFlow<ProfileUIState>(ProfileUIState.Start)
    val actionState: StateFlow<ProfileUIState> = _actionState

    // Fetch user profile
    fun getUserProfile(token: String, id: Int) {
        _profileState.value = ProfileUIState.Loading
        viewModelScope.launch {
            try {
                val response = repository.getUserProfile(token, id).awaitResponse()
                if (response.isSuccessful) {
                    val user = response.body()?.data
                    if (user != null) {
                        _profileState.value = ProfileUIState.Success(user)
                    } else {
                        _profileState.value = ProfileUIState.Failed("User data not found")
                    }
                } else {
                    _profileState.value = ProfileUIState.Failed("Failed to fetch profile: ${response.message()}")
                }
            } catch (e: Exception) {
                _profileState.value = ProfileUIState.Failed("An error occurred: ${e.message}")
            }
        }
    }


    // Create user profile
    fun createUserProfile(token: String, username: String?, email: String?, avatar: String?, bio: String?) {
        _actionState.value = ProfileUIState.Loading
        viewModelScope.launch {
            try {
                val response = repository.createUserProfile(token, username, email, avatar, bio).awaitResponse()
                if (response.isSuccessful) {
                    _actionState.value = ProfileUIState.Success(
                        UserModel(
                            id = 0, // Replace with actual ID if available in the response
                            username = username ?: "",
                            email = email ?: "",
                            avatar = avatar,
                            bio = bio,
                            createdAt = "", // Replace with actual value from the response
                            updatedAt = "",
                            token = token
                        )
                    )
                } else {
                    _actionState.value = ProfileUIState.Failed("Failed to create profile: ${response.message()}")
                }
            } catch (e: Exception) {
                _actionState.value = ProfileUIState.Failed("An error occurred: ${e.message}")
            }
        }
    }

    // Update user profile
    fun updateUserProfile(token: String, id: Int, username: String?, email: String?, avatar: String?, bio: String?) {
        _actionState.value = ProfileUIState.Loading
        viewModelScope.launch {
            try {
                val response = repository.updateUserProfile(token, id, username, email, avatar, bio).awaitResponse()
                if (response.isSuccessful) {
                    _actionState.value = ProfileUIState.Success(
                        UserModel(
                            id = id,
                            username = username ?: "",
                            email = email ?: "",
                            avatar = avatar,
                            bio = bio,
                            createdAt = "", // Replace with actual value if needed
                            updatedAt = "", // Replace with actual value if needed
                            token = token
                        )
                    )
                } else {
                    _actionState.value = ProfileUIState.Failed("Failed to update profile: ${response.message()}")
                }
            } catch (e: Exception) {
                _actionState.value = ProfileUIState.Failed("An error occurred: ${e.message}")
            }
        }
    }

    // Delete user profile
    fun deleteUserProfile(token: String, id: Int) {
        _actionState.value = ProfileUIState.Loading
        viewModelScope.launch {
            try {
                val response = repository.deleteUserProfile(token, id).awaitResponse()
                if (response.isSuccessful) {
                    _actionState.value = ProfileUIState.Success(
                        UserModel(
                            id = id,
                            username = "",
                            email = "",
                            avatar = null,
                            bio = null,
                            createdAt = "",
                            updatedAt = "",
                            token = null
                        )
                    )
                } else {
                    _actionState.value = ProfileUIState.Failed("Failed to delete profile: ${response.message()}")
                }
            } catch (e: Exception) {
                _actionState.value = ProfileUIState.Failed("An error occurred: ${e.message}")
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CommunityApplication)
                val profileRepository = application.container.profileRepository
                ProfileViewModel(profileRepository)
            }
        }
    }
}
