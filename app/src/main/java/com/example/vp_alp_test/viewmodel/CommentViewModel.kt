package com.example.vp_alp_test.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vp_alp_test.model.CommentModel
import com.example.vp_alp_test.repository.CommentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CommentViewModel : ViewModel() {
    private val commentRepository = CommentRepository()

    private val _comments = MutableStateFlow<List<CommentModel>>(emptyList())
    val comments: StateFlow<List<CommentModel>> = _comments

    private val _postCommentCount = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val postCommentCount = _postCommentCount.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _commentText = MutableStateFlow("")
    val commentText = _commentText.asStateFlow()

    fun updateCommentText(text: String) {
        _commentText.value = text
    }

    fun loadPostComments(postId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val fetchedComments = commentRepository.getCommentsForPost(postId)
                _comments.value = fetchedComments

                _postCommentCount.update { currentCounts ->
                    currentCounts + (postId to fetchedComments.size)
                }

                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to load comments: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addComment(postId: Int) {
        val commentText = _commentText.value.trim()
        if (commentText.isEmpty()) return

        viewModelScope.launch {
            try {
                val newComment = CommentModel(
                    id = 0, postId = postId, userId = 1, content = commentText
                )

                val addedComment = commentRepository.addComment(newComment)
                loadPostComments(postId)
                _commentText.value = ""
            } catch (e: Exception) {
                _error.value = "Failed to add comment: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}