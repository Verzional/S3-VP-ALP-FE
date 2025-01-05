package com.example.vp_alp_test.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vp_alp_test.model.CommentModel
import com.example.vp_alp_test.repository.CommentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CommentViewModel : ViewModel() {
    private val repository = CommentRepository()

    private val _comments = MutableStateFlow<List<CommentModel>>(emptyList())
    val comments: StateFlow<List<CommentModel>> = _comments

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadCommentsForPost(postId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val fetchedComments = repository.getCommentsForPost(postId)
                _comments.value = fetchedComments
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to load comments: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}