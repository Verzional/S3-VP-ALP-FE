package com.example.vp_alp_test.viewmodel

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.vp_alp_test.CommunityApplication
import com.example.vp_alp_test.model.GeneralResponseModel
import com.example.vp_alp_test.model.UserModel
import com.example.vp_alp_test.repository.ProfileRepository
import com.example.vp_alp_test.repository.UserRepository
import com.example.vp_alp_test.uiState.ProfileUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse

class ProfileViewModel(
    private val repository: ProfileRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _profileState = MutableStateFlow<ProfileUIState>(ProfileUIState.Start)
    val profileState: StateFlow<ProfileUIState> = _profileState

    private val _logoutStatus = MutableLiveData<Result<GeneralResponseModel>>()
    val logoutStatus: LiveData<Result<GeneralResponseModel>> get() = _logoutStatus

    private val _actionState = MutableStateFlow<ProfileUIState>(ProfileUIState.Start)
    val actionState: StateFlow<ProfileUIState> = _actionState

    companion object {
        private const val TAG = "ProfileViewModel"

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CommunityApplication)
                val profileRepository = application.container.profileRepository
                val userRepository = application.container.userRepository
                ProfileViewModel(profileRepository, userRepository)
            }
        }
    }

    // Fetch user profile
    fun getUserProfile(token: String, id: Int) {
        Log.d(TAG, "Fetching user profile for userId=$id")
        _profileState.value = ProfileUIState.Loading
        viewModelScope.launch {
            try {
                val response = repository.getUserProfile(token, id).awaitResponse()
                if (response.isSuccessful) {
                    val user = response.body()?.data
                    Log.d(TAG, "User profile fetched successfully: $user")
                    if (user != null) {
                        _profileState.value = ProfileUIState.Success(user)
                    } else {
                        Log.e(TAG, "User data not found")
                        _profileState.value = ProfileUIState.Failed("User data not found")
                    }
                } else {
                    val error = "Failed to fetch profile: ${response.message()}"
                    Log.e(TAG, error)
                    _profileState.value = ProfileUIState.Failed(error)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching profile: ${e.message}", e)
                _profileState.value = ProfileUIState.Failed("An error occurred: ${e.message}")
            }
        }
    }

    // Create user profile
    fun createUserProfile(
        token: String,
        username: String?,
        email: String?,
        avatar: String?,
        bio: String?
    ) {
        Log.d(TAG, "Creating user profile for username=$username")
        _actionState.value = ProfileUIState.Loading
        viewModelScope.launch {
            try {
                val response = repository.createUserProfile(token, username, email, avatar, bio)
                    .awaitResponse()
                if (response.isSuccessful) {
                    Log.d(TAG, "User profile created successfully")
                    _actionState.value = ProfileUIState.Success(
                        UserModel(
                            id = 0,
                            username = username ?: "",
                            email = email ?: "",
                            avatar = avatar,
                            bio = bio,
                            createdAt = "",
                            updatedAt = "",
                            token = token
                        )
                    )
                } else {
                    val error = "Failed to create profile: ${response.message()}"
                    Log.e(TAG, error)
                    _actionState.value = ProfileUIState.Failed(error)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error creating profile: ${e.message}", e)
                _actionState.value = ProfileUIState.Failed("An error occurred: ${e.message}")
            }
        }
    }

    // Update user profile
    fun updateUserProfile(
        token: String,
        id: Int,
        username: String?,
        email: String?,
        avatar: String?,
        bio: String?
    ) {
        Log.d(TAG, "Updating user profile for userId=$id")
        _actionState.value = ProfileUIState.Loading
        viewModelScope.launch {
            try {
                val response = repository.updateUserProfile(token, id, username, email, avatar, bio)
                    .awaitResponse()
                if (response.isSuccessful) {
                    Log.d(TAG, "User profile updated successfully")
                    val updatedUser = response.body()?.data
                    if (updatedUser != null) {
                        _actionState.value = ProfileUIState.Success(
                            UserModel(
                                id = id,
                                username = username ?: "",
                                email = email ?: "",
                                avatar = avatar ?: "",
                                bio = bio ?: "",
                                createdAt = "",
                                updatedAt = "",
                                token = token
                            )
                        )
                    } else {
                        val error = "Invalid data received"
                        Log.e(TAG, error)
                        _actionState.value = ProfileUIState.Failed("Failed to update profile: $error")
                    }
                } else {
                    val error = "Failed to update profile: ${response.message()}"
                    Log.e(TAG, error)
                    _actionState.value = ProfileUIState.Failed(error)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error updating profile: ${e.message}", e)
                _actionState.value = ProfileUIState.Failed("An error occurred: ${e.message}")
            }
        }
    }

    // Logout user
    fun logoutUser() {
        Log.d(TAG, "Logging out user")
        viewModelScope.launch {
            try {
                val token = userRepository.currentUserToken.first()
                Log.d(TAG, "User token: $token")

                val logoutCall = userRepository.logout(token)
                logoutCall.enqueue(object : Callback<GeneralResponseModel> {
                    override fun onResponse(
                        call: Call<GeneralResponseModel>,
                        response: Response<GeneralResponseModel>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            Log.d(TAG, "Logout successful: ${response.body()}")
                            _logoutStatus.postValue(Result.success(response.body()!!))

                            viewModelScope.launch {
                                userRepository.saveUserToken("")
                                userRepository.saveUserId(-1)
                                Log.d(TAG, "User data cleared from DataStore")
                            }
                        } else {
                            val errorMessage = response.errorBody()?.string() ?: response.message()
                            Log.e(TAG, "Logout failed: $errorMessage")
                            _logoutStatus.postValue(
                                Result.failure(Exception("Logout failed: $errorMessage"))
                            )
                        }
                    }

                    override fun onFailure(call: Call<GeneralResponseModel>, t: Throwable) {
                        Log.e(TAG, "Logout failed: ${t.message}", t)
                        _logoutStatus.postValue(Result.failure(Exception("Logout failed: ${t.message}")))
                    }
                })
            } catch (e: Exception) {
                Log.e(TAG, "Error during logout: ${e.message}", e)
                _logoutStatus.postValue(Result.failure(e))
            }
        }
    }
}
