package com.example.vp_alp_test.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vp_alp_test.model.CommunityGetResponse
import com.example.vp_alp_test.model.CommunityModel
import com.example.vp_alp_test.model.GeneralResponseModel
import com.example.vp_alp_test.repository.CommunityRepository
import com.example.vp_alp_test.uiState.CommunityUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Define the possible states for community UI (similar to PostUIState)

class CommunityViewModel(private val communityRepository: CommunityRepository) : ViewModel() {

    private val _communityState = MutableStateFlow<CommunityModel?>(null)
    val communityState: StateFlow<CommunityModel?> get() = _communityState

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    // UI state for community actions (create/update)
    private val _communityUIState = MutableStateFlow<CommunityUIState>(CommunityUIState.Start)
    val communityUIState: StateFlow<CommunityUIState> get() = _communityUIState

    private val _selectedImage = MutableStateFlow<Uri?>(null)
    val selectedImage: StateFlow<Uri?> get() = _selectedImage

    // Update the selected image
    fun setSelectedImage(uri: Uri?) {
        _selectedImage.value = uri
    }

    fun getCommunity(token: String, id: Int) {
        _loading.value = true
        communityRepository.getCommunity(token, id).enqueue(object : Callback<CommunityGetResponse> {
            override fun onResponse(
                call: Call<CommunityGetResponse>,
                response: Response<CommunityGetResponse>
            ) {
                _loading.value = false
                if (response.isSuccessful) {
                    _communityState.value = response.body()?.data
                } else {
                    _errorMessage.value = "Failed to fetch community: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<CommunityGetResponse>, t: Throwable) {
                _loading.value = false
                _errorMessage.value = "Error: ${t.message}"
            }
        })
    }

    // Refactored to handle image and use selectedImage (similar to createPost)
    fun createCommunity(token: String, name: String?, tags: String, bio: String?, context: Context) {
        viewModelScope.launch {
            try {
                _communityUIState.value = CommunityUIState.Loading

                // Upload image if selected
                val imageUrl = _selectedImage.value?.let { uploadImage(it, context) }

                // Call repository to create community with the image URL
                communityRepository.createCommunity(token, name, tags, imageUrl, bio)
                    .enqueue(object : Callback<GeneralResponseModel> {
                        override fun onResponse(
                            call: Call<GeneralResponseModel>,
                            response: Response<GeneralResponseModel>
                        ) {
                            _communityUIState.value = CommunityUIState.Loading
                            if (!response.isSuccessful) {
                                _errorMessage.value = "Failed to create community: ${response.message()}"
                            }
                        }

                        override fun onFailure(call: Call<GeneralResponseModel>, t: Throwable) {
                            _communityUIState.value = CommunityUIState.Failed("Error: ${t.message}")
                        }
                    })

                _selectedImage.value = null
            } catch (e: Exception) {
                _communityUIState.value = CommunityUIState.Failed("Failed to create community: ${e.message}")
            }
        }
    }

    // Refactored to handle image and use selectedImage (similar to createPost)
    fun updateCommunity(token: String, id: Int, name: String?, tags: List<String>?, bio: String?, context: Context) {
        viewModelScope.launch {
            try {
                _communityUIState.value = CommunityUIState.Loading

                // Upload image if selected
                val imageUrl = _selectedImage.value?.let { uploadImage(it, context) }

                // Call repository to update community with the image URL
                communityRepository.updateCommunity(token, id, name, tags, imageUrl, bio)
                    .enqueue(object : Callback<GeneralResponseModel> {
                        override fun onResponse(
                            call: Call<GeneralResponseModel>,
                            response: Response<GeneralResponseModel>
                        ) {
                            _communityUIState.value = CommunityUIState.Loading
                            if (!response.isSuccessful) {
                                _errorMessage.value = "Failed to update community: ${response.message()}"
                            }
                        }

                        override fun onFailure(call: Call<GeneralResponseModel>, t: Throwable) {
                            _communityUIState.value = CommunityUIState.Failed("Error: ${t.message}")
                        }
                    })

                _selectedImage.value = null
            } catch (e: Exception) {
                _communityUIState.value = CommunityUIState.Failed("Failed to update community: ${e.message}")
            }
        }
    }

    // Function to handle image upload (e.g., upload to server or cloud storage)
    private fun uploadImage(uri: Uri, context: Context): String {
        // Implement the actual image upload logic here (e.g., upload to Firebase, AWS, or your server)
        // Return the image URL after successful upload
        return "uploaded_image_url" // Placeholder image URL after upload
    }

    fun deleteCommunity(token: String, id: Int) {
        _loading.value = true
        communityRepository.deleteCommunity(token, id)
            .enqueue(object : Callback<GeneralResponseModel> {
                override fun onResponse(
                    call: Call<GeneralResponseModel>,
                    response: Response<GeneralResponseModel>
                ) {
                    _loading.value = false
                    if (!response.isSuccessful) {
                        _errorMessage.value = "Failed to delete community: ${response.message()}"
                    }
                }

                override fun onFailure(call: Call<GeneralResponseModel>, t: Throwable) {
                    _loading.value = false
                    _errorMessage.value = "Error: ${t.message}"
                }
            })
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}

