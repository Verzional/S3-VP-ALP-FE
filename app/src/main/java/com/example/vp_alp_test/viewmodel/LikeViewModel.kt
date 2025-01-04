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

    private var isToggling = false

    fun toggleLike(postId: Int) {
        if (isToggling) return  // Prevent double-toggling
        isToggling = true

        viewModelScope.launch {
            try {
                val isCurrentlyLiked = postId in _userLikes.value
                val currentCount = _postLikeCount.value[postId] ?: 0

                if (isCurrentlyLiked) {
                    // Unlike
                    _userLikes.update { it - postId }
                    _postLikeCount.update { it + (postId to (currentCount - 1)) }

                    val existingLike = likeRepository.getLikesForPost(postId)
                        .firstOrNull { it.userId == 1 }

                    if (existingLike != null) {
                        val success = likeRepository.unlikePost(existingLike.id)
                        if (!success) {
                            // Revert if failed
                            _userLikes.update { it + postId }
                            _postLikeCount.update { it + (postId to currentCount) }
                        }
                    }
                } else {
                    // Like
                    _userLikes.update { it + postId }
                    _postLikeCount.update { it + (postId to (currentCount + 1)) }

                    val newLike = LikeModel(
                        id = 0,
                        postId = postId,
                        userId = 1
                    )

                    val addedLike = likeRepository.likePost(newLike)
                    if (addedLike == null) {
                        // Revert if failed
                        _userLikes.update { it - postId }
                        _postLikeCount.update { it + (postId to currentCount) }
                    }
                }
            } catch (e: Exception) {
                Log.e("LikeViewModel", "Error toggling like", e)
            } finally {
                isToggling = false
            }
        }
    }

    fun loadPostLikes(postId: Int) {
        if (isToggling) return  // Skip loading if currently toggling

        viewModelScope.launch {
            try {
                val likes = likeRepository.getLikesForPost(postId)

                // Only update if not currently toggling
                if (!isToggling) {
                    _postLikeCount.update { currentCounts ->
                        currentCounts + (postId to likes.size)
                    }

                    val isLikedByUser = likes.any { it.userId == 1 }
                    _userLikes.update { currentLikes ->
                        if (isLikedByUser) currentLikes + postId
                        else currentLikes - postId
                    }
                }
            } catch (e: Exception) {
                Log.e("LikeViewModel", "Error loading post likes", e)
            }
        }
    }
}

