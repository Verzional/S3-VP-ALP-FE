package com.example.vp_alp_test.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vp_alp_test.model.CommunityGetResponse
import com.example.vp_alp_test.model.CommunityModel
import com.example.vp_alp_test.model.GeneralResponseModel
import com.example.vp_alp_test.repository.CommunityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommunityViewModel(private val communityRepository: CommunityRepository) : ViewModel() {

    private val _communityState = MutableStateFlow<CommunityModel?>(null)
    val communityState: StateFlow<CommunityModel?> get() = _communityState

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

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

    fun createCommunity(token: String, name: String?, tags: List<String>?, avatar: String?, bio: String?) {
        _loading.value = true
        communityRepository.createCommunity(token, name, tags, avatar, bio)
            .enqueue(object : Callback<GeneralResponseModel> {
                override fun onResponse(
                    call: Call<GeneralResponseModel>,
                    response: Response<GeneralResponseModel>
                ) {
                    _loading.value = false
                    if (!response.isSuccessful) {
                        _errorMessage.value = "Failed to create community: ${response.message()}"
                    }
                }

                override fun onFailure(call: Call<GeneralResponseModel>, t: Throwable) {
                    _loading.value = false
                    _errorMessage.value = "Error: ${t.message}"
                }
            })
    }

    fun updateCommunity(token: String, id: Int, name: String?, tags: List<String>?, avatar: String?, bio: String?) {
        _loading.value = true
        communityRepository.updateCommunity(token, id, name, tags, avatar, bio)
            .enqueue(object : Callback<GeneralResponseModel> {
                override fun onResponse(
                    call: Call<GeneralResponseModel>,
                    response: Response<GeneralResponseModel>
                ) {
                    _loading.value = false
                    if (!response.isSuccessful) {
                        _errorMessage.value = "Failed to update community: ${response.message()}"
                    }
                }

                override fun onFailure(call: Call<GeneralResponseModel>, t: Throwable) {
                    _loading.value = false
                    _errorMessage.value = "Error: ${t.message}"
                }
            })
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
