package com.example.vp_alp_test.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vp_alp_test.model.TagModel
import com.example.vp_alp_test.repository.TagRepository
import com.example.vp_alp_test.uiState.TagUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TagViewModel(private val tagRepository: TagRepository) : ViewModel() {

    // MutableStateFlow untuk mengelola status UI
    private val _tagState = MutableStateFlow<TagUIState>(TagUIState.Start)
    val tagState: StateFlow<TagUIState> get() = _tagState

    // Fungsi untuk membuat tag baru
    fun createTag(tag: TagModel) {
        _tagState.value = TagUIState.Loading
        viewModelScope.launch {
            val result = tagRepository.createTag(tag)
            if (result != null) {
                _tagState.value = TagUIState.Success(result.data)
            } else {
                _tagState.value = TagUIState.Failed("Failed to create tag.")
            }
        }
    }

    // Fungsi untuk menghapus tag jika tidak ada komunitas yang menggunakannya
    fun deleteTagIfNoCommunityUsage(tagId: Int) {
        _tagState.value = TagUIState.Loading
        viewModelScope.launch {
            val result = tagRepository.deleteTagIfNoCommunityUsage(tagId)
            if (result != null) {
                _tagState.value = TagUIState.Success(result.data)
            } else {
                _tagState.value = TagUIState.Failed("Failed to delete tag or tag is in use by communities.")
            }
        }
    }

    // Fungsi untuk mendapatkan tag berdasarkan ID
    fun getTagById(tagId: Int) {
        _tagState.value = TagUIState.Loading
        viewModelScope.launch {
            try {
                val tagResponse = tagRepository.getTagById(tagId)
                if (tagResponse != null) {
                    _tagState.value = TagUIState.Success(tagResponse.toString())
                } else {
                    _tagState.value = TagUIState.Failed("Tag not found.")
                }
            } catch (e: Exception) {
                _tagState.value = TagUIState.Failed("Error fetching tag: ${e.message}")
            }
        }
    }
}
