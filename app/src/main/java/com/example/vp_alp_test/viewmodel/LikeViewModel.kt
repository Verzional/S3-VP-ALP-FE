package com.example.vp_alp_test.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vp_alp_test.model.LikeModel
import com.example.vp_alp_test.repository.LikeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LikeViewModel : ViewModel() {
    private val likeRepository = LikeRepository()

    private val _postLikeCount = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val postLikeCount = _postLikeCount.asStateFlow()

    private val _userLikes = MutableStateFlow<Set<Int>>(emptySet())
    val userLikes = _userLikes.asStateFlow()

    fun toggleLike(postId: Int) {
        viewModelScope.launch {
            try {
                val isCurrentlyLiked = postId in _userLikes.value
                val currentCount = _postLikeCount.value[postId] ?: 0

                if (isCurrentlyLiked) {
                    // Unlike - Update UI state immediately for better UX
                    _userLikes.update { it - postId }
                    _postLikeCount.update { it + (postId to (currentCount - 1)) }

                    // Then perform the API call
                    val existingLike = likeRepository.getLikesForPost(postId)
                        .firstOrNull { it.userId == 1 }

                    if (existingLike != null) {
                        val success = likeRepository.unlikePost(existingLike.id)
                        if (!success) {
                            // Revert UI state if API call failed
                            _userLikes.update { it + postId }
                            _postLikeCount.update { it + (postId to currentCount) }
                        }
                    }
                } else {
                    // Like - Update UI state immediately
                    _userLikes.update { it + postId }
                    _postLikeCount.update { it + (postId to (currentCount + 1)) }

                    // Then perform the API call
                    val newLike = LikeModel(
                        id = 0,
                        postId = postId,
                        userId = 1
                    )

                    val addedLike = likeRepository.likePost(newLike)
                    if (addedLike == null) {
                        // Revert UI state if API call failed
                        _userLikes.update { it - postId }
                        _postLikeCount.update { it + (postId to currentCount) }
                    }
                }
            } catch (e: Exception) {
                Log.e("LikeViewModel", "Error toggling like", e)
            }
        }
    }

    fun loadPostLikes(postId: Int) {
        viewModelScope.launch {
            try {
                val likes = likeRepository.getLikesForPost(postId)
                // Update like count
                _postLikeCount.update { currentCounts ->
                    currentCounts + (postId to likes.size)
                }

                // Update user's like status
                val isLikedByUser = likes.any { it.userId == 1 }
                _userLikes.update { currentLikes ->
                    if (isLikedByUser) currentLikes + postId
                    else currentLikes - postId
                }
            } catch (e: Exception) {
                Log.e("LikeViewModel", "Error loading post likes", e)
            }
        }
    }
}
